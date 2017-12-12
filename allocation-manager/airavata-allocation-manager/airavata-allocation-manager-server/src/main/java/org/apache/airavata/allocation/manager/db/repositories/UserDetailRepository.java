package org.apache.airavata.allocation.manager.db.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.airavata.allocation.manager.db.entities.UserDetailEntity;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
import org.apache.airavata.allocation.manager.models.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDetailRepository extends AbstractRepository<UserDetail, UserDetailEntity, String> {
	// private final static Logger logger =
	// LoggerFactory.getLogger(DomainRepository.class);

	public UserDetailRepository() {
		super(UserDetail.class, UserDetailEntity.class);
	}

	public UserDetail getAdminDetails() throws Exception {
		String userType = "ADMIN";
		String queryString = "SELECT * FROM " + UserDetailEntity.class.getSimpleName() + "WHERE"
				+ DBConstants.UserDetailTable.USERTYPE + "=" + userType;
		Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put(DBConstants.UserDetailTable.USERTYPE, userType);

		return (UserDetail) select(queryString, queryParameters, 0, -1);
	}

	public List<UserDetail> getAllReviewers() throws Exception {
		Map<String, Object> queryParameters = new HashMap<>();
		String query = "SELECT DISTINCT p from " + UserDetailEntity.class.getSimpleName() + " as p";
		query += " WHERE ";
		query += "p." + DBConstants.UserDetailTable.USERTYPE + " ='" + DBConstants.UserType.REVIEWER + "'";
		return select(query, queryParameters, 0, -1);
	}
}