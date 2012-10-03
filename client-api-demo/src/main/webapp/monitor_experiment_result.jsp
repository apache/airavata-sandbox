<%@ page import="org.apache.airavata.client.api.AiravataAPI" %>
<%@ page import="org.apache.airavata.client.api.ProvenanceManager" %>
<%@ page import="org.apache.airavata.registry.api.workflow.WorkflowInstanceData" %>
<%@ page import="org.apache.airavata.registry.api.workflow.WorkflowInstanceNodeData" %>
<%@ page import="org.apache.airavata.registry.api.workflow.WorkflowInstanceNodePortData" %>
<%@ page import="org.apache.airavata.xbaya.interpretor.NameValue" %>
<%@ page import="org.apache.airavata.xbaya.util.XBayaUtil" %>
<%@ page import="org.sample.airavata.api.SampleUtil" %>
<%@ page import="org.xml.sax.SAXException" %>
<%@ page import="javax.xml.parsers.ParserConfigurationException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Properties" %>

<html>
<head>
    <title>Airavata Server Experiments Results<<</title>
</head>

<body>
<h1>Experiment result</h1>

<br>
<%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            String username = props.getProperty("jcr.username");
            String password = props.getProperty("jcr.password");
            String registryURL = props.getProperty("jcr.url");

            AiravataAPI airavataAPI = SampleUtil.getAiravataAPI(username, password, registryURL);
            ProvenanceManager provenanceManager = airavataAPI.getProvenanceManager();

            String experimentID = request.getParameter("experimentID");

            WorkflowInstanceData workflowInstanceData = provenanceManager.getWorkflowInstanceData(experimentID, experimentID);
            String topicId = workflowInstanceData.getTopicId();
            String user = workflowInstanceData.getUser();
            String workflowName = workflowInstanceData.getWorkflowName();
            String experimentId1 = workflowInstanceData.getExperimentId();
            String metadata = workflowInstanceData.getMetadata();
            String status = null;
            if(workflowInstanceData.getStatus()!=null) {
                status = workflowInstanceData.getStatus().toString();
            }

//            System.out.println("Topic id      : " + topicId);
//            System.out.println("User          : " + user);
//            System.out.println("Workflow Name : " + workflowName);
//            System.out.println("Experiment ID : " + experimentID);
//            System.out.println("metadata      : " + metadata);
//            System.out.println("status        : " + status);

            %>

            <table border="1">
                <tr><td>Topic ID          </td><td> <%=topicId%></td></tr>
                <tr><td>User              </td><td> <%=user%></td></tr>
                <%--<tr><td>Workflow Name     </td><td> <%=workflowName%></td></tr>--%>
                <tr><td>Experiment ID     </td><td> <%=experimentID%></td></tr>
                <%--<tr><td>Metadata          </td><td> <%=metadata%></td></tr>--%>
                <tr><td>Status            </td><td> <%=status%></td></tr>
            </table>

            <br>
            Workflow Inputs :
            <table border="1">
                <tr><th>Name              </th><th> Value</th></tr>

                <%

                    for (WorkflowInstanceNodeData workflowInstanceNodeData : workflowInstanceData.getNodeDataList()) {
                        String nodeId = workflowInstanceNodeData.getWorkflowInstanceNode().getNodeId();

//                        System.out.println("Node ID : " + nodeId);

                        WorkflowInstanceNodeData nodeData = workflowInstanceData.getNodeData(nodeId);
                        List<WorkflowInstanceNodePortData> inputData = nodeData.getInputData();

                        for (WorkflowInstanceNodePortData workflowInstanceNodePortData : inputData) {
                            List<NameValue> ioParameterData = null;
                            try {
                                ioParameterData = XBayaUtil.getIOParameterData(workflowInstanceNodePortData.getValue());
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                                //TODO : alert
                            } catch (SAXException e) {
                                e.printStackTrace();
                            }

                            for (NameValue next : ioParameterData) {
                                String inputName = next.getName();
                                String inputValue = next.getValue();
//                                System.out.println("input name : " + inputName);
//                                System.out.println("input value : " + inputValue);

                %>
                <tr>
                    <td><%=inputName%>
                    </td>
                    <td><%=inputValue%>
                    </td>
                </tr>
                <%

                            }

                        }
                    }

                %>

            </table>

    </body>
</html>




