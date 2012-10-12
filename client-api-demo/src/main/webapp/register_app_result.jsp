<%@ page import="java.io.*" %>
<%@ page import="org.sample.airavata.api.*" %>
<%@ page import="org.apache.xmlbeans.SchemaType" %>
<%@ page import="org.apache.airavata.schemas.gfac.GlobusHostType" %>


    <%--<script type="text/javascript">
        <%
        /*
        * Code to read from DB or other source, or whatever you need.
        * Say, for example, the string "hello world" is returned, and
        * set to the message variable
        */
        message = "hello world";
        %>
        var msg = "<%=message%>";
        alert(msg);
    </script>--%>

<html>
    <head>
        <title>Application registered in Airavata</title>
    </head>

    <body>

        <br>
        <%
            String username = request.getParameter("userName");
            String password = request.getParameter("password");
            String registryURL = request.getParameter("registryURL");
            String hostName = request.getParameter("hostName");

            String type = request.getParameter("hostType");
            SchemaType hostType = null;
            if ("globus".equalsIgnoreCase(type)) {
                hostType = GlobusHostType.type;
            }
            String hostAddress = request.getParameter("hostAddress");
            String hostEndpoint = request.getParameter("hostEndpoint");
            String gatekeeperEndpoint = request.getParameter("gatekeeperEndpoint");

            String applicationName = request.getParameter("appName");
            String executable = request.getParameter("exeuctableLocation");
            String scratchWorkingDir = request.getParameter("scratchWorkingDirectory");
            String projectAccNumber = request.getParameter("projAccNumber");

            String queueName = request.getParameter("queueName");
            int cpuCount = Integer.parseInt(request.getParameter("cpuCount"));
            int nodeCount = Integer.parseInt(request.getParameter("nodeCount"));
            int maxMemory = Integer.parseInt(request.getParameter("maxMemory"));

            String serviceName = request.getParameter("serviceName");
            String inputName = request.getParameter("inputName");
            String inputType = request.getParameter("inputType");
            String outputName = request.getParameter("outputName");
            String outputType = request.getParameter("outputType");

            try {
                DescriptorRegistrationSample.registerApplication(
                        new AppDescriptorBean(username, password, registryURL, hostType, hostName, hostAddress,
                                hostEndpoint, gatekeeperEndpoint, applicationName, executable, scratchWorkingDir,
                                projectAccNumber, queueName, cpuCount, nodeCount, maxMemory, serviceName, inputName,
                                outputName, inputType, outputType));
            } catch (Exception e) {
                e.printStackTrace();
//                  TODO alert()
            }

            System.out.println("Registration was successful");

            System.out.println("Registration DONE!");
        %>

    </body>
</html>