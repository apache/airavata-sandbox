package org.apache.airavata.allocation.manager.db.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.airavata.allocation.manager.db.entities.ProjectReviewerEntity;
import org.apache.airavata.allocation.manager.db.entities.ProjectReviewerEntityPK;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
import org.apache.airavata.allocation.manager.models.AllocationManagerException;
import org.apache.airavata.allocation.manager.models.ProjectReviewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectReviewerRepository extends AbstractRepository<ProjectReviewer, ProjectReviewerEntity, ProjectReviewerEntityPK> {
    private final static Logger logger = LoggerFactory.getLogger(DomainRepository.class);

    public ProjectReviewerRepository(){
        super(ProjectReviewer.class, ProjectReviewerEntity.class);
    }
    
    /*Method for getting a list of project assigned to a reviewer*/
    public List<ProjectReviewer> getProjectForReviewer(String reviewerUserName) throws AllocationManagerException, Exception {
        String query = "SELECT "+ DBConstants.ProjectReviewerTable.PROJECTID +" from " + ProjectReviewerEntity.class.getSimpleName();
        query += " WHERE ";
        query += DBConstants.ProjectReviewerTable.REVIEWER + " = " + reviewerUserName;
        Map<String,Object> queryParameters = new HashMap<>();
        queryParameters.put(DBConstants.ProjectReviewerTable.REVIEWER, reviewerUserName);
        return select(query, queryParameters, 0, -1);
    }
}