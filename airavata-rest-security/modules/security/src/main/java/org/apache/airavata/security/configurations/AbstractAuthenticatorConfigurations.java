package org.apache.airavata.security.configurations;

import org.apache.airavata.security.AuthenticatorConfiguration;

/**
 * An abstract implementation of Authenticator configurations.
 * An authenticator configurations will have two types of configurations.
 * <ol>
 *     <li>Configurations common to all authenticators. This includes configurations such as authenticator name,
 *     the priority, whether authenticator is enabled or not.</li>
 *     <li>Configurations which are specific to a particular authenticator.</li>
 * </ol>
 */
public abstract class AbstractAuthenticatorConfigurations implements AuthenticatorConfiguration {

    private String authenticatorName;

    private String authenticatorClassName;

    private boolean isEnabled;

    private int authenticatorPriority;

    public AbstractAuthenticatorConfigurations() {

    }



}
