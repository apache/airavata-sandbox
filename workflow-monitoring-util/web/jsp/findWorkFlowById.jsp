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
<%@ page import="java.io.InputStream" %>
<%@ page import="java.sql.Blob" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title> :: Workflow Information ::</title></head>
<body>
<% String workflowId = request.getParameter("workflowId");
    // proceed only if there is a workflow id
    if (workflowId != null && !"".equals(workflowId)) {

%>

<h2 align="center"> Wokflow information for the id <%= workflowId%>
</h2>
<%
    /**
     * This is how this will work. First I retrieve data from the databse for the given Wokflow id. The page will
     * first list message number with links to the messages. This was not to clutter the page with lot of data.
     * I first retrieve all the messages and store them in an array list. During the first iteration I print the links
     * to the messages as well.
     * After this, during the second iteration, I retrieve the stored messages and print them out with proper bookmark
     * tages
     */
    ArrayList messages = new ArrayList();
    QueryManager queryManager = new QueryManager();

    // try to retrieve data from the database for the given id
    ResultSet workflowData = null;
    int i;
    try {
        workflowData = queryManager.getDataRelatesToWorkflowId(workflowId);
%>
<table cellspacing="1" cellpadding="3" border="0" width="100%">
    <%

        int resultsCounter = 0;
        while (workflowData.next()) {

            // retrieving data from the database
            Blob xmlBlob = workflowData.getBlob("xml");

            InputStream in = xmlBlob.getBinaryStream();
            StringBuffer xmlMessage = new StringBuffer();

            int read;

            byte[] buf = new byte[1024];
            while ((read = in.read(buf)) > 0) {
                xmlMessage.append(new String(buf, 0, read));
            }
            in.close();

            // storing the retreived message
            messages.add(xmlMessage.toString());
    %>
    <!-- Adding links to the messages-->
    <tr>
        <td><a href="#message" <%= ++resultsCounter %> /> Message <%=resultsCounter%>
        </td>
    </tr>
    <%
        }
        queryManager.close();

    %>

</table>

<% if (messages.isEmpty()) {
%>
<h5> There are no fault messages for the Wokflow id <%= workflowId%>
</h5>
<%
} else {
%>
<table>
    <%
        // now let's display the messages here
        for (int j = 0; j < messages.size(); j++) {
    %>
    <tr bgcolor="#dbeaf5">
        <td><a name="#message" <%=j%> /> <%=messages.get(j) %>
        </td>
    </tr>

    <% }
    %>
</table>
<%
    }
} catch (WorkflowMonitoringException e) {
    e.printStackTrace();
%>
<p>An error occurred while processing this request</p>
<%

    }
%>


<%
} else {
%>

<h3> There is no information for the workflow id <%=workflowId%>
</h3>
<%
    }
%>

</body>
</html>