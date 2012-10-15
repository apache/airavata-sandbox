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
            try {
                eventingServiceURL = WorkflowExecutionSample.getEventingServiceURL();
                messageBoxServiceURL = WorkflowExecutionSample.getMessageBoxServiceURL();
                registryRMIURI = WorkflowExecutionSample.getRegistryURL();
                gFaCURL = WorkflowExecutionSample.getGFaCURL();
                workflowInterpreterServiceURL = WorkflowExecutionSample.getWorkflowInterpreterServiceURL();

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