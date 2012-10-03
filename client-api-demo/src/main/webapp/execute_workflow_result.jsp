<%@ page import="java.io.*" %>
<%@ page import="org.sample.airavata.api.*" %>
<%@ page import="org.apache.xmlbeans.SchemaType" %>
<%@ page import="org.apache.airavata.schemas.gfac.GlobusHostType" %>
<%@ page import="java.net.URI" %>
<%@ page import="org.apache.airavata.client.api.AiravataAPIInvocationException" %>
<%@ page import="org.apache.airavata.client.api.AiravataAPI" %>
<%@ page import="org.apache.airavata.workflow.model.wf.WorkflowInput" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<html>
    <head>
        <title>Airavata Server Workflow Execution</title>
    </head>

    <body>
        <h1>Workflow Result</h1>

        <br>
        <%
            String  username = request.getParameter("userName");
            String password = request.getParameter("password");
            String registryURL = request.getParameter("registryURL");
            AiravataAPI airavataAPI = SampleUtil.getAiravataAPI(username, password, registryURL);

            String workflowName = request.getParameter("workflowName");
            String arguments = request.getParameter("arguments");
//            System.out.println("inputs received to execute: " + arguments);

            List<WorkflowInput> workflowInputs = new ArrayList<WorkflowInput>();
            String[] inputs = arguments.split(";");
            for (String input : inputs) {

                String[] param = input.split(",");
                for (String aParam : param) {
//                    public WorkflowInput(String name,String type,Object defaultValue,Object value, boolean optional)
                    String name = param[0];
                    String type = param[1];
                    Object value = null;

                    if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
                        value = Integer.parseInt(request.getParameter(name));
                    } else if (type.equalsIgnoreCase("float")) {
                        value = Integer.parseInt(request.getParameter(name));
                    } else if (type.equalsIgnoreCase("double")) {
                        value = Integer.parseInt(request.getParameter(name));
                    } else if (type.equalsIgnoreCase("String")) {
                        value = request.getParameter(name);
                    }

//                    System.out.println("Creating workflow input ...");
//                    System.out.println("input Name  : " + name);
//                    System.out.println("input Type : " + type);
//                    System.out.println("input Value : " + value);
                    WorkflowInput workflowInput = new WorkflowInput(name, type, null, value, false);
                    workflowInputs.add(workflowInput);
                }
            }

            String result = null;
            try {
                // String workflowTemplateId,List<WorkflowInput> inputs, String user, String metadata, String workflowInstanceName
                result = airavataAPI.getExecutionManager().runExperiment(workflowName, workflowInputs, "admin", "", workflowName);
            } catch (AiravataAPIInvocationException e) {
                e.printStackTrace();
//                  TODO alert(e)
            }
            System.out.println("The result is : " + result);
            out.println("Result : " + result);

        %>


    </body>
</html>




