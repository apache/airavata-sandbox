package org.apache.airavata.credential.store.servlet;

import edu.uiuc.ncsa.myproxy.oa4mp.client.AssetResponse;
import edu.uiuc.ncsa.myproxy.oa4mp.client.ClientEnvironment;
import edu.uiuc.ncsa.myproxy.oa4mp.client.OA4MPService;
import edu.uiuc.ncsa.myproxy.oa4mp.client.servlet.ClientServlet;
import edu.uiuc.ncsa.security.core.exceptions.GeneralException;
import edu.uiuc.ncsa.security.servlet.JSPUtil;
import edu.uiuc.ncsa.security.util.pkcs.CertUtil;
import org.apache.airavata.credential.store.CertificateCredential;
import org.apache.airavata.credential.store.CommunityUser;
import org.apache.airavata.credential.store.impl.CertificateCredentialWriter;
import org.apache.airavata.credential.store.util.DBUtil;
import org.apache.airavata.credential.store.util.Utility;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * Callback from the portal will come here. In this class we will store incomming
 * certificate to the database.
 * Partly taken from OA4MP code base.
 */
public class CredentialStoreCallbackServlet extends ClientServlet {

    private static final String ERROR_PAGE = "/credential-store/error.jsp";
    private static final String SUCCESS_PAGE = "/credential-store/success.jsp";

    private OA4MPService oa4mpService;

    private CertificateCredentialWriter certificateCredentialWriter;

    public void init() throws ServletException {

        DBUtil dbUtil;

        try {
            dbUtil = DBUtil.getDBUtil(getServletContext());
        } catch (Exception e) {
            throw new ServletException("Error initializing database operations.", e);
        }

        super.init();
        certificateCredentialWriter = new CertificateCredentialWriter(dbUtil);

        info("Credential store callback initialized successfully.");
    }

    @Override
    public OA4MPService getOA4MPService() {
        return oa4mpService;
    }

    @Override
    public void loadEnvironment() throws IOException {
        environment = getConfigurationLoader().load();
        oa4mpService = new CredentialStoreOA4MPServer((ClientEnvironment) environment);
    }

    @Override
    protected void doIt(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        String gatewayName = request.getParameter("gatewayName");
        String portalUserName = request.getParameter("portalUserName");
        String durationParameter = request.getParameter("duration");
        String contactEmail = request.getParameter("email");

        //TODO remove hard coded values, once passing query parameters is
        //fixed in OA4MP client api
        long duration = 800;
        contactEmail = "ogce@sciencegateway.org";

        if (durationParameter != null) {
            duration = Long.parseLong(durationParameter);
        }


        info("Gateway name " + gatewayName);
        info("Portal user name " + portalUserName);
        info("Community user contact email " + portalUserName);

        //TODO remove later
        gatewayName = "defaultGateway";
        portalUserName = "defaultPortal";

        info("2.a. Getting token and verifier.");
        String token = request.getParameter(TOKEN_KEY);
        String verifier = request.getParameter(VERIFIER_KEY);
        if (token == null || verifier == null) {
            warn("2.a. The token is " + (token == null ? "null" : token) + " and the verifier is " + (verifier == null ? "null" : verifier));
            GeneralException ge = new GeneralException("Error: This servlet requires parameters for the token and verifier. It cannot be called directly.");
            request.setAttribute("exception", ge);
            JSPUtil.fwd(request, response, ERROR_PAGE);
            return;
        }
        info("2.a Token and verifier found.");
        X509Certificate cert = null;
        AssetResponse assetResponse = null;

        try {
            info("2.a. Getting the cert(s) from the service");
            assetResponse = getOA4MPService().getCert(token, verifier);
            cert = assetResponse.getX509Certificates()[0];

            // The work in this call
        } catch (Throwable t) {
            warn("2.a. Exception from the server: " + t.getCause().getMessage());
            error("Exception while trying to get cert. message:" + t.getMessage());
            request.setAttribute("exception", t);
            JSPUtil.fwd(request, response, ERROR_PAGE);
            return;
        }
        info("2.b. Done! Displaying success page.");

        CertificateCredential certificateCredential = new CertificateCredential();

        certificateCredential.setNotBefore(Utility.convertDateToString(cert.getNotBefore()));
        certificateCredential.setNotAfter(Utility.convertDateToString(cert.getNotAfter()));
        certificateCredential.setCertificate(CertUtil.toPEM(assetResponse.getX509Certificates()));
        certificateCredential.setCommunityUser(new CommunityUser(gatewayName, assetResponse.getUsername(),
                contactEmail));
        certificateCredential.setPortalUserName(portalUserName);
        certificateCredential.setLifeTime(duration);

        certificateCredentialWriter.writeCredentials(certificateCredential);

        StringBuilder stringBuilder = new StringBuilder("Certificate for community user ");
        stringBuilder.append(assetResponse.getUsername()).append(" successfully persisted.");
        stringBuilder.append(" Certificate DN - ").append(cert.getSubjectDN());

        info(stringBuilder.toString());

        String contextPath = request.getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        request.setAttribute("action", contextPath);
        JSPUtil.fwd(request, response, SUCCESS_PAGE);
        info("2.a. Completely finished with delegation.");

    }
}

