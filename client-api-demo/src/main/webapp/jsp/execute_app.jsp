<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Properties" %>
<%! String username;%>
<%! String password;%>
<%! String registryURL;%>
<%! String certDirectory;%>

<html>
    <head>
        <title>Execute an Application</title>
    </head>

    <%
        InputStream stream = application.getResourceAsStream("/deployment.properties");
        Properties props = new Properties();
        props.load(stream);

        username = props.getProperty("jcr.username");
        password = props.getProperty("jcr.password");
        registryURL = props.getProperty("jcr.url");
        certDirectory = props.getProperty("ca.certificates.directory");
    %>

    <body>
        <h1>Execute an Application</h1>
        <form action="execute_app_result.jsp" method="POST">

            <table border="0">
                <tr><td>Service Name            </td><td> <input type="text" name="serviceName" value="SimpleEcho" size="50"></td></tr>
                <tr><td>Input Name              </td><td> <input type="text" name="inputName" value="echo_input" size="50"></td></tr>
                <tr><td>Output Name             </td><td> <input type="text" name="outputName" value="echo_output" size="50"></td></tr>
                <tr><td>Input Value             </td><td> <input type="text" name="inputValue" value="echo_output=hello" size="50"></td></tr>
            </table>

            <input type="hidden" name="userName" value="<%=username%>">
            <input type="hidden" name="password" value="<%=password%>">
            <input type="hidden" name="registryURL" value="<%=registryURL%>">
            <input type="hidden" name="certLocation" value="<%=certDirectory%>">

            <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">
        </form>
    </body>
</html>