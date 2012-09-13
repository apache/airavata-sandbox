package org.apache.airavata.services.registry.rest.security.basic;

import org.apache.airavata.security.AbstractDatabaseAuthenticator;
import org.apache.airavata.security.AuthenticationException;
import org.apache.airavata.services.registry.rest.security.session.DBLookup;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * This authenticator handles basic access authentication requests. In basic access authentication
 * we get user name and password as HTTP headers. The password is encoded with base64.
 * More information @link{http://en.wikipedia.org/wiki/Basic_access_authentication}
 */
public class BasicAccessAuthenticator extends AbstractDatabaseAuthenticator {

    private DBLookup dbLookup;

    private String userTable;
    private String userNameColumn;
    private String passwordColumn;

    private static final String AUTHENTICATOR_NAME = "BasicAccessAuthenticator";

    /**
     * Header names
     */
    private static final String AUTHORISATION_HEADER_NAME = "Authorization";
    private static final String USER_IN_SESSION = "userName";

    public BasicAccessAuthenticator() {
        super(AUTHENTICATOR_NAME);
    }

    private String decode(String encoded) {
        return new String(Base64.decodeBase64(encoded.getBytes()));
    }

    /**
     * Returns user name and password as an array. The first element is user name and second is password.
     *
     * @param httpServletRequest The servlet request.
     * @return User name password pair as an array.
     * @throws AuthenticationException If an error occurred while extracting user name and password.
     */
    private String[] getUserNamePassword(HttpServletRequest httpServletRequest) throws AuthenticationException {

        String basicHeader = httpServletRequest.getHeader(AUTHORISATION_HEADER_NAME);

        if (basicHeader == null) {
            throw new AuthenticationException("Authorization Required");
        }

        String[] userNamePasswordArray = basicHeader.split(" ");

        if (userNamePasswordArray == null || userNamePasswordArray.length != 2) {
            throw new AuthenticationException("Authorization Required");
        }

        String decodedString = decode(userNamePasswordArray[1]);

        String[] array = decodedString.split(":");

        if (array == null || array.length != 2) {
            throw new AuthenticationException("Authorization Required");
        }

        return array;

    }

    @Override
    protected boolean doAuthentication(Object credentials) throws AuthenticationException {
        if (this.dbLookup == null) {
            throw new AuthenticationException("Authenticator is not initialized. Error processing request.");
        }

        if (credentials == null)
            return false;

        HttpServletRequest httpServletRequest = (HttpServletRequest) credentials;

        String[] array = getUserNamePassword(httpServletRequest);

        String userName = array[0];
        String password = array[1];

        try {
            String retrievedPassword = dbLookup.getMatchingColumnValue(userTable, passwordColumn, userNameColumn,
                    userName);

            return retrievedPassword != null && (retrievedPassword.equals(password));

        } catch (SQLException e) {
            throw new AuthenticationException("Error querying database for session information.", e);
        }
    }

    protected void addUserToSession(String userName, HttpServletRequest servletRequest) {

        if (servletRequest.getSession() != null) {
            servletRequest.getSession().setAttribute(USER_IN_SESSION, userName);
        }
    }

    @Override
    public void onSuccessfulAuthentication(Object authenticationInfo) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) authenticationInfo;

        try {
            String[] array = getUserNamePassword(httpServletRequest);

            StringBuilder stringBuilder = new StringBuilder("User : ");

            if (array != null) {

                addUserToSession(array[0], httpServletRequest);

                stringBuilder.append(array[0]).append(" successfully logged into system at ").append(getCurrentTime());
                log.info(stringBuilder.toString());

            } else {
                log.error("System error occurred while extracting user name after authentication. " +
                        "Couldn't extract user name from the request.");
            }
        } catch (AuthenticationException e) {
            log.error("System error occurred while extracting user name after authentication.", e);
        }

    }

    @Override
    public void onFailedAuthentication(Object authenticationInfo) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) authenticationInfo;

        try {
            String[] array = getUserNamePassword(httpServletRequest);

            StringBuilder stringBuilder = new StringBuilder("User : ");

            if (array != null) {

                stringBuilder.append(array[0]).append(" Failed login attempt to system at ").append(getCurrentTime());
                log.warn(stringBuilder.toString());

            } else {
                stringBuilder.append("Failed login attempt to system at ").append(getCurrentTime()).append( ". User unknown.");
                log.warn(stringBuilder.toString());
            }
        } catch (AuthenticationException e) {
            log.error("System error occurred while extracting user name after authentication.", e);
        }
    }

    @Override
    public boolean isAuthenticated(Object credentials) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) credentials;

        HttpSession httpSession = httpServletRequest.getSession();

        return httpSession != null && httpSession.getAttribute(USER_IN_SESSION) != null;

    }

    @Override
    public boolean canProcess(Object credentials) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) credentials;

        return (httpServletRequest.getHeader(AUTHORISATION_HEADER_NAME) != null);
    }

    @Override
    public void configure(Node node) throws RuntimeException {

        super.configure(node);

        /**
         <specificConfigurations>
         <database>
         <jdbcUrl></jdbcUrl>
         <databaseDriver></databaseDriver>
         <userName></userName>
         <password></password>
         <userTableName></userTableName>
         <userNameColumnName></userNameColumnName>
         <passwordColumnName></passwordColumnName>
         </database>
         </specificConfigurations>
         */

        NodeList databaseNodeList = node.getChildNodes();

        Node databaseNode = null;

        for (int k = 0; k < databaseNodeList.getLength(); ++k) {

            Node n = databaseNodeList.item(k);

            if (n != null && n.getNodeType() == Node.ELEMENT_NODE) {
                databaseNode = n;
            }
        }

        if (databaseNode != null) {
            NodeList nodeList = databaseNode.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node n = nodeList.item(i);

                if (n.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) n;

                    if (element.getNodeName().equals("userTableName")) {
                        userTable = element.getFirstChild().getNodeValue();
                    } else if (element.getNodeName().equals("userNameColumnName")) {
                        userNameColumn = element.getFirstChild().getNodeValue();
                    } else if (element.getNodeName().equals("passwordColumnName")) {
                        passwordColumn = element.getFirstChild().getNodeValue();
                    }
                }
            }
        }

        initializeDatabaseLookup();

        StringBuilder stringBuilder = new StringBuilder("Configuring DB parameters for authenticator with User name Table - ");
        stringBuilder.append(userTable).append(" User name column - ").append(userNameColumn).append(" Password column - ").
                append(passwordColumn);

        log.info(stringBuilder.toString());

    }

    private void initializeDatabaseLookup() throws RuntimeException {

        this.dbLookup = new DBLookup(getDatabaseURL(), getDatabaseUserName(), getDatabasePassword(), getDatabaseDriver());

        try {
            this.dbLookup.init();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading database driver. Driver class not found.", e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error loading database driver. Error instantiating driver object.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error loading database driver. Illegal access to driver object.", e);
        }
    }

    public String getUserTable() {
        return userTable;
    }

    public String getUserNameColumn() {
        return userNameColumn;
    }

    public String getPasswordColumn() {
        return passwordColumn;
    }
}
