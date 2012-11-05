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
        <%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            username = props.getProperty("jcr.username");
            password = props.getProperty("jcr.password");
            registryURL = props.getProperty("jcr.url");
        %>
        <h1>Register an Application</h1>
        <form action="register_app_result.jsp" method="POST">

            <table border="0">
                <b>HOST</b>
                <tr><td>Host Type           </td><td> <input type="text" name="hostType" value="globus" size="50"></td></tr>
                <tr><td>Host Name           </td><td> <input type="text" name="hostName" value="gram" size="50"></td></tr>
                <tr><td>Host Address        </td><td> <input type="text" name="hostAddress" value="gatekeeper2.ranger.tacc.teragrid.org" size="50"></td></tr>
                <tr><td>Host Endpoint       </td><td> <input type="text" name="hostEndpoint" value="gsiftp://gridftp.ranger.tacc.teragrid.org:2811/" size="50"></td></tr>
                <tr><td>Gatekeeper Endpoint </td><td> <input type="text" name="gatekeeperEndpoint" value="gatekeeper.ranger.tacc.teragrid.org:2119/jobmanager-sge" size="50"></td></tr>
            </table>
            <br>

            <%--<hr noshade size=7>--%>
            <table border="0">
                <b>APPLICATION</b>
                <tr><td>Application Name    </td><td> <input type="text" name="appName" value="EchoLocal" size="50"></td></tr>
                <tr><td>Executable location </td><td> <input type="text" name="exeuctableLocation" value="/bin/echo" size="50"></td></tr>
                <tr><td>Scratch Working Dir.</td><td> <input type="text" name="scratchWorkingDirectory" value="/scratch/01437/ogce/test" size="50"></td></tr>
                <tr><td>Project Acc. number </td><td> <input type="text" name="projAccNumber" value="TG-STA110014S" size="50"></td></tr>

                <tr><td>Queue Name          </td><td> <input type="text" name="queueName" value="development" size="50"></td></tr>
                <tr><td>CPU count           </td><td> <input type="text" name="cpuCount" value="1" size="50"></td></tr>
                <tr><td>Node count          </td><td> <input type="text" name="nodeCount" value="1" size="50"></td></tr>
                <tr><td>Max memory          </td><td> <input type="text" name="maxMemory" value="100" size="50"></td></tr>
            </table>
            <br>

            <%--<hr noshade size=7>--%>
            <table border="0">
                <b>SERVICE</b>
                <tr><td>Service Name        </td><td> <input type="text" name="serviceName" value="SimpleEcho" size="50"></td></tr>
                <tr><td>Input Name          </td><td> <input type="text" name="inputName" value="echo_input" size="50"></td></tr>
                <tr><td>Input Type          </td><td> <input type="text" name="inputType" value="String" size="50"></td></tr>
                <tr><td>Output Name         </td><td> <input type="text" name="outputName" value="echo_output" size="50"></td></tr>
                <tr><td>Output Type         </td><td> <input type="text" name="outputType" value="String" size="50"></td></tr>
            </table>

            <input type="hidden" name="userName" value="<%=username%>">
            <input type="hidden" name="password" value="<%=password%>">
            <input type="hidden" name="registryURL" value="<%=registryURL%>">


            <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">
        </form>
    </body>
</html>