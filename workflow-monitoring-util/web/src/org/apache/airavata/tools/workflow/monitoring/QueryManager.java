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

import org.apache.airavata.tools.workflow.monitoring.db.DBConstants;
import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryManager {
    private static JdbcStorage database = new JdbcStorage(Server.DB_CONFIG_NAME, true);

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public QueryManager() {
    }

    public void close() throws WorkflowMonitoringException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                database.closeConnection(connection);
            }
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

    public ResultSet getDataRelatesToWorkflowId(String workflowId) throws WorkflowMonitoringException {

        try {

            connection = database.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + DBConstants.T_FAULTS_NAME + " WHERE "
                    + DBConstants.T_FAULTS_WORKFLOW_ID + " = '" + workflowId + "'");
            resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }

    }

    public ResultSet getWorkflowsWithinTimeRange(long startDate, long endDate) throws WorkflowMonitoringException {

        // do a bit of validation here
        if (startDate < 0) {
            return null;
        }
        try {

            String sqlString = getSQLForWorkflowInformationRetrieval(startDate, endDate);

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }

    }

    public ResultSet getWorkflowsOfUser(String username) throws WorkflowMonitoringException {

        // do a bit of validation here
        if (username != null || "".equals(username)) {
            return null;
        }

        try {

            String sqlString = getSQLForWorkflowInformationRetrieval(username);

            System.out.println("sqlString = " + sqlString);

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sqlString);
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }

    }

    /**
     * This will process the input dates and provide with an SQL to retrieve workflow data from the database
     * <p/>
     * Note : This method assumes the inputs are validated by the caller before passing them here.
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    private String getSQLForWorkflowInformationRetrieval(long startDate, long endDate) {

        String sql = "SELECT * FROM " + DBConstants.T_SUMMARY_NAME + " WHERE " + DBConstants.T_SUMMARY_START_TIME
                + " > " + startDate;

        if (endDate > 0) {
            sql += " AND " + DBConstants.T_SUMMARY_START_TIME + " < " + endDate;
        }
        return sql;
    }

    private String getSQLForWorkflowInformationRetrieval(String username) {
        return "SELECT * FROM " + DBConstants.T_SUMMARY_NAME + " WHERE " + DBConstants.T_SUMMARY_USERNAME + " LIKE \"%"
                + username + "%\"";

    }

    /**
     * This will retrieve summary information for all the workflow that had started within the given time range. If no
     * end time is given, the current time is assumed.
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public ResultSet getSummaryInformation(long startDate, long endDate) {

        // do a bit of validation here
        if (startDate < 0) {
            return null;
        }
        try {

            String sql = "SELECT count(*) as total, " + DBConstants.T_SUMMARY_STATUS + " FROM "
                    + DBConstants.T_SUMMARY_NAME + " WHERE " + DBConstants.T_SUMMARY_START_TIME + " > " + startDate;

            if (endDate > 0) {
                sql += " AND " + DBConstants.T_SUMMARY_START_TIME + " < " + endDate;
            }

            sql += " GROUP BY " + DBConstants.T_SUMMARY_STATUS;

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

    public ResultSet getSummaryInformationForTemplates(long startDate, long endDate) {

        // do a bit of validation here
        if (startDate < 0) {
            return null;
        }

        try {

            String sql = "SELECT count(*) as total, " + DBConstants.T_SUMMARY_STATUS + ", "
                    + DBConstants.T_SUMMARY_TEMPLATE_ID + " FROM " + DBConstants.T_SUMMARY_NAME + " WHERE "
                    + DBConstants.T_SUMMARY_START_TIME + " > " + startDate;

            if (endDate > 0) {
                sql += " AND " + DBConstants.T_SUMMARY_START_TIME + " < " + endDate;
            }

            sql += " GROUP BY " + DBConstants.T_SUMMARY_TEMPLATE_ID + ", " + DBConstants.T_SUMMARY_STATUS;

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

    /**
     * There can be workflows successful now, but those might be failed earlier and recovered. We need to find why those
     * failed earlier to diagnose errors in the system. This will retrieve all the workflows with the current status set
     * to SUCCESSFUL but has faults registered in faults table.
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public ResultSet getPreviouslyFailedSuccessfulInstances(long startDate, long endDate) {
        String sql = "SELECT summary." + DBConstants.T_SUMMARY_WORKFLOW_ID + " FROM " + DBConstants.T_SUMMARY_NAME
                + " summary, " + DBConstants.T_FAULTS_NAME + " faults " + " WHERE " + DBConstants.T_SUMMARY_START_TIME
                + " > " + startDate + " AND summary." + DBConstants.T_SUMMARY_WORKFLOW_ID + "=faults."
                + DBConstants.T_FAULTS_WORKFLOW_ID + " AND summary." + DBConstants.T_SUMMARY_STATUS + "='SUCCESSFUL' ";

        if (endDate > 0) {
            sql += " AND " + DBConstants.T_SUMMARY_START_TIME + " < " + endDate;
        }

        try {

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }

    }

    /**
     * There can be workflows successful now, but those might be failed earlier and recovered. We need to find why those
     * failed earlier to diagnose errors in the system. This will retrieve all the workflows with the current status set
     * to SUCCESSFUL but has faults registered in faults table.
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public ResultSet getPreviouslyFailedSuccessfulInstances(String username) {
        String sql = "SELECT summary." + DBConstants.T_SUMMARY_WORKFLOW_ID + " FROM " + DBConstants.T_SUMMARY_NAME
                + " summary, " + DBConstants.T_FAULTS_NAME + " faults " + " WHERE " + DBConstants.T_SUMMARY_USERNAME
                + " = '" + username + "' " + " AND summary." + DBConstants.T_SUMMARY_WORKFLOW_ID + "=faults."
                + DBConstants.T_FAULTS_WORKFLOW_ID + " AND summary." + DBConstants.T_SUMMARY_STATUS + "='SUCCESSFUL' ";
        try {

            connection = database.connect();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        }

    }
}
