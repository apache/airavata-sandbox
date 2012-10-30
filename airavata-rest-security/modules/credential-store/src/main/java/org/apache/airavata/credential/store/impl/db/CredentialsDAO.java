package org.apache.airavata.credential.store.impl.db;

import org.apache.airavata.credential.store.CommunityUser;
import org.apache.airavata.credential.store.CredentialStoreException;
import org.apache.airavata.credential.store.CertificateCredential;
import org.apache.airavata.credential.store.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access class for credential store.
 */
public class CredentialsDAO extends ParentDAO {

    public CredentialsDAO(DBUtil dbUtil) {
        super(dbUtil);
    }

    public void addCredentials(CertificateCredential certificateCredential) throws CredentialStoreException {

        String sql = "insert into credentials values (?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
        //TODO By any chance will we use some other database other than MySQL ?

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, certificateCredential.getCommunityUser().getGatewayName());
            preparedStatement.setString(2, certificateCredential.getCommunityUser().getUserName());
            preparedStatement.setString(3, certificateCredential.getCertificate());
            preparedStatement.setString(4, certificateCredential.getPrivateKey());
            preparedStatement.setString(5, certificateCredential.getNotBefore());
            preparedStatement.setString(6, certificateCredential.getNotAfter());
            preparedStatement.setLong(7, certificateCredential.getLifeTime());
            preparedStatement.setString(8, certificateCredential.getPortalUserName());


            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error persisting community credentials.");
            stringBuilder.append(" gateway - ").append(certificateCredential.getCommunityUser().getGatewayName());
            stringBuilder.append(" community user name - ").append(certificateCredential.
                    getCommunityUser().getUserName());
            stringBuilder.append(" life time - ").append(certificateCredential.getLifeTime());

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {

            dbUtil.cleanup(preparedStatement, connection);
        }
    }


    public void deleteCredentials(String gatewayName, String communityUserName) throws CredentialStoreException {

        String sql = "delete from credentials where gateway_name=? and community_user_name=?;";

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
            StringBuilder stringBuilder = new StringBuilder("Error deleting credentials for .");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("community user name - ").append(communityUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }
    }

    public void updateCredentials(CertificateCredential certificateCredential) throws CredentialStoreException {

        String sql = "update credentials set credential = ?, private_key = ?, lifetime = ?, " +
                "requesting_portal_user_name = ?, " + "not_before = ?," + "not_after = ?," +
                "requested_time =  CURDATE() where gateway_name = ? and community_user_name = ?";
        //TODO By any chance will we use some other database other than MySQL ?

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, certificateCredential.getCertificate());
            preparedStatement.setString(2, certificateCredential.getPrivateKey());
            preparedStatement.setLong(3, certificateCredential.getLifeTime());
            preparedStatement.setString(4, certificateCredential.getPortalUserName());
            preparedStatement.setString(5, certificateCredential.getNotBefore());
            preparedStatement.setString(6, certificateCredential.getNotAfter());
            preparedStatement.setString(7, certificateCredential.getCommunityUser().getGatewayName());
            preparedStatement.setString(8, certificateCredential.getCommunityUser().getUserName());


            preparedStatement.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error updating credentials.");
            stringBuilder.append(" gateway - ").append(certificateCredential.getCommunityUser().getGatewayName());
            stringBuilder.append(" community user name - ").append(certificateCredential.
                    getCommunityUser().getUserName());
            stringBuilder.append(" life time - ").append(certificateCredential.getLifeTime());

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {

            dbUtil.cleanup(preparedStatement, connection);
        }

    }

    public CertificateCredential getCredential(String gatewayName, String communityUserName)
            throws CredentialStoreException {

        String sql = "select * from credentials where gateway_name=? and community_user_name=?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);
            preparedStatement.setString(2, communityUserName);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                CertificateCredential certificateCredential = new CertificateCredential();

                certificateCredential.setCertificate(resultSet.getString("CREDENTIAL"));
                certificateCredential.setPrivateKey(resultSet.getString("PRIVATE_KEY"));
                certificateCredential.setLifeTime(resultSet.getLong("LIFETIME"));
                certificateCredential.setCommunityUser(new CommunityUser(gatewayName, communityUserName, null));
                certificateCredential.setPortalUserName(resultSet.getString("REQUESTING_PORTAL_USER_NAME"));
                certificateCredential.setCertificateRequestedTime(resultSet.getTimestamp("REQUESTED_TIME"));

                return certificateCredential;
            }

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error retrieving credentials for community user.");
            stringBuilder.append("gateway - ").append(gatewayName);
            stringBuilder.append("community user name - ").append(communityUserName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }

        return null;
    }

    public List<CertificateCredential> getCredentials(String gatewayName)
            throws CredentialStoreException {

        List<CertificateCredential> credentialList = new ArrayList<CertificateCredential>();

        String sql = "select * from credentials where gateway_name=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, gatewayName);

            ResultSet resultSet = preparedStatement.executeQuery();

            CertificateCredential certificateCredential;

            while (resultSet.next()) {
                certificateCredential = new CertificateCredential();

                certificateCredential.setCommunityUser(new CommunityUser(gatewayName,
                        resultSet.getString("COMMUNITY_USER_NAME"), null));
                certificateCredential.setCertificate(resultSet.getString("CREDENTIAL"));
                certificateCredential.setPrivateKey(resultSet.getString("PRIVATE_KEY"));
                certificateCredential.setNotBefore(resultSet.getString("NOT_BEFORE"));
                certificateCredential.setNotBefore(resultSet.getString("NOT_AFTER"));
                certificateCredential.setLifeTime(resultSet.getLong("LIFETIME"));
                certificateCredential.setPortalUserName(resultSet.getString("REQUESTING_PORTAL_USER_NAME"));
                certificateCredential.setCertificateRequestedTime(resultSet.getTimestamp("REQUESTED_TIME"));

                credentialList.add(certificateCredential);
            }

        } catch (SQLException e) {
            StringBuilder stringBuilder = new StringBuilder("Error retrieving credential list for ");
            stringBuilder.append("gateway - ").append(gatewayName);

            log.error(stringBuilder.toString(), e);

            throw new CredentialStoreException(stringBuilder.toString(), e);
        } finally {
            dbUtil.cleanup(preparedStatement, connection);
        }

        return credentialList;
    }

}
