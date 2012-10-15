<%@ page import="java.io.*" %>
<%@ page import="org.sample.airavata.api.*" %>
<%@ page import="org.apache.xmlbeans.SchemaType" %>
<%@ page import="org.apache.airavata.schemas.gfac.GlobusHostType" %>

<html>
<head>
    <title>Application registered in Airavata</title>
</head>

<body>

<br>
<%
    String hostName = request.getParameter("hostName");

    String hostType = request.getParameter("hostType");
    String hostAddress = request.getParameter("hostAddress");
    String hostEndpoint = request.getParameter("hostEndpoint");
    String gateKeeperEndpoint = request.getParameter("gatekeeperEndpoint");

    try {
        DescriptorRegistrationSample.registerHost(
                new HostDescriptorBean(hostType, hostName, hostAddress,
                        hostEndpoint, gateKeeperEndpoint));
    } catch (Exception e) {
        e.printStackTrace();
//                  TODO alert()
    }

    System.out.println("Host Registration was successful");
%>
out.print("Registration DONE!");

</body>
</html>