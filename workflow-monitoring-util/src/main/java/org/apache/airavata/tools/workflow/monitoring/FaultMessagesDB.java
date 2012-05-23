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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.airavata.tools.workflow.monitoring.db.JdbcStorage;

public class FaultMessagesDB {

    private static final String TABLE_NAME = "faults";

    private static final String SQL_INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + " (xml, workflowid) "
            + "VALUES(?,?)";

    private JdbcStorage db;

    public FaultMessagesDB(JdbcStorage db) {
        this.db = db;
    }

    public void insert(String workflowID, String message) {
        try {
            Connection connection = db.connect();
            PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_STATEMENT);
            byte[] buffer;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(message);
            buffer = output.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            stmt.setBinaryStream(1, in, buffer.length);
            stmt.setString(2, workflowID);
            db.insert(stmt);
            stmt.close();
            connection.commit();
            db.closeConnection(connection);
        } catch (SQLException e) {
            throw new WorkflowMonitoringException(e);
        } catch (IOException e) {
            throw new WorkflowMonitoringException(e);
        }
    }

}
