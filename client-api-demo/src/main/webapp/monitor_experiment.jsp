<%@ page import="org.apache.airavata.client.api.AiravataAPI" %>
<%@ page import="org.apache.airavata.client.api.AiravataAPIInvocationException" %>
<%@ page import="org.apache.airavata.client.api.ProvenanceManager" %>
<%@ page import="org.sample.airavata.api.SampleUtil" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Properties" %>

<%! String username;%>
<%! String password;%>
<%! String registryURL;%>

<html>
    <head>
        <title>Airavata Experiment Information</title>
    </head>

        <body>
        <h1>Saved Experiments</h1>

        <br>
        <%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            username = props.getProperty("jcr.username");
            password = props.getProperty("jcr.password");
            registryURL = props.getProperty("jcr.url");

            AiravataAPI airavataAPI = SampleUtil.getAiravataAPI(username, password, registryURL);
            ProvenanceManager provenanceManager = airavataAPI.getProvenanceManager();
            List<String> experimentsIDs = null;
            try {
                experimentsIDs = provenanceManager.getExperiments(airavataAPI.getCurrentUser());
            } catch (AiravataAPIInvocationException e) {
                e.printStackTrace();
                // TODO :add alert
            }

        %>

        <%--<form action="monitor_experiment_result.jsp" method="POST">--%>

            <table border="1">
                <tr>
                    <th>Experiment ID           </th>
                    <th>Status                  </th>
                    <th>Detailed Information    </th>
                </tr>

                <%
                    String status = "DEFAULT";
                    if (experimentsIDs != null) {
                        for(String experimentID: experimentsIDs) {
                            try {
                        if (provenanceManager.getWorkflowInstanceStatus(experimentID, experimentID).getExecutionStatus()!=null) {
                            status = provenanceManager.getWorkflowInstanceStatus(experimentID, experimentID).
                                    getExecutionStatus().toString();
                        } else {
                            status = "";
                        }

                    } catch (AiravataAPIInvocationException e) {
                        e.printStackTrace();
                        // TODO :add alert
            }
                %>
                            <form action="monitor_experiment_result.jsp" method="POST">
                                <tr><td><%=experimentID%></td>
                                    <td><%=status%></td>
                                    <input type="hidden" name="experimentID" value="<%=experimentID%>">
                                    <td><input type="SUBMIT" value="VIEW"></td>
                                </tr>
                            </form>

                <%
                        }
                    }
                %>

            </table>

        <%--</form>--%>

        <%--<h1>Detailed View of Experiment</h1>
        <form action="monitor_experiment_result.jsp" method="POST">

            <table border="0">
                &lt;%&ndash;<tr><td>experimentID      </td><td> <input type="text" name="experimentID" value="<%=experiment%>" size="50"></td></tr>&ndash;%&gt;
                <tr>
                    <td>experimentID      </td>
                    <td><select name="experimentID">
                        <%
                            for (String id : experimentsIDs) {
                        %>

                        <option value="<%=id%>"><%=id%>
                        </option>
                        <%
                            }
                        %>
                    </select>
                   </td>
                </tr>
            </table>

            <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">
        </form>--%>

    </body>
</html>