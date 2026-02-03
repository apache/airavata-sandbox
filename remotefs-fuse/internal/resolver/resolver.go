// Package resolver resolves host:port to an IP for gRPC dialing (avoids resolver issues in some environments).
package resolver

import (
	"net"
)

// ResolveServer resolves host:port to ip:port so that gRPC's dialer receives
// an IP address. This avoids "name resolver error: produced zero addresses"
// when the default resolver fails (e.g. in some Docker setups).
func ResolveServer(addr string) (string, error) {
	host, port, err := net.SplitHostPort(addr)
	if err != nil {
		return "", err
	}
	if net.ParseIP(host) != nil {
		return addr, nil
	}
	ips, err := net.LookupIP(host)
	if err != nil {
		return addr, nil
	}
	for _, ip := range ips {
		if ip4 := ip.To4(); ip4 != nil {
			return net.JoinHostPort(ip4.String(), port), nil
		}
	}
	for _, ip := range ips {
		return net.JoinHostPort(ip.String(), port), nil
	}
	return addr, nil
}
