package org.apache.airavata.security;

/**
 * An interface class define authenticator configurations. Based on the mechanism we need
 * to implement this interface to get the proper configuration.
 */
public interface AuthenticatorConfiguration {

    /**
     * Load configurations from a file or some other place.
     */
    void load ();

    /**
     * Serialize configurations.
     */
    void serialize();
}
