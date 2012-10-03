<%@ page import="org.apache.airavata.client.api.AiravataAPI" %>
<%@ page import="org.apache.airavata.workflow.model.wf.WorkflowInput" %>
<%@ page import="org.sample.airavata.api.SampleUtil" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Properties" %>

<%! String username;%>
<%! String password;%>
<%! String registryURL;%>
<%! String workflowName;%>
<%! String arguments ="";%>

<html>
    <head>
        <title>Execute Workflow</title>
    </head>

    <%
        workflowName = request.getParameter("workflowName");

        InputStream stream = application.getResourceAsStream("/deployment.properties");
        Properties props = new Properties();
        props.load(stream);

        username = props.getProperty("jcr.username");
        password = props.getProperty("jcr.password");
        registryURL = props.getProperty("jcr.url");

        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI(username, password, registryURL);

        List<WorkflowInput> workflowInputs = null;
        try {
            workflowInputs = airavataAPI.getWorkflowManager().getWorkflow(workflowName).getWorkflowInputs();
        } catch (Exception e) {
            e.printStackTrace();
        }

    %>

        <body>
        <h1>Insert inputs to execute <%=workflowName%></h1>
        <form action="execute_workflow_result.jsp" method="POST">

            <table border="0">
                <tr><th>Input Name           </th><th>Type          </th><th>Value          </th> </tr>

    <%
        if (workflowInputs!=null) {
            for (WorkflowInput workflowInput : workflowInputs) {
                arguments = arguments + workflowInput.getName() + "," +
                        workflowInput.getType() + "," + workflowInput.getDefaultValue() + ";";
    %>

                <tr><td><%=workflowInput.getName()%>            </td><td><%=workflowInput.getType()%></td><td> <input type="text" name="<%=workflowInput.getName()%>" value="<%=workflowInput.getDefaultValue()%>" size="50"></td></tr>

                <%
                        }
                    }
                %>

            </table>

            <input type="hidden" name="arguments" value="<%=arguments%>">
            <input type="hidden" name="workflowName" value="<%=workflowName%>">
            <input type="hidden" name="username" value="<%=username%>">
            <input type="hidden" name="password" value="<%=password%>">
            <input type="hidden" name="registryURL" value="<%=registryURL%>">


            <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">
        </form>

                <%
                    arguments = "";
                    System.out.println("Resetting arguments Arguments : " + arguments);
                %>
    </body>
</html>