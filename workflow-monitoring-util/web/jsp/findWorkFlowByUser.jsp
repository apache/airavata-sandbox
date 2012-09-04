<%--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.QueryManager" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.WorkflowMonitoringException" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.bean.WorkflowInfo" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.db.DBConstants" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.util.Util" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title> :: Workflow Information :: </title>
</head>
<body>

<%
    try {


        String username = request.getParameter("username");

        QueryManager queryManager = new QueryManager();
        ResultSet workflowData = queryManager.getWorkflowsOfUser(username);
%>

<h2 align="center">Workflow Information for <%=username%>
</h2>

<br/>
<br/>
<br/>

<table cellspacing="1" cellpadding="3" border="0" width="100%">
    <tr>
        <th bgcolor="#4682B4"><font color="#ffffff">Wokflow Id</font></th>
        <th bgcolor="#4682B4"><font color="#ffffff">Template Id</font></th>
        <th bgcolor="#4682B4"><font color="#ffffff">Username</font></th>
        <th bgcolor="#4682B4"><font color="#ffffff">Status</font></th>
        <th bgcolor="#4682B4"><font color="#ffffff">Start Time</font></th>
        <th bgcolor="#4682B4"><font color="#ffffff">End Time</font></th>
    </tr>
    <%


        if (workflowData == null) {
    %>
    <h3> No worflow information available for this user </h3>
    <%

    } else {

        String endTime;

        Map<String, WorkflowInfo> workflowInfoObjects = new HashMap<String, WorkflowInfo>();

        while (workflowData.next()) {
            WorkflowInfo workflow = new WorkflowInfo();
            workflow.setStatus(workflowData.getString(DBConstants.T_SUMMARY_STATUS));
            workflow.setTemplateId(workflowData.getString(DBConstants.T_SUMMARY_TEMPLATE_ID));
            workflow.setUsername(workflowData.getString(DBConstants.T_SUMMARY_USERNAME));
            String workflowId = workflowData.getString(DBConstants.T_SUMMARY_WORKFLOW_ID);
            workflow.setWorkflowId(workflowId);
            workflow.setStartTime(Util.getFormattedDateFromLongValue(workflowData.getString(DBConstants.T_SUMMARY_START_TIME)));

            endTime = workflowData.getString(DBConstants.T_SUMMARY_END_TIME);
            if (endTime != null && !"".equals(endTime) && !"0".equals(endTime.trim())) {
                endTime = Util.getFormattedDateFromLongValue(endTime);
            } else {
                endTime = "";
            }
            workflow.setEndTime(endTime);
            workflow.setFaultsAvailable("FAILED".equalsIgnoreCase(workflow.getStatus()));
            workflowInfoObjects.put(workflowId, workflow);
        }

        ResultSet resultSet = queryManager.getPreviouslyFailedSuccessfulInstances(username);

        if (resultSet != null) {
            while (resultSet.next()) {
                String workflowId = resultSet.getString(DBConstants.T_SUMMARY_WORKFLOW_ID);
                if (workflowInfoObjects.containsKey(workflowId)) {
                    workflowInfoObjects.get(workflowId).setFaultsAvailable(true);
                }
            }
        }

        queryManager.close();
        

        for (WorkflowInfo workflowInfo : workflowInfoObjects.values()) {
    %>
    <tr bgcolor="#dbeaf5">

        <td>
            <%
                if (workflowInfo.isFaultsAvailable()) {
            %>
            <a href="findWorkFlowById.jsp?workflowId=<%=workflowInfo.getWorkflowId()%>"><%=workflowInfo.getWorkflowId()%>
            </a>
            <%
            } else {
            %>
            <%=workflowInfo.getWorkflowId()%>
            <%
                }
            %>
        </td>
        <td><%=Util.extractShortTemplateId(workflowInfo.getTemplateId())%>
        </td>
        <td><%=Util.extractUsername(workflowInfo.getUsername())%>
        </td>
        <td><%=workflowInfo.getStatus()%>
        </td>
        <td><%=workflowInfo.getStartTime()%>
        </td>
        <td><%=workflowInfo.getEndTime()%>
        </td>
    </tr>
    <%
            }
        }
    } catch (SQLException e) {
                e.printStackTrace();
    %>
    <h4>An error occurred while retreiving data. Please retry later.</h4>
    <%
    } catch (WorkflowMonitoringException e) {
        e.printStackTrace();
    %>
    <h4>An error occurred while parsing input data. Make sure all the dates are in mm/dd/yyyy HH.mm format.</h4>
    <%
        }

    %>
</table>
</body>
</html>