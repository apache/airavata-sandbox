// Package export implements the backend that serves file operations over the gRPC protocol.
package export

import (
	"context"
	"io"
	"os"
	"path/filepath"
	"strings"
	"sync"
	"syscall"
	"time"

	pb "github.com/you/remotefs/proto/gen/remotefs"
)

func errToErrno(e error) uint32 {
	if e == nil {
		return 0
	}
	if errno, ok := e.(syscall.Errno); ok {
		return uint32(errno)
	}
	if pe, ok := e.(*os.PathError); ok {
		if errno, ok := pe.Err.(syscall.Errno); ok {
			return uint32(errno)
		}
	}
	return uint32(syscall.EIO)
}

// Backend serves file operations from local paths, resolving virtual paths using export paths.
type Backend struct {
	paths      []*pb.ExportPath // virtual_name -> local_path (we resolve by virtual_name)
	handles    map[uint64]handleEntry
	nextHandle uint64
	inoMap     map[string]uint64 // virtual path -> inode (stable for Lookup/Readdir)
	nextIno    uint64
	mu         sync.Mutex
}

type handleEntry struct {
	path string
	f    *os.File
}

// NewBackend creates a backend for the given export paths.
func NewBackend(paths []*pb.ExportPath) *Backend {
	return &Backend{
		paths:      paths,
		handles:    make(map[uint64]handleEntry),
		nextHandle: 1,
		inoMap:     make(map[string]uint64),
		nextIno:    2, // 1 is root
	}
}

// Resolve virtual path (e.g. "data/foo") to local path. Returns empty string and errno if invalid.
func (b *Backend) Resolve(virtualPath string) (localPath string, errno uint32) {
	virtualPath = filepath.Clean(virtualPath)
	if virtualPath == "." || virtualPath == "/" {
		// Root: list virtual names only; no single local path.
		return "", 0
	}
	parts := strings.Split(strings.TrimPrefix(virtualPath, "/"), "/")
	if len(parts) == 0 || parts[0] == "" {
		return "", 0
	}
	first := parts[0]
	for _, p := range b.paths {
		if p.VirtualName == first {
			localBase := filepath.Clean(p.LocalPath)
			if len(parts) == 1 {
				return localBase, 0
			}
			rest := filepath.Join(parts[1:]...)
			localFull := filepath.Join(localBase, rest)
			// Ensure we don't escape localBase
			absLocal, _ := filepath.Abs(localFull)
			absBase, _ := filepath.Abs(localBase)
			if !strings.HasPrefix(absLocal, absBase+string(filepath.Separator)) && absLocal != absBase {
				return "", uint32(syscall.EACCES)
			}
			return localFull, 0
		}
	}
	return "", uint32(syscall.ENOENT)
}

// inoForPath returns a stable inode number for the given virtual path (e.g. "" for root, "data", "data/hello.txt").
func (b *Backend) inoForPath(virtualPath string) uint64 {
	b.mu.Lock()
	defer b.mu.Unlock()
	if virtualPath == "" || virtualPath == "." || virtualPath == "/" {
		return 1
	}
	if ino, ok := b.inoMap[virtualPath]; ok {
		return ino
	}
	ino := b.nextIno
	b.nextIno++
	b.inoMap[virtualPath] = ino
	return ino
}

// RootNames returns the virtual names at root (for ReadDir of root).
func (b *Backend) RootNames() []string {
	names := make([]string, 0, len(b.paths))
	for _, p := range b.paths {
		names = append(names, p.VirtualName)
	}
	return names
}

// HandleRequest handles a single FileRequest and returns the response.
func (b *Backend) HandleRequest(ctx context.Context, req *pb.FileRequest) *pb.FileResponse {
	resp := &pb.FileResponse{RequestId: req.RequestId}
	switch op := req.Op.(type) {
	case *pb.FileRequest_GetAttr:
		b.handleGetAttr(op.GetAttr, resp)
	case *pb.FileRequest_Lookup:
		b.handleLookup(op.Lookup, resp)
	case *pb.FileRequest_Open:
		b.handleOpen(op.Open, resp)
	case *pb.FileRequest_Read:
		b.handleRead(op.Read, resp)
	case *pb.FileRequest_Write:
		b.handleWrite(op.Write, resp)
	case *pb.FileRequest_Release:
		b.handleRelease(op.Release, resp)
	case *pb.FileRequest_Opendir:
		b.handleOpendir(op.Opendir, resp)
	case *pb.FileRequest_Readdir:
		b.handleReaddir(op.Readdir, resp)
	case *pb.FileRequest_Releasedir:
		b.handleReleasedir(op.Releasedir, resp)
	case *pb.FileRequest_Create:
		b.handleCreate(op.Create, resp)
	case *pb.FileRequest_Mkdir:
		b.handleMkdir(op.Mkdir, resp)
	case *pb.FileRequest_Unlink:
		b.handleUnlink(op.Unlink, resp)
	case *pb.FileRequest_Rmdir:
		b.handleRmdir(op.Rmdir, resp)
	case *pb.FileRequest_Rename:
		b.handleRename(op.Rename, resp)
	case *pb.FileRequest_Symlink:
		b.handleSymlink(op.Symlink, resp)
	case *pb.FileRequest_Readlink:
		b.handleReadlink(op.Readlink, resp)
	case *pb.FileRequest_SetAttr:
		b.handleSetAttr(op.SetAttr, resp)
	case *pb.FileRequest_Statfs:
		b.handleStatfs(op.Statfs, resp)
	case *pb.FileRequest_Flush:
		b.handleFlush(op.Flush, resp)
	case *pb.FileRequest_Fsync:
		b.handleFsync(op.Fsync, resp)
	default:
		resp.Errno = uint32(syscall.ENOSYS)
	}
	return resp
}

func statToAttr(info os.FileInfo, ino uint64) *pb.Attr {
	mode := uint32(info.Mode())
	if info.IsDir() {
		mode = syscall.S_IFDIR | (mode & 07777)
	} else if info.Mode()&os.ModeSymlink != 0 {
		mode = syscall.S_IFLNK | (mode & 07777)
	} else {
		mode = syscall.S_IFREG | (mode & 07777)
	}
	attr := &pb.Attr{
		Ino:     ino,
		Size:    uint64(info.Size()),
		Mode:    mode,
		Blksize: 4096,
		Blocks:  (uint64(info.Size()) + 4095) / 4096,
	}
	mt := info.ModTime().Unix()
	attr.Atime = uint64(mt)
	attr.Mtime = uint64(mt)
	attr.Ctime = uint64(mt)
	if stat, ok := info.Sys().(*syscall.Stat_t); ok {
		attr.Uid = uint32(stat.Uid)
		attr.Gid = uint32(stat.Gid)
	}
	return attr
}

func (b *Backend) handleGetAttr(r *pb.GetAttrRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path)
	if errno != 0 {
		if r.Path == "" || r.Path == "/" || r.Path == "." {
			// Root: return synthetic dir attr
			resp.Result = &pb.FileResponse_GetAttr{GetAttr: &pb.GetAttrResult{
				Attr: &pb.Attr{Mode: uint32(syscall.S_IFDIR) | 0755, Ino: 1},
			}}
			return
		}
		resp.Errno = errno
		return
	}
	if local == "" {
		// Root: return synthetic dir attr (do not os.Stat(""))
		resp.Result = &pb.FileResponse_GetAttr{GetAttr: &pb.GetAttrResult{
			Attr: &pb.Attr{Mode: uint32(syscall.S_IFDIR) | 0755, Ino: 1},
		}}
		return
	}
	info, err := os.Lstat(local)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	ino := b.inoForPath(r.Path)
	resp.Result = &pb.FileResponse_GetAttr{GetAttr: &pb.GetAttrResult{
		Attr: statToAttr(info, ino),
	}}
}

func (b *Backend) handleLookup(r *pb.LookupRequest, resp *pb.FileResponse) {
	parentPath := r.Path
	childVirtualPath := r.Name
	if parentPath != "" && parentPath != "/" {
		childVirtualPath = parentPath + "/" + r.Name
	}
	if parentPath == "" || parentPath == "/" {
		// Lookup at root: find virtual name
		for _, p := range b.paths {
			if p.VirtualName == r.Name {
				info, err := os.Lstat(p.LocalPath)
				if err != nil {
					resp.Errno = errToErrno(err)
					return
				}
				ino := b.inoForPath(childVirtualPath)
				resp.Result = &pb.FileResponse_Lookup{Lookup: &pb.LookupResult{
					Attr: statToAttr(info, ino),
				}}
				return
			}
		}
		resp.Errno = uint32(syscall.ENOENT)
		return
	}
	local, errno := b.Resolve(parentPath + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	info, err := os.Lstat(local)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	ino := b.inoForPath(childVirtualPath)
	resp.Result = &pb.FileResponse_Lookup{Lookup: &pb.LookupResult{
		Attr: statToAttr(info, ino),
	}}
}

func (b *Backend) handleOpen(r *pb.OpenRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	flags := int(r.Flags)
	f, err := os.OpenFile(local, flags, 0)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	b.mu.Lock()
	id := b.nextHandle
	b.nextHandle++
	b.handles[id] = handleEntry{path: r.Path, f: f}
	b.mu.Unlock()
	resp.Result = &pb.FileResponse_Open{Open: &pb.OpenResult{HandleId: id}}
}

func (b *Backend) handleRead(r *pb.ReadRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	b.mu.Unlock()
	if !ok {
		resp.Errno = uint32(syscall.EBADF)
		return
	}
	buf := make([]byte, r.Size)
	n, err := ent.f.ReadAt(buf, r.Offset)
	if err != nil && err != io.EOF {
		resp.Errno = errToErrno(err)
		return
	}
	resp.Result = &pb.FileResponse_Read{Read: &pb.ReadResult{Data: buf[:n]}}
}

func (b *Backend) handleWrite(r *pb.WriteRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	b.mu.Unlock()
	if !ok {
		resp.Errno = uint32(syscall.EBADF)
		return
	}
	n, err := ent.f.WriteAt(r.Data, r.Offset)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	resp.Result = &pb.FileResponse_Write{Write: &pb.WriteResult{Written: uint32(n)}}
}

func (b *Backend) handleRelease(r *pb.ReleaseRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	if ok {
		delete(b.handles, r.HandleId)
	}
	b.mu.Unlock()
	if ok {
		_ = ent.f.Close()
	}
}

func (b *Backend) handleOpendir(r *pb.OpendirRequest, resp *pb.FileResponse) {
	var local string
	var errno uint32
	if r.Path == "" || r.Path == "/" {
		// Root: we don't open a real dir; use a synthetic handle that Readdir will use
		local = ""
	} else {
		local, errno = b.Resolve(r.Path)
		if errno != 0 {
			resp.Errno = errno
			return
		}
	}
	var f *os.File
	if local != "" {
		var err error
		f, err = os.Open(local)
		if err != nil {
			resp.Errno = errToErrno(err)
			return
		}
	}
	b.mu.Lock()
	id := b.nextHandle
	b.nextHandle++
	b.handles[id] = handleEntry{path: r.Path, f: f}
	b.mu.Unlock()
	resp.Result = &pb.FileResponse_Open{Open: &pb.OpenResult{HandleId: id}}
}

func (b *Backend) handleReaddir(r *pb.ReaddirRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	b.mu.Unlock()
	if !ok {
		resp.Errno = uint32(syscall.EBADF)
		return
	}
	var entries []*pb.DirEntry
	if ent.f == nil {
		// Root handle: virtual names are top-level entries (dirs or files)
		for _, p := range b.paths {
			info, err := os.Stat(p.LocalPath)
			if err != nil {
				continue
			}
			var mode uint32
			if info.IsDir() {
				mode = syscall.S_IFDIR | 0755
			} else {
				mode = syscall.S_IFREG | (uint32(info.Mode()) & 07777)
			}
			ino := b.inoForPath(p.VirtualName)
			entries = append(entries, &pb.DirEntry{Name: p.VirtualName, Mode: mode, Ino: ino})
		}
	} else {
		names, err := ent.f.Readdirnames(-1)
		if err != nil {
			resp.Errno = errToErrno(err)
			return
		}
		dirPath := ent.f.Name() // path of the directory we opened (e.g. /export/data)
		for _, name := range names {
			info, err := os.Lstat(filepath.Join(dirPath, name))
			if err != nil {
				continue
			}
			mode := uint32(info.Mode())
			if info.IsDir() {
				mode = syscall.S_IFDIR | (mode & 07777)
			} else if info.Mode()&os.ModeSymlink != 0 {
				mode = syscall.S_IFLNK | (mode & 07777)
			} else {
				mode = syscall.S_IFREG | (mode & 07777)
			}
			childPath := ent.path + "/" + name
			if ent.path == "" {
				childPath = name
			}
			ino := b.inoForPath(childPath)
			entries = append(entries, &pb.DirEntry{Name: name, Mode: mode, Ino: ino})
		}
	}
	resp.Result = &pb.FileResponse_Readdir{Readdir: &pb.ReaddirResult{Entries: entries}}
}

func (b *Backend) handleReleasedir(r *pb.ReleasedirRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	if ok {
		delete(b.handles, r.HandleId)
	}
	b.mu.Unlock()
	if ok && ent.f != nil {
		_ = ent.f.Close()
	}
}

func (b *Backend) handleCreate(r *pb.CreateRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	flags := int(r.Flags)
	mode := os.FileMode(r.Mode) & 07777
	f, err := os.OpenFile(local, flags|os.O_CREATE|os.O_TRUNC, mode)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	info, _ := f.Stat()
	b.mu.Lock()
	id := b.nextHandle
	b.nextHandle++
	b.handles[id] = handleEntry{path: r.Path + "/" + r.Name, f: f}
	b.mu.Unlock()
	childPath := r.Path + "/" + r.Name
	if r.Path == "" {
		childPath = r.Name
	}
	ino := b.inoForPath(childPath)
	resp.Result = &pb.FileResponse_Create{Create: &pb.CreateResult{
		HandleId: id,
		Attr:     statToAttr(info, ino),
	}}
}

func (b *Backend) handleMkdir(r *pb.MkdirRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if err := os.Mkdir(local, os.FileMode(r.Mode)&07777); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	info, _ := os.Stat(local)
	childPath := r.Path + "/" + r.Name
	if r.Path == "" {
		childPath = r.Name
	}
	ino := b.inoForPath(childPath)
	resp.Result = &pb.FileResponse_Mkdir{Mkdir: &pb.MkdirResult{Attr: statToAttr(info, ino)}}
}

func (b *Backend) handleUnlink(r *pb.UnlinkRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if err := os.Remove(local); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
}

func (b *Backend) handleRmdir(r *pb.RmdirRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if err := os.Remove(local); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
}

func (b *Backend) handleRename(r *pb.RenameRequest, resp *pb.FileResponse) {
	oldLocal, errno := b.Resolve(r.Path + "/" + r.OldName)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	newLocal, errno := b.Resolve(r.NewPath + "/" + r.NewName)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if err := os.Rename(oldLocal, newLocal); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
}

func (b *Backend) handleSymlink(r *pb.SymlinkRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path + "/" + r.Name)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if err := os.Symlink(r.Target, local); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	info, _ := os.Lstat(local)
	childPath := r.Path + "/" + r.Name
	if r.Path == "" {
		childPath = r.Name
	}
	ino := b.inoForPath(childPath)
	resp.Result = &pb.FileResponse_Symlink{Symlink: &pb.SymlinkResult{Attr: statToAttr(info, ino)}}
}

func (b *Backend) handleReadlink(r *pb.ReadlinkRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	target, err := os.Readlink(local)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	resp.Result = &pb.FileResponse_Readlink{Readlink: &pb.ReadlinkResult{Target: target}}
}

func (b *Backend) handleSetAttr(r *pb.SetAttrRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path)
	if errno != 0 {
		resp.Errno = errno
		return
	}
	if r.Size != nil {
		if err := os.Truncate(local, int64(*r.Size)); err != nil {
			resp.Errno = errToErrno(err)
			return
		}
	}
	if r.Mtime != nil {
		// best-effort
		_ = os.Chtimes(local, time.Unix(int64(*r.Mtime), 0), time.Unix(int64(*r.Mtime), 0))
	}
	info, err := os.Stat(local)
	if err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	ino := b.inoForPath(r.Path)
	resp.Result = &pb.FileResponse_SetAttr{SetAttr: &pb.SetAttrResult{Attr: statToAttr(info, ino)}}
}

func (b *Backend) handleStatfs(r *pb.StatfsRequest, resp *pb.FileResponse) {
	local, errno := b.Resolve(r.Path)
	if errno != 0 {
		if r.Path == "" || r.Path == "/" {
			local = b.paths[0].LocalPath
		} else {
			resp.Errno = errno
			return
		}
	}
	var stat syscall.Statfs_t
	if err := syscall.Statfs(local, &stat); err != nil {
		resp.Errno = errToErrno(err)
		return
	}
	resp.Result = &pb.FileResponse_Statfs{Statfs: &pb.StatfsResult{
		Blocks:  stat.Blocks,
		Bfree:   stat.Bfree,
		Bavail:  stat.Bavail,
		Files:   stat.Files,
		Ffree:   stat.Ffree,
		Bsize:   uint64(stat.Bsize),
		Namelen: 255,
	}}
}

func (b *Backend) handleFlush(r *pb.FlushRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	b.mu.Unlock()
	if ok && ent.f != nil {
		_ = ent.f.Sync()
	}
}

func (b *Backend) handleFsync(r *pb.FsyncRequest, resp *pb.FileResponse) {
	b.mu.Lock()
	ent, ok := b.handles[r.HandleId]
	b.mu.Unlock()
	if !ok {
		resp.Errno = uint32(syscall.EBADF)
		return
	}
	if ent.f != nil {
		if err := ent.f.Sync(); err != nil {
			resp.Errno = errToErrno(err)
			return
		}
	}
}
