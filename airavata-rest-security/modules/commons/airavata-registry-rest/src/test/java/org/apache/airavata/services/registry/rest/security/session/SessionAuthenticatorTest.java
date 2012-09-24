package org.apache.airavata.services.registry.rest.security.session;

import org.apache.airavata.services.registry.rest.security.AbstractAuthenticatorTest;
import org.apache.airavata.services.registry.rest.security.MyHttpServletRequest;

/**
 * Session authenticator test.
 */
public class SessionAuthenticatorTest extends AbstractAuthenticatorTest {

    public SessionAuthenticatorTest() throws Exception {
        super("sessionAuthenticator");
    }

    public void testAuthenticateSuccess() throws Exception {

        MyHttpServletRequest servletRequestRequest = new MyHttpServletRequest();
        servletRequestRequest.addHeader("sessionTicket", "1234");

        assertTrue(authenticator.authenticate(servletRequestRequest));

    }

    public void testAuthenticateFail() throws Exception {

        MyHttpServletRequest servletRequestRequest = new MyHttpServletRequest();
        servletRequestRequest.addHeader("sessionTicket", "12345");

        assertFalse(authenticator.authenticate(servletRequestRequest));

    }

    public void testCanProcess() throws Exception {

        MyHttpServletRequest servletRequestRequest = new MyHttpServletRequest();
        servletRequestRequest.addHeader("sessionTicket", "12345");

        assertTrue(authenticator.canProcess(servletRequestRequest));

    }
}
