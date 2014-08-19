
/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.airavata.scheduler.jobthrottler;

import java.sql.*;
import java.util.ArrayList;

public class MetaScheduler {
    
    public static Connection mySQLConnection()  {
        String jdbcDriver = "com.mysql.jdbc.Driver";
        String jdbcUser="******";
        String jdbcPwd="********";
        String jdbcUrl="jdbc:mysql://rdc04.uits.iu.edu:3059/scheduler";
        Connection connect = null;
        try {
        Class.forName(jdbcDriver).newInstance();
        connect = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPwd);         
        } 
        
        catch (Exception e) {
            System.out.println("Connection to mysql Failed!");
            e.printStackTrace();
            return(null);      
        }
        return(connect);
      }
    
    public static ArrayList<String> submitThrottleJob(ArrayList<String> experimentData) {
    	ArrayList<String> statusData = new ArrayList<String>(); 
        String hostID = "";
        String queueID = "";
        String experimentID = "";
        String gatewayID = "";
        String jobStatus = "";
        int activeJobs  = 0;
        int queueLimit = 0;
        
        Connection conn = mySQLConnection();
        
        int dataNDX = 0;
        while (dataNDX < experimentData.size()) {
            hostID = experimentData.get(dataNDX);
            queueID = experimentData.get(dataNDX+1);
            gatewayID = experimentData.get(dataNDX+2);
            experimentID = experimentData.get(dataNDX+3);
            queueLimit = getQueueLimits(hostID,queueID,conn);
            activeJobs = getActiveJobs(gatewayID, hostID, queueID, conn);
            jobStatus = getJobStatus(activeJobs, queueLimit);
            if (!updateActiveJobs(gatewayID, hostID, queueID, experimentID, jobStatus, conn))
            	jobStatus = "ERROR";
            statusData.add(jobStatus);
            dataNDX += 4;
       }
        return(statusData);
    }
    
    public static Boolean updateActiveJobs(String gatewayID, String hostID, 
    		String queueID, String jobID, String jobStatus, Connection conn ) {	
        try {
            try {
                String sql = "insert into activejobs "
                		+ " (gatewayID, hostID, queueName, jobID, jobState) VALUES "
                		+ " (?,?,?,?,?) ";
                PreparedStatement insertSQL = conn.prepareStatement(sql);
                insertSQL.setString(1, gatewayID);
                insertSQL.setString(2, hostID);
                insertSQL.setString(3, queueID);
                insertSQL.setString(4, jobID);
                insertSQL.setString(5, jobStatus);               
                insertSQL.executeUpdate();

            } finally {

            }
        } catch (SQLException e) {
            return false;
        }	
    	return(true);
    }
    
    public static String getJobStatus(int activeJobs, int queueLimit) {
    	String jobStatus = "";
    	if (queueLimit > activeJobs)
    		jobStatus = "SUBMIT";
    	else
    		jobStatus = "HOLD";
    	return(jobStatus);
    }
    
    public static int getActiveJobs(String gatewayID, String hostID, String queueID, Connection conn) {
    	int activeJobs = 0;
        try {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                String sql = "select count(*) from activejobs where " +
                " gatewayID = ? and hostID = ? and queueName = ?";
                PreparedStatement updateSQL = conn.prepareStatement(sql);
                updateSQL.setString(1, gatewayID);
                updateSQL.setString(2, hostID);
                updateSQL.setString(3, queueID);
                ResultSet rs = updateSQL.executeQuery();
                if (rs != null) {
                	 rs.next();          
                	 activeJobs = rs.getInt(1);                
                	 rs.close();
                }
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            return -1;
        }
        return activeJobs;
    }    
    
    public static int getQueueLimits(String hostID, String queueID, Connection conn) {
    	int queueLimit = 0;
        try {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                String sql = "select * from queuelimits where hostID = ? and queueName = ?";
                PreparedStatement updateSQL = conn.prepareStatement(sql);
                updateSQL.setString(1, hostID);
                updateSQL.setString(2, queueID);
                ResultSet rs = updateSQL.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                          queueLimit = (Integer) rs.getObject("queueLimit");
                        }                    
                    rs.close();
                }
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    return -1;
                }
            }
        } catch (SQLException e) {
            return -1;
        }
        return queueLimit;
    }
    
    public static Boolean clearActiveJobs() {	
        try {
        	Connection conn = mySQLConnection();
            Statement statement = null;
            try {
                statement = conn.createStatement();
                String sql = "delete from activejobs";
                statement.executeUpdate(sql);
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    return false;
                }
            }
        } catch (SQLException e) {
            return false;
        }	
    	return(true);
    }
    
    public static Boolean changeJobStatus(ArrayList<String> experimentData) {	
        String hostID = "";
        String queueID = "";
        String gatewayID = "";
        int deleteCtr = (experimentData.size() + 1) / 4;
        
        int dataNDX = 0;
        if (deleteCtr > 0) {
            hostID = experimentData.get(dataNDX);
            queueID = experimentData.get(dataNDX+1);
            gatewayID = experimentData.get(dataNDX+2);        	
        }
        try {
        	Connection conn = mySQLConnection();
            try {
                // not really meaningful, but useful for testing
                String sql = "delete top (?) from activejobs where " +
                		"hostID = ? and queueID = ? and gatewayID = ? ";
                PreparedStatement updateSQL = conn.prepareStatement(sql);
                updateSQL.setInt(1, deleteCtr);
                updateSQL.setString(2, hostID);
                updateSQL.setString(3, queueID);
                updateSQL.setString(4, gatewayID);
                updateSQL.executeQuery();
            } finally {

            }
        } catch (SQLException e) {
            return false;
        }	
    	return(true);
    }
}
