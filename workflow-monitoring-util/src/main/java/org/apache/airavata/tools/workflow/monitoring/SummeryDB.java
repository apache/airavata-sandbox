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

package org.apache.airavata.tools.workflow.monitoring;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;

public class SummeryDB implements StatusConstants {

    private static final String TABLE_NAME = "summary";

    private static String SQL_INSERT_STMT = "INSERT INTO " + TABLE_NAME
            + " (workflowid, templateid, status, startTime, username) VALUES (?,?,?,?,?)";

    private static String SQL_UPDATE_STMT = "UPDATE " + TABLE_NAME + " set status=?, endTime=? WHERE workflowid=?";

    private JdbcStorage db;

    public SummeryDB(JdbcStorage db) {
        this.db = db;
    }

    public void insert(String workflowID, String templateID, String username) {
        if (null == templateID) {
            templateID = workflowID.substring(0, workflowID.indexOf("/instance"));

        }
        try {
            Connection connection = db.connect();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_STMT);
            stmt.setString(1, workflowID);
            stmt.setString(2, templateID);
            stmt.setString(3, STATUS_RUNNING);
            stmt.setBigDecimal(4, new BigDecimal(System.currentTimeMillis()));
            stmt.setString(5, username);
            db.insert(stmt);
            stmt.close();
            db.closeConnection(connection);
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

    public void edit(String workflowid, String status) {
        try {
            Connection connection = db.connect();
            PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_STMT);
            stmt.setString(1, status);
            stmt.setBigDecimal(2, new BigDecimal(System.currentTimeMillis()));
            stmt.setString(3, workflowid);
            stmt.executeUpdate();
            stmt.close();
            db.closeConnection(connection);
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

}