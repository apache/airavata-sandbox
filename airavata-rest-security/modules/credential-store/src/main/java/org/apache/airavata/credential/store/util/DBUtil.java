package org.apache.airavata.credential.store.util;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

/**
 * Database utility class.
 */
public class DBUtil {

    private String jdbcUrl;
    private String databaseUserName;
    private String databasePassword;
    private String driverName;

    protected static Logger log = LoggerFactory.getLogger(DBUtil.class);

    private Properties properties;

    public DBUtil(String jdbcUrl, String userName, String password, String driver) {

        this.jdbcUrl = jdbcUrl;
        this.databaseUserName = userName;
        this.databasePassword = password;
        this.driverName = driver;
    }

    public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        properties = new Properties();

        properties.put("user", databaseUserName);
        properties.put("password", databasePassword);
        properties.put("characterEncoding", "ISO-8859-1");
        properties.put("useUnicode", "true");

        loadDriver();
    }

    private void loadDriver() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName(driverName).newInstance();
    }

    public DataSource getDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(this.driverName);
        ds.setUsername(this.databaseUserName);
        ds.setPassword(this.databasePassword);
        ds.setUrl(this.jdbcUrl);

        return ds;
    }

    /**
     * Mainly useful for tests.
     * @param tableName The table name.
     * @param connection The connection to be used.
     */
    public static void truncate(String tableName, Connection connection) throws SQLException {

        String sql = "delete from " + tableName;

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        connection.commit();

    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcUrl, properties);
        connection.setAutoCommit(false);

        return connection;
    }

    public void cleanup(PreparedStatement preparedStatement, Connection connection) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.error("Error closing prepared statement.", e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Error closing database connection.", e);
            }
        }
    }

    public static DBUtil getDBUtil(ServletContext servletContext) throws Exception{

        String jdbcUrl = servletContext.getInitParameter("credential-store-jdbc-url");
        String userName = servletContext.getInitParameter("credential-store-db-user");
        String password = servletContext.getInitParameter("credential-store-db-password");
        String driverName = servletContext.getInitParameter("credential-store-db-driver");

        StringBuilder stringBuilder = new StringBuilder("Starting credential store, connecting to database - ");
        stringBuilder.append(jdbcUrl).append(" DB user - ").append(userName).
                append(" driver name - ").append(driverName);

        log.info(stringBuilder.toString());

        DBUtil dbUtil = new DBUtil(jdbcUrl, userName, password, driverName);
        try {
            dbUtil.init();
        } catch (Exception e) {
            log.error("Error initializing database operations.", e);
            throw e;
        }

        return dbUtil;
    }

}

