package org.apache.airavata.allocation.manager.db.repositories;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.airavata.allocation.manager.db.entities.ReviewerAllocationDetailEntity;
import org.apache.airavata.allocation.manager.db.entities.ReviewerAllocationDetailEntityPK;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
import org.apache.airavata.allocation.manager.models.ReviewerAllocationDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewerAllocationDetailRepository extends
		AbstractRepository<ReviewerAllocationDetail, ReviewerAllocationDetailEntity, ReviewerAllocationDetailEntityPK> {
	// private final static Logger logger =
	// LoggerFactory.getLogger(DomainRepository.class);

	public ReviewerAllocationDetailRepository() {
		super(ReviewerAllocationDetail.class, ReviewerAllocationDetailEntity.class);
	}

	public List<ReviewerAllocationDetail> getAllReviewsForARequest(String projectId) throws Exception {
		Map<String, Object> queryParameters = new HashMap<>();
		String query = "SELECT * from " + ReviewerAllocationDetailEntity.class.getSimpleName();
		query += " WHERE ";
		query += DBConstants.UserAllocationDetailTable.PROJECTID + " = " + projectId;
		queryParameters.put(DBConstants.UserAllocationDetailTable.PROJECTID, projectId);
		return select(query, queryParameters, 0, -1);
	}
}