/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.utils;

import org.apache.airavata.model.user.UserProfile;
import org.apache.airavata.allocation.manager.db.entities.UserAllocationDetailEntityPK;
import org.apache.airavata.allocation.manager.models.UserAllocationDetail;

/**
 *
 * @author madrinathapa
 */
public class ThriftDataModelConversion {
  /**
     * Build user object from UserProfile
     * @param userProfile thrift object
     * @return
     * User corresponding to userProfile thrift
     */
    public static UserAllocationDetail getUser(UserProfile userProfile){
        UserAllocationDetail user = new UserAllocationDetail();
        //user.id.setUsername(userProfile.getUserId());
        //user.setDomainId(userProfile.getGatewayId());
        user.id.setUsername(userProfile.getFirstName() + " " + userProfile.getLastName());
        //user.setEmail(userProfile.getEmails().get(0));
        return user;
    }
}