package org.apache.airavata.services.registry.rest.security.session;

import org.apache.airavata.security.AbstractDatabaseAuthenticator;
import org.apache.airavata.security.AuthenticationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * This authenticator will authenticate requests based on a session (NOT HTTP Session) id stored
 * in the database.
 */
public class SessionAuthenticator extends AbstractDatabaseAuthenticator {

    private DBLookup dbLookup;

    private String sessionTable;
    private String sessionColumn;
    private String comparingColumn;

    private static final String AUTHENTICATOR_NAME = "SessionAuthenticator";

    private static final String SESSION_TICKET = "sessionTicket";

    public SessionAuthenticator() {
        super(AUTHENTICATOR_NAME);
    }

    @Override
    public boolean doAuthentication(Object credentials) throws AuthenticationException {

        if (this.dbLookup == null) {
            throw new AuthenticationException("Authenticator is not initialized. Error processing request.");
        }

        if (credentials == null)
            return false;

        HttpServletRequest httpServletRequest = (HttpServletRequest)credentials;
        String sessionTicket = httpServletRequest.getHeader(SESSION_TICKET);
        try {
            String sessionString = dbLookup.getMatchingColumnValue(sessionTable, sessionColumn, sessionTicket);
            return (sessionString != null);
        } catch (SQLException e) {
            throw new AuthenticationException("Error querying database for session information.", e);
        }
    }

    @Override
    public boolean canProcess(Object credentials) {

        if (credentials instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) credentials;

            String ticket = request.getHeader(SESSION_TICKET);
            if (ticket != null) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onSuccessfulAuthentication(Object authenticationInfo) {

        HttpServletRequest httpServletRequest = (HttpServletRequest)authenticationInfo;
        String sessionTicket = httpServletRequest.getHeader(SESSION_TICKET);

        // Add sessionTicket to http session
        HttpSession httpSession = httpServletRequest.getSession();

        if (httpSession != null) {
            httpSession.setAttribute(SESSION_TICKET, sessionTicket);
        }

        log.info("A request with a session ticket is successfully logged in.");

    }

    @Override
    public void onFailedAuthentication(Object authenticationInfo) {
        log.warn("Failed attempt to login.");
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

                    if (element.getNodeName().equals("sessionTable")) {
                        sessionTable = element.getFirstChild().getNodeValue();
                    } else if (element.getNodeName().equals("sessionColumn")) {
                        sessionColumn = element.getFirstChild().getNodeValue();
                    } else if (element.getNodeName().equals("comparingColumn")) {
                        comparingColumn = element.getFirstChild().getNodeValue();
                    }
                }
            }
        }

        initializeDatabaseLookup();

        StringBuilder stringBuilder = new StringBuilder("Configuring DB parameters for authenticator with Session Table - ");
        stringBuilder.append(sessionTable).append(" Session column - ").append(sessionColumn).append(" Comparing column - ").
                append(comparingColumn);

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

    public String getSessionTable() {
        return sessionTable;
    }

    public void setSessionTable(String sessionTable) {
        this.sessionTable = sessionTable;
    }

    public String getSessionColumn() {
        return sessionColumn;
    }

    public void setSessionColumn(String sessionColumn) {
        this.sessionColumn = sessionColumn;
    }

    public String getComparingColumn() {
        return comparingColumn;
    }

    public void setComparingColumn(String comparingColumn) {
        this.comparingColumn = comparingColumn;
    }


    @Override
    public boolean isAuthenticated(Object credentials) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)credentials;

        if (httpServletRequest.getSession() != null) {
            String sessionTicket = (String)httpServletRequest.getSession().getAttribute(SESSION_TICKET);
            return (sessionTicket != null);
        }

        return false;

    }
}
