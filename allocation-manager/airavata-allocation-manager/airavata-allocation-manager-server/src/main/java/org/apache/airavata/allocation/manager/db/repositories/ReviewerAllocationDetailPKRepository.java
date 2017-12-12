package org.apache.airavata.allocation.manager.db.repositories;

import org.apache.airavata.allocation.manager.db.entities.ReviewerAllocationDetailEntityPK;
import org.apache.airavata.allocation.manager.models.ReviewerAllocationDetailPK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReviewerAllocationDetailPKRepository extends AbstractRepository<ReviewerAllocationDetailPK, ReviewerAllocationDetailEntityPK, String> {
  //  private final static Logger logger = LoggerFactory.getLogger(DomainRepository.class);

    public ReviewerAllocationDetailPKRepository(){
        super(ReviewerAllocationDetailPK.class, ReviewerAllocationDetailEntityPK.class);
    }
}