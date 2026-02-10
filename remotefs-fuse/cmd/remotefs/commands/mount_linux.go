//go:build linux

package commands

import (
	"context"
	"fmt"
	"log"
	"os"
	"os/exec"
	"os/signal"
	"strings"
	"syscall"
	"time"

	"github.com/hanwen/go-fuse/v2/fs"
	"github.com/hanwen/go-fuse/v2/fuse"
	"github.com/spf13/cobra"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/encoding/gzip"

	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/cache"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/fileproto"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/frpclient"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/mount"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/resolver"
	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

var (
	mountAddr      string
	mountToken     string
	mountFRP       string
	allowOther     bool
	cacheSize      int64
	cacheTTL       int
	cacheBlockSize int64
	noCache        bool
	fileCacheDir   string
	fileCacheSize  int64
	passthrough    bool
	mmapCache      bool
)

func init() {
	mountCmd = &cobra.Command{
		Use:   "mount <mountpoint>",
		Short: "Mount published folder at mountpoint (use --addr for direct gRPC or --token for FRP)",
		RunE:  runMount,
		Args:  cobra.ExactArgs(1),
	}
	mountCmd.Flags().StringVarP(&mountAddr, "addr", "a", "", "gRPC server address (e.g. localhost:50051)")
	mountCmd.Flags().StringVar(&mountToken, "token", "", "Forwarding token from publish --frp (id:secret)")
	mountCmd.Flags().StringVar(&mountFRP, "frp", "", "FRP connection when using --token: hostname:port:password. Env: REMOTEFS_FRP")
	mountCmd.Flags().BoolVar(&allowOther, "allow-other", false, "allow other users to access the mount (requires user_allow_other in /etc/fuse.conf)")

	// Cache configuration flags
	mountCmd.Flags().Int64Var(&cacheSize, "cache-size", 256, "Maximum cache size in MB (default: 256)")
	mountCmd.Flags().IntVar(&cacheTTL, "cache-ttl", 30, "Metadata/directory cache TTL in seconds (default: 30)")
	mountCmd.Flags().Int64Var(&cacheBlockSize, "cache-block-size", 256, "Data cache block size in KB (default: 256)")
	mountCmd.Flags().BoolVar(&noCache, "no-cache", false, "Disable caching entirely")
	
	// File-backed cache and passthrough flags
	mountCmd.Flags().StringVar(&fileCacheDir, "cache-dir", "", "Directory for file-backed cache (enables passthrough support and mmap-cache)")
	mountCmd.Flags().Int64Var(&fileCacheSize, "file-cache-size", 1024, "Maximum file cache size in MB (default: 1024)")
	mountCmd.Flags().BoolVar(&passthrough, "passthrough", false, "Enable FUSE passthrough mode (requires Linux 6.9+ and --cache-dir)")
	mountCmd.Flags().BoolVar(&mmapCache, "mmap-cache", false, "Use file-backed memory-mapped block cache (requires --cache-dir)")
}

func runMount(cmd *cobra.Command, args []string) error {
	mp := args[0]
	if _, err := os.Stat(mp); err != nil {
		return fmt.Errorf("mountpoint %q: %w", mp, err)
	}

	var serverAddr string
	var frpCancel func()
	if mountAddr != "" && mountToken != "" {
		return fmt.Errorf("use either --addr (-a) or --token, not both")
	}
	if mountAddr != "" {
		var err error
		serverAddr, err = resolver.ResolveServer(mountAddr)
		if err != nil {
			return err
		}
	} else if mountToken != "" {
		parts := strings.SplitN(mountToken, ":", 2)
		if len(parts) != 2 || parts[0] == "" || parts[1] == "" {
			return fmt.Errorf("--token must be id:secret (from publish --frp output)")
		}
		id, secret := parts[0], parts[1]
		server, authToken, err := frpclient.FRPConnection(mountFRP)
		if err != nil {
			return fmt.Errorf("frp connection: %w", err)
		}
		if err := frpclient.CheckFRPServerReachable(server, 0); err != nil {
			return err
		}
		common, err := frpclient.CommonConfig(server, authToken)
		if err != nil {
			return fmt.Errorf("frp config: %w", err)
		}
		serverAddr, frpCancel, err = frpclient.RunMountVisitors(context.Background(), common, id, secret)
		if err != nil {
			return fmt.Errorf("frp visitor: %w", err)
		}
		defer frpCancel()
	} else {
		return fmt.Errorf("either --addr (-a) or --token is required")
	}

	conn, err := grpc.NewClient(serverAddr,
		grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithDefaultCallOptions(grpc.UseCompressor(gzip.Name)), // Enable gzip compression
	)
	if err != nil {
		return err
	}
	defer conn.Close()
	client := pb.NewRemotefsCoordinatorClient(conn)
	stream, err := client.ConnectSink(context.Background())
	if err != nil {
		return err
	}
	err = stream.Send(&pb.FileMessage{
		Payload: &pb.FileMessage_ConnectSink{ConnectSink: &pb.ConnectSinkRequest{}},
	})
	if err != nil {
		return err
	}
	fpClient := fileproto.NewClient(stream)
	go fpClient.Run()

	if mmapCache && fileCacheDir == "" {
		return fmt.Errorf("--mmap-cache requires --cache-dir to be set")
	}

	cacheConfig := &cache.Config{
		MaxDataCacheSize:    cacheSize * 1024 * 1024,
		BlockSize:           cacheBlockSize * 1024,
		DataTTL:             5 * time.Minute,
		MetadataTTL:         time.Duration(cacheTTL) * time.Second,
		DirectoryTTL:        time.Duration(cacheTTL) * time.Second,
		Enabled:             !noCache,
		PrefetchBlocks:      4,
		MaxParallelFetches:  8,
		EnablePrefetch:      true,
		EnableParallelFetch: true,
		UseMmapCache:        mmapCache,
		MmapCacheDir:        fileCacheDir,
	}

	cachedClient := cache.NewCachedClient(fpClient, cacheConfig)

	root := &mount.RemoteRoot{Client: fpClient, CachedClient: cachedClient}
	if !noCache {
		if mmapCache {
			log.Printf("Mmap cache enabled: size=%dMB, TTL=%ds, block=%dKB, dir=%s", cacheSize, cacheTTL, cacheBlockSize, fileCacheDir)
		} else {
			log.Printf("In-memory cache enabled: size=%dMB, TTL=%ds, block=%dKB", cacheSize, cacheTTL, cacheBlockSize)
		}
	}
	
	// Set up file-backed cache for passthrough
	if fileCacheDir != "" {
		fileCache, err := cache.NewFileCache(fileCacheDir, fileCacheSize*1024*1024)
		if err != nil {
			return fmt.Errorf("file cache: %w", err)
		}
		root.FileCache = fileCache
		defer fileCache.Close()
		
		if passthrough {
			root.Passthrough = true
			log.Printf("FUSE passthrough enabled: cache-dir=%s, file-cache-size=%dMB", fileCacheDir, fileCacheSize)
		} else {
			log.Printf("File cache enabled (no passthrough): cache-dir=%s, file-cache-size=%dMB", fileCacheDir, fileCacheSize)
		}
	} else if passthrough {
		return fmt.Errorf("--passthrough requires --cache-dir to be set")
	}
	
	sec := time.Second
	opts := &fs.Options{
		AttrTimeout:  &sec,
		EntryTimeout: &sec,
		MountOptions: fuse.MountOptions{AllowOther: allowOther},
	}
	server, err := fs.Mount(mp, root, opts)
	if err != nil {
		return err
	}
	done := make(chan struct{})
	go func() {
		sig := make(chan os.Signal, 1)
		signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
		<-sig
		signal.Stop(sig)
		log.Printf("Unmounting %s...", mp)
		// Close client first so in-flight FUSE ops fail and unmount can complete.
		fpClient.Close()
		_ = conn.Close()
		go func() {
			_ = server.Unmount()
			close(done)
		}()
		unmountTimeout := 10 * time.Second
		select {
		case <-done:
			// Unmount finished
		case <-time.After(unmountTimeout):
			log.Printf("Unmount timed out after %v", unmountTimeout)
		}
		// Always run fusermount -u so the kernel mount is cleared and the directory
		// is not left as d????????? / "Transport endpoint is not connected".
		var forceErr error
		for _, name := range []string{"fusermount", "fusermount3"} {
			forceErr = exec.Command(name, "-u", mp).Run()
			if forceErr == nil {
				break
			}
		}
		if forceErr != nil {
			log.Printf("Unmount failed: %v; run manually: fusermount -u %q", forceErr, mp)
		}
		os.Exit(0)
	}()
	log.Printf("Mounted at %s", mp)
	server.Wait()
	return nil
}
