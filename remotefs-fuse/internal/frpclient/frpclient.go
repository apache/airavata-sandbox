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
// It waits until the NAT tunnel is fully established and data can flow through.
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
		FallbackTo:         fallbackName,
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

	// Wait for the NAT tunnel to be fully established.
	// The local port may accept connections before the NAT hole-punching completes,
	// so we need to verify data can actually flow through the tunnel.
	deadline := time.Now().Add(90 * time.Second) // Extended timeout for NAT hole-punching
	const dialTimeout = 5 * time.Second
	const retryInterval = 1 * time.Second

	var lastErr error
	for time.Now().Before(deadline) {
		// Check if context was cancelled
		select {
		case <-ctx.Done():
			runCancel()
			svc.Close()
			return "", nil, fmt.Errorf("context cancelled while waiting for NAT tunnel: %w", ctx.Err())
		default:
		}

		// Try to establish a connection and verify it works
		conn, err := net.DialTimeout("tcp", localAddr, dialTimeout)
		if err != nil {
			lastErr = err
			time.Sleep(retryInterval)
			continue
		}

		// Connection established - now verify the tunnel is working by
		// setting a deadline and trying to read/write. If NAT isn't ready,
		// this will timeout or error.
		conn.SetDeadline(time.Now().Add(10 * time.Second))

		// Send HTTP/2 preface to trigger gRPC server response.
		// If the tunnel isn't ready, this will fail with a timeout or connection reset.
		preface := []byte("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n")
		_, writeErr := conn.Write(preface)
		if writeErr != nil {
			conn.Close()
			lastErr = fmt.Errorf("tunnel write test failed: %w", writeErr)
			time.Sleep(retryInterval)
			continue
		}

		// Try to read something back - gRPC server should respond
		buf := make([]byte, 1024)
		conn.SetReadDeadline(time.Now().Add(5 * time.Second))
		n, readErr := conn.Read(buf)
		conn.Close()

		if readErr != nil {
			// Check if it's a timeout vs connection error
			if netErr, ok := readErr.(net.Error); ok && netErr.Timeout() {
				lastErr = fmt.Errorf("tunnel read timeout - NAT may not be established yet")
				time.Sleep(retryInterval)
				continue
			}
			// Connection reset or other error - NAT likely not ready
			lastErr = fmt.Errorf("tunnel read failed: %w", readErr)
			time.Sleep(retryInterval)
			continue
		}

		// We got a response! Tunnel is working.
		if n > 0 {
			return localAddr, func() {
				runCancel()
				svc.Close()
			}, nil
		}

		time.Sleep(retryInterval)
	}

	runCancel()
	svc.Close()
	if lastErr != nil {
		return "", nil, fmt.Errorf("FRP NAT tunnel not established at %s within 90s: %v", localAddr, lastErr)
	}
	return "", nil, fmt.Errorf("FRP NAT tunnel not established at %s within 90s", localAddr)
}
