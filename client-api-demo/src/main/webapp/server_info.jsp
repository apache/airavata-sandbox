<html>
    <head>
        <title>Airavata Server Information</title>
    </head>

    <body>
        <h1>Get Airavata Server information</h1>
        <form action="server_info_result.jsp" method="POST">

            <table border="0">
                <tr><td>User Name           </td><td> <input type="text" name="userName" value="admin" size="50"></td></tr>
                <tr><td>Password            </td><td> <input type="text" name="password" value="admin" size="50"></td></tr>
                <tr><td>Registry URL (RMI)  </td><td> <input type="text" name="registryURL" value="http://localhost:8090/jackrabbit-webapp-2.4.0/rmi" size="50"></td></tr>
            </table>

            <input type="SUBMIT" value="Submit"><input type="RESET" value="Reset">
        </form>
    </body>
</html>