<%@ page import="org.sample.airavata.api.DescriptorRegistrationSample" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>

<html>
    <head>
        <title>Application Execution in Airavata</title>
    </head>

    <body>

        <%
            InputStream stream = application.getResourceAsStream("/deployment.properties");
            Properties props = new Properties();
            props.load(stream);

            final String myProxyServer = props.getProperty("myproxy.server");
            final String myProxyUsername = props.getProperty("myproxy.username");
            final String myProxyPassword = props.getProperty("myproxy.password");

//            System.out.println("My proxy server   : " + myProxyServer);
//            System.out.println("My proxy username : " + myProxyUsername);
//            System.out.println("My proxy password : " + myProxyPassword);
            final String trustedCertLoc = request.getParameter("certLocation");


            final String serviceName = request.getParameter("serviceName");
            final String inputName = request.getParameter("inputName");
            final String outputName = request.getParameter("outputName");
            final String inputValue = request.getParameter("inputValue");

            try {

                new Thread() {
                    public void run() {
                        DescriptorRegistrationSample.execute(trustedCertLoc,
                        serviceName, inputName, inputValue, outputName, myProxyServer, myProxyUsername, myProxyPassword);
                    }

                }.start();

            } catch (Exception e) {
                e.printStackTrace();
//                  TODO alert(e)
            }

            System.out.println("Job submitted");


        %>

    </body>
</html>