package org.apache.airavata.services.registry.rest.security.basic;

import org.apache.airavata.security.configurations.AuthenticatorConfigurationReader;
import org.apache.airavata.services.registry.rest.security.AbstractAuthenticatorTest;
import org.apache.airavata.services.registry.rest.security.MyHttpServletRequest;
import org.apache.airavata.services.registry.rest.security.session.SessionAuthenticator;
import org.apache.commons.codec.binary.Base64;

/**
 * Test class for basic access authenticator.
 */
public class BasicAccessAuthenticatorTest extends AbstractAuthenticatorTest {

    private SessionAuthenticator sessionAuthenticator;

    private AuthenticatorConfigurationReader authenticatorConfigurationReader;

    public BasicAccessAuthenticatorTest() throws Exception {
        super("basicAccessAuthenticator");
    }


    @Override
    public void testAuthenticateSuccess() throws Exception {

        assertTrue(authenticator.authenticate(getRequest("amilaj:secret")));
    }

    @Override
     public void testAuthenticateFail() throws Exception {
        assertFalse(authenticator.authenticate(getRequest("amilaj:secret1")));
    }

    public void testAuthenticateFailUserName() throws Exception {
        assertFalse(authenticator.authenticate(getRequest("amila:secret1")));
    }

    @Override
    public void testCanProcess() throws Exception {

        assertTrue(authenticator.canProcess(getRequest("amilaj:secret")));
    }

    private MyHttpServletRequest getRequest(String userPassword) {
        MyHttpServletRequest myHttpServletRequest = new MyHttpServletRequest();

        String authHeader = "Basic " + new String(Base64.encodeBase64(userPassword.getBytes()));

        myHttpServletRequest.addHeader("Authorization", authHeader);

        return myHttpServletRequest;

    }

    public void tearDown() throws Exception {

    }

    /*public void testConfigure() throws Exception {

        BasicAccessAuthenticator basicAccessAuthenticator = (BasicAccessAuthenticator)authenticator;

        assertEquals("AIRAVATA_USER", basicAccessAuthenticator.getUserTable());
        assertEquals("USERID", basicAccessAuthenticator.getUserNameColumn());
        assertEquals("PASSWORD", basicAccessAuthenticator.getPasswordColumn());
    }*/

}
