<%@ page import="org.apache.airavata.client.api.AiravataAPIInvocationException" %>
<%@ page import="org.sample.airavata.api.WorkflowExecutionSample" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.net.URI" %>
<%@ page import="java.util.Properties" %>

<%! URI eventingServiceURL; %>
<%! URI messageBoxServiceURL; %>
<%! URI registryRMIURI; %>
<%! URI gFaCURL; %>
<%! URI workflowInterpreterServiceURL; %>

<html>
    <head>
        <title>Airavata Server Information</title>
    </head>

    <body>
        <h1>Airavata Service URLs</h1>

        <br>
        <%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            String username = props.getProperty("jcr.username");
            String password = props.getProperty("jcr.password");
            String registryURL = props.getProperty("jcr.url");

            try {
                eventingServiceURL = WorkflowExecutionSample.getEventingServiceURL(username, password, registryURL);
                messageBoxServiceURL = WorkflowExecutionSample.getMessageBoxServiceURL(username, password, registryURL);
                registryRMIURI = WorkflowExecutionSample.getRegistryURL(username, password, registryURL);
                gFaCURL = WorkflowExecutionSample.getGFaCURL(username, password, registryURL);
                workflowInterpreterServiceURL = WorkflowExecutionSample.getWorkflowInterpreterServiceURL(username, password, registryURL);

            } catch (AiravataAPIInvocationException e) {
                e.printStackTrace();
//             TODO alert(e)
            }

        %>
            <table border="1">
                <tr><td>Eventing Service URL                </td><td> <%=eventingServiceURL%></td></tr>
                <tr><td>MessageBox Service URL              </td><td> <%=messageBoxServiceURL%></td></tr>
                <tr><td>Registry URL                        </td><td> <%=registryRMIURI%></td></tr>
                <tr><td>GFac URL                            </td><td> <%=gFaCURL%></td></tr>
                <tr><td>Workflow Interpreter Service URL    </td><td> <%=workflowInterpreterServiceURL%></td></tr>
            </table>
    </body>
</html>