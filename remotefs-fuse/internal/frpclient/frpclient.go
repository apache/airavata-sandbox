package frpclient

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"net"
	"os"
	"strconv"
	"strings"
	"time"

	frpclient "github.com/fatedier/frp/client"
	v1 "github.com/fatedier/frp/pkg/config/v1"
)

const (
	DefaultFRPServer = "149.165.172.97"
	DefaultFRPPort   = 17000
	DefaultFRPToken  = "mysecret"

	EnvFRPServer = "REMOTEFS_FRP_SERVER"
	EnvFRPToken  = "REMOTEFS_FRP_TOKEN"
)

// FRPServerAndToken returns server address and token from env or defaults.
// Server can be "host" or "host:port".
func FRPServerAndToken(serverFlag, tokenFlag string) (server, token string) {
	token = tokenFlag
	if token == "" {
		token = os.Getenv(EnvFRPToken)
	}
	if token == "" {
		token = DefaultFRPToken
	}
	server = serverFlag
	if server == "" {
		server = os.Getenv(EnvFRPServer)
	}
	if server == "" {
		server = net.JoinHostPort(DefaultFRPServer, strconv.Itoa(DefaultFRPPort))
	} else if !strings.Contains(server, ":") {
		server = net.JoinHostPort(server, strconv.Itoa(DefaultFRPPort))
	}
	return server, token
}

// GenerateIDSecret returns a short alphanumeric id and a hex secret (e.g. for one-time share).
func GenerateIDSecret() (id, secret string, err error) {
	b := make([]byte, 8)
	if _, e := rand.Read(b); e != nil {
		return "", "", e
	}
	id = hex.EncodeToString(b)[:12] // 12 hex chars
	b2 := make([]byte, 16)
	if _, e := rand.Read(b2); e != nil {
		return "", "", e
	}
	secret = hex.EncodeToString(b2)
	return id, secret, nil
}

// CheckFRPServerReachable tries to open a TCP connection to the FRP server.
// Use this before starting FRP to fail fast with a clear error if the server is unreachable
// (e.g. connection refused, timeout). The failure is then clearly a connectivity/server
// issue, not a bug in remotefs.
func CheckFRPServerReachable(serverAddr string, timeout time.Duration) error {
	if timeout <= 0 {
		timeout = 5 * time.Second
	}
	conn, err := net.DialTimeout("tcp", serverAddr, timeout)
	if err != nil {
		return fmt.Errorf("cannot reach FRP server at %s: %w (is frps running and is the port open?)", serverAddr, err)
	}
	conn.Close()
	return nil
}

// CommonConfig builds ClientCommonConfig from server address and token.
// serverAddr can be "host" or "host:port"; if port is missing, DefaultFRPPort is used.
func CommonConfig(serverAddr, token string) (*v1.ClientCommonConfig, error) {
	host, portStr, err := net.SplitHostPort(serverAddr)
	if err != nil {
		if strings.Contains(serverAddr, ":") {
			return nil, fmt.Errorf("invalid frp server address %q: %w", serverAddr, err)
		}
		host = serverAddr
		portStr = strconv.Itoa(DefaultFRPPort)
	}
	port, err := strconv.Atoi(portStr)
	if err != nil {
		return nil, fmt.Errorf("invalid frp server port %q: %w", portStr, err)
	}
	cfg := &v1.ClientCommonConfig{
		ServerAddr: host,
		ServerPort: port,
		Auth: v1.AuthClientConfig{
			Method: v1.AuthMethodToken,
			Token:  token,
		},
	}
	if err := cfg.Complete(); err != nil {
		return nil, err
	}
	return cfg, nil
}

// RunPublishProxies registers XTCP and STCP proxies with the FRP server and runs the
// client service in the background. The returned cancel function stops the service.
// localPort is the local gRPC server port. id and secret identify the session.
func RunPublishProxies(ctx context.Context, common *v1.ClientCommonConfig, id, secret string, localPort int) (cancel func(), err error) {
	xtcp := &v1.XTCPProxyConfig{
		ProxyBaseConfig: v1.ProxyBaseConfig{
			Name: id,
			Type: string(v1.ProxyTypeXTCP),
			ProxyBackend: v1.ProxyBackend{
				LocalIP:   "127.0.0.1",
				LocalPort: localPort,
			},
		},
		Secretkey: secret,
	}
	xtcp.Complete("")

	stcp := &v1.STCPProxyConfig{
		ProxyBaseConfig: v1.ProxyBaseConfig{
			Name: id + "-stcp",
			Type: string(v1.ProxyTypeSTCP),
			ProxyBackend: v1.ProxyBackend{
				LocalIP:   "127.0.0.1",
				LocalPort: localPort,
			},
		},
		Secretkey: secret,
	}
	stcp.Complete("")

	svc, err := frpclient.NewService(frpclient.ServiceOptions{
		Common:    common,
		ProxyCfgs: []v1.ProxyConfigurer{xtcp, stcp},
	})
	if err != nil {
		return nil, fmt.Errorf("frp service: %w", err)
	}

	runCtx, runCancel := context.WithCancel(ctx)
	go func() {
		_ = svc.Run(runCtx)
	}()

	return func() {
		runCancel()
		svc.Close()
	}, nil
}

// RunMountVisitors starts XTCP visitor with STCP fallback and returns the local address
// (e.g. "127.0.0.1:12345") to dial for gRPC. The returned cancel function stops the service.
func RunMountVisitors(ctx context.Context, common *v1.ClientCommonConfig, id, secret string) (localAddr string, cancel func(), err error) {
	// Reserve a port so we know what to dial
	l, err := net.Listen("tcp", "127.0.0.1:0")
	if err != nil {
		return "", nil, fmt.Errorf("listen for visitor port: %w", err)
	}
	port := l.Addr().(*net.TCPAddr).Port
	l.Close()

	fallbackName := id + "-fb"
	xtcpVisitor := &v1.XTCPVisitorConfig{
		VisitorBaseConfig: v1.VisitorBaseConfig{
			Name:       id + "-xtcp",
			Type:       string(v1.VisitorTypeXTCP),
			SecretKey:  secret,
			ServerName: id,
			BindAddr:   "127.0.0.1",
			BindPort:   port,
		},
		FallbackTo:        fallbackName,
		FallbackTimeoutMs:  5000,
	}
	xtcpVisitor.Complete(common)

	stcpVisitor := &v1.STCPVisitorConfig{
		VisitorBaseConfig: v1.VisitorBaseConfig{
			Name:       fallbackName,
			Type:       string(v1.VisitorTypeSTCP),
			SecretKey:  secret,
			ServerName: id + "-stcp",
			BindAddr:   "127.0.0.1",
			BindPort:   -1, // no physical port; fallback only
		},
	}
	stcpVisitor.Complete(common)

	svc, err := frpclient.NewService(frpclient.ServiceOptions{
		Common:      common,
		VisitorCfgs: []v1.VisitorConfigurer{xtcpVisitor, stcpVisitor},
	})
	if err != nil {
		return "", nil, fmt.Errorf("frp service: %w", err)
	}

	runCtx, runCancel := context.WithCancel(ctx)
	go func() {
		_ = svc.Run(runCtx)
	}()

	localAddr = net.JoinHostPort("127.0.0.1", strconv.Itoa(port))
	return localAddr, func() {
		runCancel()
		svc.Close()
	}, nil
}
