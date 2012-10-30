package org.apache.airavata.credential.store.impl.db;

import org.apache.airavata.credential.store.CredentialStoreException;
import org.apache.airavata.credential.store.Mapping;
import org.apache.airavata.credential.store.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access class for Mapping table.
 */
public class MappingDAO extends ParentDAO {

    public MappingDAO(DBUtil dbUtil) {
        super(dbUtil);
    }

    public void addMapping (Mapping mapping) throws CredentialStoreException {

        String sql = "insert into mapping values (?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, mapping.getGatewayName());
            preparedStatement.setString(2, mapping.getCommunityUser());
            preparedStatement.setString(3, mapping.getPortalUser());

            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error persisting community user.");
            stringBuilder.append("gateway - ").append(mapping.getGatewayName());
            stringBuilder.append("community user name - ").append(mapping.getCommunityUser());
            stringBuilder.append("portal user name - ").append(mapping.getPortalUser());

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }
    }


    public void deleteGatewayMapping(String portalUser, String gatewayName) throws CredentialStoreException {

        String sql = "delete from mapping where gateway_name=? and portal_user_name=?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, portalUser);

            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error deleting mapping for portal user.");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("portal user name - ").append(portalUser);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }
    }

    public void deleteGatewayCommunityAccountMappings(String communityUserName,
                                                      String gatewayName) throws CredentialStoreException {

        String sql = "delete from mapping where gateway_name=? and community_user_name=?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, communityUserName);

            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error deleting mapping for portal user.");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("community user name - ").append(communityUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }
    }



    public List<String> getMappingPortalUsers (String communityUserName, String gatewayName) throws
            CredentialStoreException{

        String sql = "select portal_user_name from mapping where gateway_name=? and community_user_name=?;";

        List<String> portalUsers = new ArrayList<String>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, communityUserName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                portalUsers.add(resultSet.getString("portal_user_name"));
            }

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error retrieving mapping user.");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("community user name - ").append(communityUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }

        return portalUsers;
    }

    public String getMappingCommunityUser (String portalUserName, String gatewayName) throws CredentialStoreException {

        String sql = "select community_user_name from mapping where gateway_name=? and portal_user_name=?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, portalUserName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("community_user_name");
            }

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error retrieving mapping user.");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("portal user name - ").append(portalUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }

        return null;

    }

    public String getCredentials(String portalUserName, String gatewayName) throws Exception {

        String sql = "select credential from credentials where credentials.community_user_name in " +
                "(select mapping.community_user_name from mapping where mapping.gateway_name = ?" +
                "and mapping.portal_user_name = ?);";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, portalUserName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("credential");
            }

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error retrieving credentials for ");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("portal user name - ").append(portalUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }

        return null;

    }

}

