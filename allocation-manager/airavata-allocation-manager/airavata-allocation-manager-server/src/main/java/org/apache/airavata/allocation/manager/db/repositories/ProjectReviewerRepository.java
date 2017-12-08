package org.apache.airavata.allocation.manager.db.repositories;

import org.apache.airavata.allocation.manager.db.entities.ProjectReviewerEntity;
import org.apache.airavata.allocation.manager.models.ProjectReviewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectReviewerRepository extends AbstractRepository<ProjectReviewer, ProjectReviewerEntity, String> {
    private final static Logger logger = LoggerFactory.getLogger(DomainRepository.class);

    public ProjectReviewerRepository(){
        super(ProjectReviewer.class, ProjectReviewerEntity.class);
    }
}