package org.apache.airavata.allocation.manager.db.repositories;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.airavata.allocation.manager.db.entities.UserAllocationDetailEntity;
import org.apache.airavata.allocation.manager.db.utils.DBConstants;
import org.apache.airavata.allocation.manager.models.UserAllocationDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAllocationDetailRepository extends AbstractRepository<UserAllocationDetail, UserAllocationDetailEntity, String> {
   // private final static Logger logger = LoggerFactory.getLogger(DomainRepository.class);

    public UserAllocationDetailRepository(){
        super(UserAllocationDetail.class, UserAllocationDetailEntity.class);
    }
    
//    public String getPrimaryOwner(String projectId) throws Exception{
//        Map<String,Object> queryParameters = new HashMap<>();
//        String query = "SELECT DISTINCT p from " + UserAllocationDetailEntity.class.getSimpleName() + " as p";
//        query += " WHERE ";
//        query += "p." + DBConstants.UserAllocationDetailTable.PROJECTID + " = " + projectId + " AND "; 
//        query += "p." + DBConstants.UserAllocationDetailTable.ISPRIMARYOWNER + " = 1" ;
//        queryParameters.put(DBConstants.UserAllocationDetailTable.PROJECTID, projectId);
//        return select(query, queryParameters, 0, -1).get(0).getId().getUsername();
//    }
//    
    public List<UserAllocationDetail> getAllUserRequests() throws Exception{
       Map<String,Object> queryParameters = new HashMap<>();
        String query = "SELECT DISTINCT p from " + UserAllocationDetailEntity.class.getSimpleName() + " as p";
        return select(query, queryParameters, 0, -1);

    }
    
//     public List<UserAllocationDetail> getAllReviewsForARequest(String projectId) throws Exception{
//        Map<String,Object> queryParameters = new HashMap<>();
//        String query = "SELECT * from " + UserAllocationDetailEntity.class.getSimpleName();
//        query += " WHERE ";
//        query += DBConstants.UserAllocationDetailTable.PROJECTID + " = " + projectId + " AND "; 
//        query += DBConstants.UserAllocationDetailTable.ISPRIMARYOWNER + " = FALSE" ;
//        queryParameters.put(DBConstants.UserAllocationDetailTable.PROJECTID, projectId);
//        return select(query, queryParameters, 0, -1);
//    }
}