package org.apache.airavata.services.registry.rest.security.session;

import junit.framework.TestCase;
import org.apache.airavata.security.util.DBLookup;

import java.io.File;

/**
 * Test class for DBSession lookup.
 */
public class DBLookupTest extends TestCase {

    private DBLookup dbLookup;

    private String url = "jdbc:h2:src/test/resources/testdb/test";
    //private String url = "jdbc:h2:modules/commons/airavata-registry-rest/src/test/resources/testdb/test";
    private String userName = "sa";
    private String password = "sa";
    private String driver = "org.h2.Driver";

    private String sessionTable = "Persons";
    private String sessionColumn = "sessionId";

    public void setUp() throws Exception {
        File f = new File(".");
        System.out.println(f.getAbsolutePath());
        dbLookup = new DBLookup(url, userName, password, driver);
        dbLookup.init();

    }

    public void tearDown() throws Exception {

    }

    public void testGetSessionString() throws Exception {

        String sessionId = dbLookup.getMatchingColumnValue(sessionTable, sessionColumn, "1234");
        assertNotNull(sessionId);
    }

    public void testGetSessionStringInvalidSession() throws Exception {
        String sessionId = dbLookup.getMatchingColumnValue(sessionTable, sessionColumn, "12345");
        assertNull(sessionId);
    }
}

