package org.apache.airavata.allocation.manager.db.repositories;

import org.apache.airavata.allocation.manager.db.entities.ProjectReviewerEntityPK;
import org.apache.airavata.allocation.manager.models.ProjectReviewerPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectReviewerPKRepository extends AbstractRepository<ProjectReviewerPK, ProjectReviewerEntityPK, String> {
    private final static Logger logger = LoggerFactory.getLogger(DomainRepository.class);

    public ProjectReviewerPKRepository(){
        super(ProjectReviewerPK.class, ProjectReviewerEntityPK.class);
    }
}