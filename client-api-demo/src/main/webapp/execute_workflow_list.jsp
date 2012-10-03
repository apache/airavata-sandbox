<%@ page import="org.apache.airavata.client.api.AiravataAPI" %>
<%@ page import="org.apache.airavata.client.api.AiravataAPIInvocationException" %>
<%@ page import="org.apache.airavata.workflow.model.wf.Workflow" %>
<%@ page import="org.sample.airavata.api.SampleUtil" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Properties" %>

<%! String username;%>
<%! String password;%>
<%! String registryURL;%>

<html>
    <head>
        <title>Airavata Workflow Information</title>
    </head>

        <body>
        <h1>Saved Workflows</h1>

        <br>
        <%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            username = props.getProperty("jcr.username");
            password = props.getProperty("jcr.password");
            registryURL = props.getProperty("jcr.url");

            AiravataAPI airavataAPI = SampleUtil.getAiravataAPI(username, password, registryURL);
            List<Workflow> workflows = null;
            try {
                workflows = airavataAPI.getWorkflowManager().getWorkflows();
            } catch (AiravataAPIInvocationException e) {
                e.printStackTrace();
            }
        %>

            <table border="1">
                <tr><th>Workflow Name                </th><th>Description</th> <th>Execute Workflow</th></tr>

                <%
                    if(workflows!=null) {
                        for(Workflow w : workflows) {
                %>

                <tr>
                    <form action="execute_workflow_run.jsp" method="POST">
                        <td><%=w.getName() %></td>
                        <td><%=w.getDescription()%></td>
                        <input type="hidden" name="workflowName" value="<%=w.getName()%>">
                        <td><input type="SUBMIT" value="RUN"><%--<input type="RESET" value="Reset">--%></td>
                    </form>
                </tr>

                <%
                        }
                    }
                %>

            </table>

    </body>
</html>