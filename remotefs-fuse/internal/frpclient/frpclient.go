// Package frpclient provides FRP (Fast Reverse Proxy) client helpers for registering proxies (publish) and visitors (mount).
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

	EnvFRP = "REMOTEFS_FRP"
)

// ParseFRPConnection parses "hostname:port:password" into server (host:port) and token. Splits from the right for IPv6.
func ParseFRPConnection(s string) (serverAddr, token string, err error) {
	s = strings.TrimSpace(s)
	if s == "" {
		return "", "", fmt.Errorf("FRP connection string is empty")
	}
	idx := strings.LastIndex(s, ":")
	if idx <= 0 || idx == len(s)-1 {
		return "", "", fmt.Errorf("FRP connection must be hostname:port:password")
	}
	serverAddr = s[:idx]
	token = s[idx+1:]
	if _, _, parseErr := net.SplitHostPort(serverAddr); parseErr != nil {
		return "", "", fmt.Errorf("invalid host:port in FRP connection: %w", parseErr)
	}
	return serverAddr, token, nil
}

// FRPConnection returns server address and token from connectionFlag, or REMOTEFS_FRP env, or default.
func FRPConnection(connectionFlag string) (serverAddr, token string, err error) {
	s := connectionFlag
	if s == "" {
		s = os.Getenv(EnvFRP)
	}
	if s == "" {
		serverAddr = net.JoinHostPort(DefaultFRPServer, strconv.Itoa(DefaultFRPPort))
		return serverAddr, DefaultFRPToken, nil
	}
	return ParseFRPConnection(s)
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

// CheckFRPServerReachable verifies the FRP server is reachable (TCP dial). Fails fast with a clear error if not.
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

// CommonConfig builds FRP client config from server address and token.
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

// RunPublishProxies registers XTCP and STCP proxies with the FRP server and runs the client in the background.
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

// RunMountVisitors starts XTCP visitor with STCP fallback and returns the local address to dial for gRPC.
func RunMountVisitors(ctx context.Context, common *v1.ClientCommonConfig, id, secret string) (localAddr string, cancel func(), err error) {
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
			BindPort:   -1,
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
	deadline := time.Now().Add(60 * time.Second)
	const dialTimeout = 2 * time.Second
	const retryInterval = 300 * time.Millisecond
	for time.Now().Before(deadline) {
		conn, err := net.DialTimeout("tcp", localAddr, dialTimeout)
		if err == nil {
			conn.Close()
			return localAddr, func() {
				runCancel()
				svc.Close()
			}, nil
		}
		time.Sleep(retryInterval)
	}
	runCancel()
	svc.Close()
	return "", nil, fmt.Errorf("FRP visitor did not become ready at %s within 60s", localAddr)
}
