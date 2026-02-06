//go:build linux

// Package mount implements the FUSE filesystem that proxies to a remote gRPC backend.
package mount

import (
	"context"
	"path"
	"syscall"
	"time"

	"github.com/hanwen/go-fuse/v2/fs"
	"github.com/hanwen/go-fuse/v2/fuse"
	"github.com/you/remotefs/internal/cache"
	"github.com/you/remotefs/internal/fileproto"
	pb "github.com/you/remotefs/proto/gen/remotefs"
)

func pbAttrToFuse(a *pb.Attr) fuse.Attr {
	if a == nil {
		return fuse.Attr{}
	}
	return fuse.Attr{
		Ino:     a.Ino,
		Size:    a.Size,
		Mode:    a.Mode,
		Owner:   fuse.Owner{Uid: a.Uid, Gid: a.Gid},
		Atime:   a.Atime,
		Mtime:   a.Mtime,
		Ctime:   a.Ctime,
		Blksize: uint32(a.Blksize),
		Blocks:  a.Blocks,
		Nlink:   1,
	}
}

func errnoFromU32(e uint32) syscall.Errno {
	return syscall.Errno(e)
}

// RemoteRoot is the root of the remote FUSE filesystem.
type RemoteRoot struct {
	fs.Inode
	Client       *fileproto.Client
	CachedClient *cache.CachedClient
	FileCache    *cache.FileCache // file-backed cache for passthrough
	Passthrough  bool             // enable FUSE passthrough mode
}

var _ fs.NodeLookuper = (*RemoteRoot)(nil)
var _ fs.NodeGetattrer = (*RemoteRoot)(nil)
var _ fs.NodeReaddirer = (*RemoteRoot)(nil)
var _ fs.NodeStatfser = (*RemoteRoot)(nil)

func (r *RemoteRoot) getClient() *fileproto.Client { return r.Client }

// do sends a request through the cached client if available, otherwise directly.
func (r *RemoteRoot) do(ctx context.Context, req *pb.FileRequest) (*pb.FileResponse, error) {
	if r.CachedClient != nil {
		return r.CachedClient.Do(ctx, req)
	}
	return r.Client.Do(ctx, req)
}

func (r *RemoteRoot) Lookup(ctx context.Context, name string, out *fuse.EntryOut) (*fs.Inode, syscall.Errno) {
	resp, err := r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: "", Name: name}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	lr := resp.GetLookup()
	if lr == nil {
		return nil, syscall.EIO
	}
	attr := pbAttrToFuse(lr.Attr)
	out.Attr = attr
	out.SetEntryTimeout(time.Second)
	out.SetAttrTimeout(time.Second)
	childPath := name
	stable := fs.StableAttr{Mode: attr.Mode, Ino: attr.Ino}
	child := r.NewInode(ctx, &remoteNode{path: childPath, root: r}, stable)
	return child, 0
}

func (r *RemoteRoot) Getattr(ctx context.Context, fh fs.FileHandle, out *fuse.AttrOut) syscall.Errno {
	resp, err := r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_GetAttr{GetAttr: &pb.GetAttrRequest{Path: ""}},
	})
	if err != nil {
		return syscall.EIO
	}
	if resp.Errno != 0 {
		return errnoFromU32(resp.Errno)
	}
	ga := resp.GetGetAttr()
	if ga == nil {
		return syscall.EIO
	}
	out.Attr = pbAttrToFuse(ga.Attr)
	out.SetTimeout(time.Second)
	return 0
}

func (r *RemoteRoot) Readdir(ctx context.Context) (fs.DirStream, syscall.Errno) {
	resp, err := r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Opendir{Opendir: &pb.OpendirRequest{Path: ""}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	openResp := resp.GetOpen()
	if openResp == nil {
		return nil, syscall.EIO
	}
	handleID := openResp.HandleId
	resp2, err := r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Readdir{Readdir: &pb.ReaddirRequest{Path: "", HandleId: handleID}},
	})
	_, _ = r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Releasedir{Releasedir: &pb.ReleasedirRequest{Path: "", HandleId: handleID}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp2.Errno != 0 {
		return nil, errnoFromU32(resp2.Errno)
	}
	rd := resp2.GetReaddir()
	if rd == nil {
		return nil, syscall.EIO
	}
	entries := make([]fuse.DirEntry, 0, len(rd.Entries))
	for _, e := range rd.Entries {
		entries = append(entries, fuse.DirEntry{Name: e.Name, Mode: e.Mode, Ino: e.Ino})
	}
	return fs.NewListDirStream(entries), 0
}

func (r *RemoteRoot) Statfs(ctx context.Context, out *fuse.StatfsOut) syscall.Errno {
	resp, err := r.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Statfs{Statfs: &pb.StatfsRequest{Path: ""}},
	})
	if err != nil {
		return syscall.EIO
	}
	if resp.Errno != 0 {
		return errnoFromU32(resp.Errno)
	}
	st := resp.GetStatfs()
	if st == nil {
		return syscall.EIO
	}
	out.Blocks = st.Blocks
	out.Bfree = st.Bfree
	out.Bavail = st.Bavail
	out.Files = st.Files
	out.Ffree = st.Ffree
	out.Bsize = uint32(st.Bsize)
	out.NameLen = uint32(st.Namelen)
	return 0
}

// remoteNode is a non-root node with a path.
type remoteNode struct {
	fs.Inode
	path string
	root *RemoteRoot
}

func (n *remoteNode) getClient() *fileproto.Client { return n.root.Client }

// do sends a request through the cached client if available, otherwise directly.
func (n *remoteNode) do(ctx context.Context, req *pb.FileRequest) (*pb.FileResponse, error) {
	if n.root.CachedClient != nil {
		return n.root.CachedClient.Do(ctx, req)
	}
	return n.root.Client.Do(ctx, req)
}

var _ fs.NodeLookuper = (*remoteNode)(nil)
var _ fs.NodeGetattrer = (*remoteNode)(nil)
var _ fs.NodeOpener = (*remoteNode)(nil)
var _ fs.NodeReaddirer = (*remoteNode)(nil)
var _ fs.NodeCreater = (*remoteNode)(nil)
var _ fs.NodeMkdirer = (*remoteNode)(nil)
var _ fs.NodeUnlinker = (*remoteNode)(nil)
var _ fs.NodeRmdirer = (*remoteNode)(nil)
var _ fs.NodeRenamer = (*remoteNode)(nil)
var _ fs.NodeSymlinker = (*remoteNode)(nil)
var _ fs.NodeReadlinker = (*remoteNode)(nil)
var _ fs.NodeSetattrer = (*remoteNode)(nil)
var _ fs.NodeStatfser = (*remoteNode)(nil)

func (n *remoteNode) Lookup(ctx context.Context, name string, out *fuse.EntryOut) (*fs.Inode, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: n.path, Name: name}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	lr := resp.GetLookup()
	if lr == nil {
		return nil, syscall.EIO
	}
	out.Attr = pbAttrToFuse(lr.Attr)
	out.SetEntryTimeout(time.Second)
	out.SetAttrTimeout(time.Second)
	childPath := path.Join(n.path, name)
	attr := lr.Attr
	stable := fs.StableAttr{Mode: attr.Mode, Ino: attr.Ino}
	child := n.NewInode(ctx, &remoteNode{path: childPath, root: n.root}, stable)
	return child, 0
}

func (n *remoteNode) Getattr(ctx context.Context, fh fs.FileHandle, out *fuse.AttrOut) syscall.Errno {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_GetAttr{GetAttr: &pb.GetAttrRequest{Path: n.path}},
	})
	if err != nil {
		return syscall.EIO
	}
	if resp.Errno != 0 {
		return errnoFromU32(resp.Errno)
	}
	ga := resp.GetGetAttr()
	if ga == nil {
		return syscall.EIO
	}
	out.Attr = pbAttrToFuse(ga.Attr)
	out.SetTimeout(time.Second)
	return 0
}

func (n *remoteNode) Open(ctx context.Context, flags uint32) (fs.FileHandle, uint32, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: n.path, Flags: flags}},
	})
	if err != nil {
		return nil, 0, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, 0, errnoFromU32(resp.Errno)
	}
	or := resp.GetOpen()
	if or == nil {
		return nil, 0, syscall.EIO
	}
	
	fh := &remoteFileHandle{
		path:         n.path,
		handleID:     or.HandleId,
		client:       n.getClient(),
		cachedClient: n.root.CachedClient,
		fileCache:    n.root.FileCache,
	}
	
	// Try to enable passthrough if file cache is available and passthrough is enabled
	var fuseFlags uint32 = 0
	if n.root.Passthrough && n.root.FileCache != nil && n.root.FileCache.Enabled() {
		// Get file attributes to know size and mtime
		attrResp, err := n.do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_GetAttr{GetAttr: &pb.GetAttrRequest{Path: n.path}},
		})
		if err == nil && attrResp.Errno == 0 {
			ga := attrResp.GetGetAttr()
			if ga != nil && ga.Attr != nil {
				size := int64(ga.Attr.Size)
				mtime := int64(ga.Attr.Mtime)
				
				// Try to get or create cached file
				downloadFn := func(offset, length int64) ([]byte, error) {
					readResp, err := fh.do(ctx, &pb.FileRequest{
						Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
							Path: fh.path, HandleId: fh.handleID, Offset: offset, Size: uint32(length),
						}},
					})
					if err != nil {
						return nil, err
					}
					if readResp.Errno != 0 {
						return nil, syscall.Errno(readResp.Errno)
					}
					rr := readResp.GetRead()
					if rr == nil {
						return nil, syscall.EIO
					}
					return rr.Data, nil
				}
				
				cachedFile, err := n.root.FileCache.GetOrCreate(n.path, size, mtime, downloadFn)
				if err == nil && cachedFile != nil {
					fh.cachedFile = cachedFile
					fh.passthroughFd = cachedFile.Fd()
					// Set FOPEN_PASSTHROUGH flag (bit 1 << 11 = 2048)
					fuseFlags |= 1 << 11
				}
			}
		}
	}
	
	return fh, fuseFlags, 0
}

func (n *remoteNode) Readdir(ctx context.Context) (fs.DirStream, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Opendir{Opendir: &pb.OpendirRequest{Path: n.path}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	openResp := resp.GetOpen()
	if openResp == nil {
		return nil, syscall.EIO
	}
	handleID := openResp.HandleId
	resp2, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Readdir{Readdir: &pb.ReaddirRequest{Path: n.path, HandleId: handleID}},
	})
	_, _ = n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Releasedir{Releasedir: &pb.ReleasedirRequest{Path: n.path, HandleId: handleID}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp2.Errno != 0 {
		return nil, errnoFromU32(resp2.Errno)
	}
	rd := resp2.GetReaddir()
	if rd == nil {
		return nil, syscall.EIO
	}
	entries := make([]fuse.DirEntry, 0, len(rd.Entries))
	for _, e := range rd.Entries {
		entries = append(entries, fuse.DirEntry{Name: e.Name, Mode: e.Mode, Ino: e.Ino})
	}
	return fs.NewListDirStream(entries), 0
}

func (n *remoteNode) Create(ctx context.Context, name string, flags uint32, mode uint32, out *fuse.EntryOut) (*fs.Inode, fs.FileHandle, uint32, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Create{Create: &pb.CreateRequest{Path: n.path, Name: name, Flags: flags, Mode: mode}},
	})
	if err != nil {
		return nil, nil, 0, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, nil, 0, errnoFromU32(resp.Errno)
	}
	cr := resp.GetCreate()
	if cr == nil {
		return nil, nil, 0, syscall.EIO
	}
	childPath := path.Join(n.path, name)
	out.Attr = pbAttrToFuse(cr.Attr)
	out.SetEntryTimeout(time.Second)
	out.SetAttrTimeout(time.Second)
	stable := fs.StableAttr{Mode: cr.Attr.Mode, Ino: cr.Attr.Ino}
	child := n.NewInode(ctx, &remoteNode{path: childPath, root: n.root}, stable)
	fh := &remoteFileHandle{
		path:         childPath,
		handleID:     cr.HandleId,
		client:       n.getClient(),
		cachedClient: n.root.CachedClient,
		fileCache:    n.root.FileCache,
	}
	return child, fh, 0, 0
}

func (n *remoteNode) Mkdir(ctx context.Context, name string, mode uint32, out *fuse.EntryOut) (*fs.Inode, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Mkdir{Mkdir: &pb.MkdirRequest{Path: n.path, Name: name, Mode: mode}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	mr := resp.GetMkdir()
	if mr == nil {
		return nil, syscall.EIO
	}
	childPath := path.Join(n.path, name)
	out.Attr = pbAttrToFuse(mr.Attr)
	out.SetEntryTimeout(time.Second)
	out.SetAttrTimeout(time.Second)
	stable := fs.StableAttr{Mode: mr.Attr.Mode, Ino: mr.Attr.Ino}
	child := n.NewInode(ctx, &remoteNode{path: childPath, root: n.root}, stable)
	return child, 0
}

func (n *remoteNode) Unlink(ctx context.Context, name string) syscall.Errno {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Unlink{Unlink: &pb.UnlinkRequest{Path: n.path, Name: name}},
	})
	if err != nil {
		return syscall.EIO
	}
	return errnoFromU32(resp.Errno)
}

func (n *remoteNode) Rmdir(ctx context.Context, name string) syscall.Errno {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Rmdir{Rmdir: &pb.RmdirRequest{Path: n.path, Name: name}},
	})
	if err != nil {
		return syscall.EIO
	}
	return errnoFromU32(resp.Errno)
}

func (n *remoteNode) Rename(ctx context.Context, name string, newParent fs.InodeEmbedder, newName string, flags uint32) syscall.Errno {
	newPath := ""
	if rn, ok := newParent.(*remoteNode); ok {
		newPath = rn.path
	} else if _, ok := newParent.(*RemoteRoot); ok {
		newPath = ""
	}
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Rename{Rename: &pb.RenameRequest{Path: n.path, OldName: name, NewPath: newPath, NewName: newName}},
	})
	if err != nil {
		return syscall.EIO
	}
	return errnoFromU32(resp.Errno)
}

func (n *remoteNode) Symlink(ctx context.Context, target, name string, out *fuse.EntryOut) (*fs.Inode, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Symlink{Symlink: &pb.SymlinkRequest{Path: n.path, Name: name, Target: target}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	sr := resp.GetSymlink()
	if sr == nil {
		return nil, syscall.EIO
	}
	childPath := path.Join(n.path, name)
	out.Attr = pbAttrToFuse(sr.Attr)
	out.SetEntryTimeout(time.Second)
	out.SetAttrTimeout(time.Second)
	stable := fs.StableAttr{Mode: sr.Attr.Mode, Ino: sr.Attr.Ino}
	child := n.NewInode(ctx, &remoteNode{path: childPath, root: n.root}, stable)
	return child, 0
}

func (n *remoteNode) Readlink(ctx context.Context) ([]byte, syscall.Errno) {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Readlink{Readlink: &pb.ReadlinkRequest{Path: n.path}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	rr := resp.GetReadlink()
	if rr == nil {
		return nil, syscall.EIO
	}
	return []byte(rr.Target), 0
}

func (n *remoteNode) Setattr(ctx context.Context, fh fs.FileHandle, in *fuse.SetAttrIn, out *fuse.AttrOut) syscall.Errno {
	req := &pb.SetAttrRequest{Path: n.path}
	if sz, ok := in.GetSize(); ok {
		req.Size = &sz
	}
	if mtime, ok := in.GetMTime(); ok {
		mt := uint64(mtime.Unix())
		req.Mtime = &mt
	}
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_SetAttr{SetAttr: req},
	})
	if err != nil {
		return syscall.EIO
	}
	if resp.Errno != 0 {
		return errnoFromU32(resp.Errno)
	}
	sa := resp.GetSetAttr()
	if sa == nil {
		return syscall.EIO
	}
	out.Attr = pbAttrToFuse(sa.Attr)
	out.SetTimeout(time.Second)
	return 0
}

func (n *remoteNode) Statfs(ctx context.Context, out *fuse.StatfsOut) syscall.Errno {
	resp, err := n.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Statfs{Statfs: &pb.StatfsRequest{Path: n.path}},
	})
	if err != nil {
		return syscall.EIO
	}
	if resp.Errno != 0 {
		return errnoFromU32(resp.Errno)
	}
	st := resp.GetStatfs()
	if st == nil {
		return syscall.EIO
	}
	out.Blocks = st.Blocks
	out.Bfree = st.Bfree
	out.Bavail = st.Bavail
	out.Files = st.Files
	out.Ffree = st.Ffree
	out.Bsize = uint32(st.Bsize)
	out.NameLen = uint32(st.Namelen)
	return 0
}

// remoteFileHandle implements fs.FileHandle for open files.
// It optionally implements fs.FilePassthroughFder for FUSE passthrough.
type remoteFileHandle struct {
	path          string
	handleID      uint64
	client        *fileproto.Client
	cachedClient  *cache.CachedClient
	fileCache     *cache.FileCache // file-backed cache for passthrough
	cachedFile    *cache.CachedFile // the cached file, if passthrough is enabled
	passthroughFd int               // file descriptor for passthrough (>0 if enabled)
}

// do sends a request through the cached client if available, otherwise directly.
func (f *remoteFileHandle) do(ctx context.Context, req *pb.FileRequest) (*pb.FileResponse, error) {
	if f.cachedClient != nil {
		return f.cachedClient.Do(ctx, req)
	}
	return f.client.Do(ctx, req)
}

var _ fs.FileReader = (*remoteFileHandle)(nil)
var _ fs.FileWriter = (*remoteFileHandle)(nil)
var _ fs.FileReleaser = (*remoteFileHandle)(nil)
var _ fs.FileFlusher = (*remoteFileHandle)(nil)
var _ fs.FileFsyncer = (*remoteFileHandle)(nil)
var _ fs.FilePassthroughFder = (*remoteFileHandle)(nil)

// PassthroughFd implements fs.FilePassthroughFder for FUSE passthrough.
// When passthrough is enabled, the kernel reads directly from the cached file,
// bypassing the FUSE daemon for significantly improved performance.
func (f *remoteFileHandle) PassthroughFd() (int, bool) {
	if f.passthroughFd > 0 && f.cachedFile != nil && f.cachedFile.IsComplete() {
		return f.passthroughFd, true
	}
	return 0, false
}

func (f *remoteFileHandle) Read(ctx context.Context, dest []byte, off int64) (fuse.ReadResult, syscall.Errno) {
	resp, err := f.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
			Path: f.path, HandleId: f.handleID, Offset: off, Size: uint32(len(dest)),
		}},
	})
	if err != nil {
		return nil, syscall.EIO
	}
	if resp.Errno != 0 {
		return nil, errnoFromU32(resp.Errno)
	}
	rr := resp.GetRead()
	if rr == nil {
		return nil, syscall.EIO
	}
	return fuse.ReadResultData(rr.Data), 0
}

func (f *remoteFileHandle) Write(ctx context.Context, data []byte, off int64) (uint32, syscall.Errno) {
	resp, err := f.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Write{Write: &pb.WriteRequest{
			Path: f.path, HandleId: f.handleID, Offset: off, Data: data,
		}},
	})
	if err != nil {
		return 0, syscall.EIO
	}
	if resp.Errno != 0 {
		return 0, errnoFromU32(resp.Errno)
	}
	wr := resp.GetWrite()
	if wr == nil {
		return 0, syscall.EIO
	}
	return wr.Written, 0
}

func (f *remoteFileHandle) Release(ctx context.Context) syscall.Errno {
	_, _ = f.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Release{Release: &pb.ReleaseRequest{Path: f.path, HandleId: f.handleID}},
	})
	// Release the cached file reference
	if f.fileCache != nil && f.cachedFile != nil {
		f.fileCache.Release(f.path)
	}
	return 0
}

func (f *remoteFileHandle) Flush(ctx context.Context) syscall.Errno {
	_, _ = f.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Flush{Flush: &pb.FlushRequest{Path: f.path, HandleId: f.handleID}},
	})
	return 0
}

func (f *remoteFileHandle) Fsync(ctx context.Context, flags uint32) syscall.Errno {
	_, err := f.do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Fsync{Fsync: &pb.FsyncRequest{Path: f.path, HandleId: f.handleID, Flags: flags}},
	})
	if err != nil {
		return syscall.EIO
	}
	return 0
}
