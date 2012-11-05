<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Properties" %>

<%! String username;%>
<%! String password;%>
<%! String registryURL;%>

<html>
<head>
    <title>Save Application</title>
</head>

<body>

<h1>Register an Application</h1>
<form action="register_host_result.jsp" method="POST">

    <table border="0">
        <b>HOST</b>
        <tr><td>Host Type           </td><td> <input type="text" name="hostType" value="globus" size="50"></td></tr>
        <tr><td>Host Name           </td><td> <input type="text" name="hostName" value="gram" size="50"></td></tr>
        <tr><td>Host Address        </td><td> <input type="text" name="hostAddress" value="gatekeeper2.ranger.tacc.teragrid.org" size="50"></td></tr>
        <tr><td>Host Endpoint       </td><td> <input type="text" name="hostEndpoint" value="gsiftp://gridftp.ranger.tacc.teragrid.org:2811/" size="50"></td></tr>
        <tr><td>Gatekeeper Endpoint </td><td> <input type="text" name="gatekeeperEndpoint" value="gatekeeper.ranger.tacc.teragrid.org:2119/jobmanager-sge" size="50"></td></tr>
    </table>
    <br>

    <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">

</form>
</body>
</html>