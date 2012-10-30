package org.apache.airavata.credential.store.impl.db;

import junit.framework.Assert;
import org.apache.airavata.credential.store.Mapping;
import org.junit.Ignore;

import java.util.List;

/**
 * DAO class for Mapping.
 */
@Ignore
public class MappingDAOTest extends DAOBaseTestCase {

    private MappingDAO mappingDAO;

    public void setUp() throws Exception {
        super.setUp();
        mappingDAO = new MappingDAO(getDbUtil());
    }

    public void testAddMapping() throws Exception {
        Mapping m = new Mapping("gw3", "amila", "lahiru");
        mappingDAO.addMapping(m);

        String communityUser = mappingDAO.getMappingCommunityUser("lahiru", "gw3");
        Assert.assertEquals(communityUser, "amila");
    }

    public void testDeleteGatewayMapping() throws Exception {
        Mapping m = new Mapping("gw4", "amila", "lahiru");
        mappingDAO.addMapping(m);

        mappingDAO.deleteGatewayMapping("lahiru", "gw4");
        String communityUser = mappingDAO.getMappingCommunityUser("lahiru", "gw4");
        Assert.assertNull(communityUser);

    }

    public void testDeleteGatewayCommunityAccountMappings() throws Exception {
        Mapping m = new Mapping("gw5", "c2", "lahiru");
        mappingDAO.addMapping(m);

        mappingDAO.deleteGatewayCommunityAccountMappings("c2", "gw1");
        List<String> portalUsers = mappingDAO.getMappingPortalUsers("c2", "gw1");

        Assert.assertEquals(0, portalUsers.size());
    }

    public void testGetMappingPortalUsers() throws Exception {
        Mapping m = new Mapping("gw6", "c2", "lahiru");
        mappingDAO.addMapping(m);

        List<String> portalUsers = mappingDAO.getMappingPortalUsers("c2", "gw6");
        Assert.assertEquals(1, portalUsers.size());
        Assert.assertEquals("lahiru", portalUsers.get(0));

    }

    public void testGetMappingCommunityUser() throws Exception {
        Mapping m = new Mapping("gw7", "c2", "lahiru");
        mappingDAO.addMapping(m);

        String communityUser = mappingDAO.getMappingCommunityUser("lahiru", "gw7");
        Assert.assertEquals(communityUser, "c2");
    }

    public void testGetCredentialsForPortalUser() throws Exception {
        String certificate = mappingDAO.getCredentials("lahiru", "gw2");
        System.out.println(certificate);
    }
}
