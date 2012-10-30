package org.apache.airavata.credential.store.impl.db;

import org.apache.airavata.credential.store.CommunityUser;
import org.apache.airavata.credential.store.util.DBUtil;

import java.sql.Connection;
import java.util.List;

/**
 * Test for community user DAO.
 */
public class CommunityUserDAOTest extends DAOBaseTestCase {

    private CommunityUserDAO communityUserDAO;

    public void setUp() throws Exception {
        super.setUp();

        communityUserDAO = new CommunityUserDAO(getDbUtil());

        Connection connection = getDbUtil().getConnection();
        DBUtil.truncate("community_user", connection);

        connection.close();
    }

    public void testAddCommunityUser() throws Exception {

        CommunityUser communityUser = new CommunityUser("gw1", "ogce","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        communityUser = new CommunityUser("gw1", "ogce2","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        CommunityUser user = communityUserDAO.getCommunityUser("gw1", "ogce");
        assertNotNull(user);
        assertEquals("ogce@sciencegateway.org", user.getUserEmail());

        user = communityUserDAO.getCommunityUser("gw1", "ogce2");
        assertNotNull(user);
        assertEquals("ogce@sciencegateway.org", user.getUserEmail());
    }

    public void testDeleteCommunityUser() throws Exception {

        CommunityUser communityUser = new CommunityUser("gw1", "ogce","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        CommunityUser user = communityUserDAO.getCommunityUser("gw1", "ogce");
        assertNotNull(user);

        communityUser = new CommunityUser("gw1", "ogce","ogce@sciencegateway.org");
        communityUserDAO.deleteCommunityUser(communityUser);

        user = communityUserDAO.getCommunityUser("gw1", "ogce");
        assertNull(user);

    }

    public void testGetCommunityUsers() throws Exception {

        CommunityUser communityUser = new CommunityUser("gw1", "ogce","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        CommunityUser user = communityUserDAO.getCommunityUser("gw1", "ogce");
        assertNotNull(user);
        assertEquals("ogce@sciencegateway.org", user.getUserEmail());

    }

    public void testGetCommunityUsersForGateway() throws Exception {

        CommunityUser communityUser = new CommunityUser("gw1", "ogce","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        communityUser = new CommunityUser("gw1", "ogce2","ogce@sciencegateway.org");
        communityUserDAO.addCommunityUser(communityUser);

        List<CommunityUser> users = communityUserDAO.getCommunityUsers("gw1");
        assertNotNull(users);
        assertEquals(2, users.size());

        assertEquals(users.get(0).getUserName(), "ogce");
        assertEquals(users.get(1).getUserName(), "ogce2");
    }
}
