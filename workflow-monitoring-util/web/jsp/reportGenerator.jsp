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
<%@ page import="org.apache.airavata.tools.workflow.monitoring.bean.TemplateInfoBean" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.db.DBConstants" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.airavata.tools.workflow.monitoring.util.Util" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>:: Reports ::</title></head>
<body>
<h2 align="center">Lead Monitoring System Reports</h2>


<%
    try {

        String startTimeString = request.getParameter("startTime");

        long startDate = Util.getTime(startTimeString);

        String endTimeString = request.getParameter("endTime");
        long endDate = -1;
        if (endTimeString != null && !"".equals(endTimeString)) {
            endDate = Util.getTime(endTimeString);
        }

        // first get the summary report

        // Here I need to get the total number of entries in this queries. I can do it either by executing another query
        // or counting the number of entries in this query.
        // I follow the second option and store the date in a temporary location (HashMap). Having claculated the total number
        // I display the results
        QueryManager queryManager = new QueryManager();
        ResultSet summaryData = queryManager.getSummaryInformation(startDate, endDate);

        HashMap<String, Integer> summaryInformation = new HashMap<String, Integer>();
        long totalCount = 0;
        int statusCount = 0;
        while (summaryData.next()) {
            statusCount = summaryData.getInt("total");
            totalCount += statusCount;

            summaryInformation.put(summaryData.getString(DBConstants.T_SUMMARY_STATUS), statusCount);
        }

        summaryData.close();

        // ok now I am ready to output the summary details
        if (totalCount > 0) {
%>
<table cellspacing="1" cellpadding="3" border="0" width="40%">
    <tr>
        <th bgcolor="#4682B4"><font color="#ffffff">Total WorkFlows</font></th>
        <th colspan="2" align="center" bgcolor="#dbeaf5"><%=totalCount%>
        </th>
        <td/>
    </tr>

    <%
        Iterator<String> summaryInfoIter = summaryInformation.keySet().iterator();
        DecimalFormat format = new DecimalFormat("##.00");
        while (summaryInfoIter.hasNext()) {
            String statusName = summaryInfoIter.next();
            int count = summaryInformation.get(statusName);
    %>
    <tr bgcolor="#dbeaf5">
        <th align="left" width="22%"><%=statusName%>
        </th>
        <td align="right" width="10%"><%=count%>
        </td>

        <td align="right" width="5%"><%=format.format(count * 100.00 / totalCount)%>%</td>
    </tr>
    <%
        }
    %>

</table>

<%
    // summary information are done now. Now it is the time to look at information grouped by template ID, if the user
    // had asked for it.
    String groupByParam = request.getParameter("groupByTemplateId");
    if (groupByParam != null && !"".equals(groupByParam) && groupByParam.equalsIgnoreCase("yes")) {
        ResultSet workflowData = queryManager.getSummaryInformationForTemplates(startDate, endDate);


        ArrayList<TemplateInfoBean> templateBeans = new ArrayList<TemplateInfoBean>();
        TemplateInfoBean bean = null;
        String currentTemplateId = "";

        // I'm gonna do a small trick here. Since the templates are coming in order, as the sql is grouped by template ID,
        // I will keep a reference to TemplateInfoBean and template id created in the previous iteration. If the current
        // template id is same as the earlier one, the the query is giving info about an existing bean. Else create
        // a new bean.
        while (workflowData.next()) {
            String templateID = workflowData.getString(DBConstants.T_SUMMARY_TEMPLATE_ID);
            if (!currentTemplateId.equals(templateID)) {
                currentTemplateId = templateID;
                bean = new TemplateInfoBean(templateID);
                templateBeans.add(bean);
            }

            bean.addStatusInfo(workflowData.getString(DBConstants.T_SUMMARY_STATUS), workflowData.getInt("total"));
        }
        queryManager.close();

        // now let's present the information we have


%>

<h3>Report on Workflow Templates</h3>
<table cellspacing="1" cellpadding="3" border="0" width="80%">
    <tr>
        <th align="center" bgcolor="#4682B4"><font color="#ffffff">Template Id</font></th>
        <td align="center" bgcolor="#4682B4"><font color="#ffffff">Status</font></td>
        <td align="center" bgcolor="#4682B4"><font color="#ffffff">Count</font></td>
        <td align="center" bgcolor="#4682B4"><font color="#ffffff">Percentage</font></td>
        <td/>
    </tr>

    <%

        for (int i = 0; i < templateBeans.size(); i++) {
            TemplateInfoBean infoBean = templateBeans.get(i);

            List<String> statsusNames = infoBean.getAllStatusNames();
            List<Long> statusCounts = infoBean.getAllStatusCounts();

            long totalStatusCount = infoBean.getTotalCount();
    %>
    <tr>
        <th align="left" bgcolor="#dbeaf5" rowspan="<%=statsusNames.size()%>"><%=infoBean.getTemplateID()%>
        </th>
        <!--<td align="right" colspan="3">-->

        <!--<table border="1" cellpadding="5">-->
        <%
            for (int j = 0; j < statsusNames.size(); j++) {
                long count = statusCounts.get(j);
                if (j > 0) {
        %>
        <tr bgcolor="#dbeaf5"><%
            }
        %>

            <td align="center"><%=statsusNames.get(j)%>
            </td>
            <td align="right"><%=count%>
            </td>
            <td align="right"><%=format.format(count * 100.00 / totalStatusCount)%>%</td>
        </tr>
        <%
            }
        %>

    </tr>
    <%
        }
    %>

</table>


<%
    }

} else {
%>
<p>There are no information available at this time about the Wokflows</p>
<%
    }


} catch (WorkflowMonitoringException e) {
%>
<h4>An error occurred while retreiving data. Please retry later.</h4>
<%
} catch (SQLException e) {
%>
<h4>An error occurred while retreiving data. Please retry later.</h4>
<%
    }

%>

</body>
</html>