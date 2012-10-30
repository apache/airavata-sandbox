package org.apache.airavata.credential.store.impl.db;

import junit.framework.TestCase;
import org.apache.airavata.credential.store.util.DBUtil;

/**
 * Base test class for DB operation testing.
 */
public class DAOBaseTestCase extends TestCase {

    private DBUtil dbUtil;

    public DAOBaseTestCase() {

        dbUtil = new DBUtil(//"jdbc:mysql://localhost/airavata",
                //  "jdbc:mysql://localhost/airavata",

                "jdbc:h2:../../src/test/resources/testdb/test",
                //   "airavata", "secret", "com.mysql.jdbc.Driver");
                //   "root", "root123", "com.mysql.jdbc.Driver");
                "sa", "sa", "org.h2.Driver");
        try {
            dbUtil.init();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected DBUtil getDbUtil() {
        return dbUtil;
    }

}
