package org.apache.airavata.credential.store.impl.db;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.credential.store.CertificateCredential;
import org.apache.airavata.credential.store.CommunityUser;
import org.apache.airavata.credential.store.CredentialStoreException;
import org.apache.airavata.credential.store.util.DBUtil;

import java.sql.Connection;
import java.util.List;

/**
 * Test class for credential class
 */
public class CredentialsDAOTest extends DAOBaseTestCase {

    private CredentialsDAO credentialsDAO;

    private String certificateString = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDWjCCAkKgAwIBAgIEUHMnRzANBgkqhkiG9w0BAQUFADBvMQswCQYDVQQGEwJV\n" +
            "UzEQMA4GA1UECBMHSW5kaWFuYTEUMBIGA1UEBxMLQmxvb21pbmd0b24xEDAOBgNV\n" +
            "BAoTB0luZGlhbmExCzAJBgNVBAsTAklVMRkwFwYDVQQDExBBbWlsYSBKYXlhc2Vr\n" +
            "YXJhMB4XDTEyMTAwODE5MTkzNVoXDTEzMDEwNjE5MTkzNVowbzELMAkGA1UEBhMC\n" +
            "VVMxEDAOBgNVBAgTB0luZGlhbmExFDASBgNVBAcTC0Jsb29taW5ndG9uMRAwDgYD\n" +
            "VQQKEwdJbmRpYW5hMQswCQYDVQQLEwJJVTEZMBcGA1UEAxMQQW1pbGEgSmF5YXNl\n" +
            "a2FyYTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJEdoR4gu32xf8C+\n" +
            "H6bVymFWkO6SAM4iAbP5hruDG8HftyfaEmz8MM651X3CoEiPRUeYyoxl5CwSARx6\n" +
            "mex1h4Hy7lbVwRKEOnsJwF0POwDo6qV5eFII1ac/XiWpBjEeHpLwoOoOm55pZC6M\n" +
            "d/YXQcZhWqpru3OOkK7nozADpOY32A7gAndMjPuuLtT1TsY+mRuHM+o7jv0cKkTM\n" +
            "SfJMScqSAWlMrDYyI3lr2nkPsYvCxP+eFp6oY0U604TAYH7ycDmemtm4OEP7pylj\n" +
            "HjmH9EpBj+kDwtexpLs6VBcavRne7Mh7JBejkORPcgcEQFSkSURUk6PSrzYMo4oq\n" +
            "Y+GxPUMCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAJamQWGcmDx59KYeo0WLMQ7Xj\n" +
            "15XKddqrdxSJetaFtBJ23XhOFHBesMAVtCKImxw9brRetUYpKV9YfBZdGInolMPX\n" +
            "HAeACHVkEeXhGft2sMt/Y9gqFSpROO5ifGKnPRosBzjiWZPAXi6giH8bf3vrQQPB\n" +
            "z7j3Dz/1u3zxwMYuTRScZ9b/RQ65Fbs2WmNnlhr8qLkgHke9Hb2r1SV0V7AkxnWb\n" +
            "gfsK27V3RUlxZvc24lhWXeRKZDrLPZrU/DscCW4x439IE+9B+Vvq4cD4g8BPoNzM\n" +
            "2jZWzXAHStjOsOpCohkXO53jiC8zW6rrqqos83Oo9E2WG8RW801vXegJif1fNQ==\n" +
            "-----END CERTIFICATE-----";

    private String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIDWjCCAkKgAwIBAgIEUHMnRzANBgkqhkiG9w0BAQUFADBvMQswCQYDVQQGEwJV\n" +
            "UzEQMA4GA1UECBMHSW5kaWFuYTEUMBIGA1UEBxMLQmxvb21pbmd0b24xEDAOBgNV\n" +
            "BAoTB0luZGlhbmExCzAJBgNVBAsTAklVMRkwFwYDVQQDExBBbWlsYSBKYXlhc2Vr\n" +
            "YXJhMB4XDTEyMTAwODE5MTkzNVoXDTEzMDEwNjE5MTkzNVowbzELMAkGA1UEBhMC\n" +
            "VVMxEDAOBgNVBAgTB0luZGlhbmExFDASBgNVBAcTC0Jsb29taW5ndG9uMRAwDgYD\n" +
            "VQQKEwdJbmRpYW5hMQswCQYDVQQLEwJJVTEZMBcGA1UEAxMQQW1pbGEgSmF5YXNl\n" +
            "a2FyYTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJEdoR4gu32xf8C+\n" +
            "H6bVymFWkO6SAM4iAbP5hruDG8HftyfaEmz8MM651X3CoEiPRUeYyoxl5CwSARx6\n" +
            "mex1h4Hy7lbVwRKEOnsJwF0POwDo6qV5eFII1ac/XiWpBjEeHpLwoOoOm55pZC6M\n" +
            "d/YXQcZhWqpru3OOkK7nozADpOY32A7gAndMjPuuLtT1TsY+mRuHM+o7jv0cKkTM\n" +
            "SfJMScqSAWlMrDYyI3lr2nkPsYvCxP+eFp6oY0U604TAYH7ycDmemtm4OEP7pylj\n" +
            "HjmH9EpBj+kDwtexpLs6VBcavRne7Mh7JBejkORPcgcEQFSkSURUk6PSrzYMo4oq\n" +
            "Y+GxPUMCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAJamQWGcmDx59KYeo0WLMQ7Xj\n" +
            "15XKddqrdxSJetaFtBJ23XhOFHBesMAVtCKImxw9brRetUYpKV9YfBZdGInolMPX\n" +
            "HAeACHVkEeXhGft2sMt/Y9gqFSpROO5ifGKnPRosBzjiWZPAXi6giH8bf3vrQQPB\n" +
            "z7j3Dz/1u3zxwMYuTRScZ9b/RQ65Fbs2WmNnlhr8qLkgHke9Hb2r1SV0V7AkxnWb\n" +
            "gfsK27V3RUlxZvc24lhWXeRKZDrLPZrU/DscCW4x439IE+9B+Vvq4cD4g8BPoNzM\n" +
            "2jZWzXAHStjOsOpCohkXO53jiC8zW6rrqqos83Oo9E2WG8RW801vXegJif1fNQ==\n" +
            "-----END PRIVATE KEY-----";

    private CommunityUser getCommunityUser(String gateway, String name) {
        return new CommunityUser(gateway, name, "amila@sciencegateway.org");
    }

    public void setUp() throws Exception {
        super.setUp();
        credentialsDAO = new CredentialsDAO(getDbUtil());

        // Cleanup tables;
        Connection connection = getDbUtil().getConnection();
        DBUtil.truncate("credentials", connection);
        DBUtil.truncate("community_user", connection);

        connection.close();
    }

    private void addTestCredentials() throws Exception {

        CertificateCredential certificateCredential = new CertificateCredential();
        certificateCredential.setCertificate(certificateString);
        certificateCredential.setPrivateKey(privateKey);
        certificateCredential.setCommunityUser(getCommunityUser("gw1", "tom"));
        certificateCredential.setLifeTime(1000);
        certificateCredential.setPortalUserName("jerry");
        certificateCredential.setNotBefore("13 OCT 2012 5:34:23");
        certificateCredential.setNotAfter("14 OCT 2012 5:34:23");

        credentialsDAO.addCredentials(certificateCredential);

    }

    public void testAddCredentials() throws Exception {

        addTestCredentials();

        CertificateCredential certificateCredential
                = credentialsDAO.getCredential("gw1", "tom");
        Assert.assertNotNull(certificateCredential);
        Assert.assertEquals("jerry", certificateCredential.getPortalUserName());
        Assert.assertEquals(certificateString, certificateCredential.getCertificate());
        Assert.assertEquals(privateKey, certificateCredential.getPrivateKey());

    }

    public void testDeleteCredentials() throws Exception {

        addTestCredentials();

        CertificateCredential certificateCredential
                = credentialsDAO.getCredential("gw1", "tom");
        Assert.assertNotNull(certificateCredential);

        credentialsDAO.deleteCredentials("gw1", "tom");

        certificateCredential = credentialsDAO.getCredential("gw1", "tom");
        Assert.assertNull(certificateCredential);
    }

    public void testUpdateCredentials() throws Exception {

        addTestCredentials();

        CertificateCredential certificateCredential = new CertificateCredential();
        certificateCredential.setCommunityUser(getCommunityUser("gw1", "tom"));
        certificateCredential.setCertificate("new.........Cert");
        certificateCredential.setPrivateKey("new..........PrivateKey");
        certificateCredential.setPortalUserName("test2");
        certificateCredential.setLifeTime(50);
        certificateCredential.setNotBefore("15 OCT 2012 5:34:23");
        certificateCredential.setNotAfter("16 OCT 2012 5:34:23");

        credentialsDAO.updateCredentials(certificateCredential);

        certificateCredential = credentialsDAO.getCredential("gw1", "tom");

        Assert.assertEquals("new.........Cert", certificateCredential.getCertificate());
        Assert.assertEquals("new..........PrivateKey", certificateCredential.getPrivateKey());
        Assert.assertEquals("test2", certificateCredential.getPortalUserName());

    }

    public void testGetCredentials() throws Exception {

        addTestCredentials();

        CertificateCredential certificateCredential = credentialsDAO.getCredential("gw1", "tom");

        Assert.assertEquals(certificateString, certificateCredential.getCertificate());
        Assert.assertEquals(privateKey, certificateCredential.getPrivateKey());
    }

    public void testGetGatewayCredentials() throws Exception {

        addTestCredentials();

        List<CertificateCredential> list = credentialsDAO.getCredentials("gw1");

        Assert.assertEquals(1, list.size());
    }
}
