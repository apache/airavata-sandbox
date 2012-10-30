package org.apache.airavata.services.registry.rest.resources;

import org.apache.airavata.credential.store.AuditInfo;
import org.apache.airavata.credential.store.CredentialStore;
import org.apache.airavata.credential.store.CredentialStoreException;
import org.apache.airavata.services.registry.rest.utils.RegistryListener;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * API to access the credential store.
 * Provides methods to manage credential store and to query information in credential store.
 * Though we will not provide methods to retrieve credentials.
 * We will trust the portal to execute following operations and we will also assume
 * portal interface will implement appropriate authentication and authorization.
 */

@Path("/credentialStore")
public class CredentialStoreAPI {

    @Context
    ServletContext context;

    @Path("/get/portalUser")
    @GET
    @Produces("text/plain")
    public Response getAssociatingPortalUser(@QueryParam("gatewayName")String gatewayName,
                                                @QueryParam("communityUserName")String communityUser) {
        try {
            String result = getCredentialStore().getPortalUser(gatewayName, communityUser);
            return getOKResponse(result);

        } catch (CredentialStoreException e) {
            return getErrorResponse(e);
        }
    }

    @Path("/get/portalUser")
    @GET
    @Produces("text/plain")
    public Response getAuditInfo(@QueryParam("gatewayName")String gatewayName,
                                             @QueryParam("communityUserName")String communityUser) {
        try {
            AuditInfo auditInfo = getCredentialStore().getAuditInfo(gatewayName, communityUser);
            return getOKResponse(auditInfo);

        } catch (CredentialStoreException e) {
            return getErrorResponse(e);
        }
    }

    @Path("/delete/credential")
    @POST
    @Produces("text/plain")
    public Response removeCredentials(@QueryParam("gatewayName")String gatewayName,
                                      @QueryParam("communityUserName")String communityUser) {
        try {
            getCredentialStore().removeCredentials(gatewayName, communityUser);
            return getOKResponse("success");
        } catch (CredentialStoreException e) {
            return getErrorResponse(e);
        }
    }

    @Path("/update/email")
    @POST
    @Produces("text/plain")
    public Response updateCommunityUserEmail(@QueryParam("gatewayName")String gatewayName,
                                      @QueryParam("communityUserName")String communityUser,
                                      @QueryParam("email")String email) {
        try {
            getCredentialStore().updateCommunityUserEmail(gatewayName, communityUser, email);
            return getOKResponse("success");
        } catch (CredentialStoreException e) {
            return getErrorResponse(e);
        }
    }


    private CredentialStore getCredentialStore() {
        return (CredentialStore) context.getAttribute(RegistryListener.CREDENTIAL_STORE);
    }

    private Response getOKResponse(String result) {
        Response.ResponseBuilder builder = Response.status(Response.Status.OK);
        builder.entity(result);
        return builder.build();
    }

    private Response getOKResponse(AuditInfo result) {
        Response.ResponseBuilder builder = Response.status(Response.Status.OK);
        builder.entity(result);
        return builder.build();
    }

    private Response getErrorResponse(CredentialStoreException exception) {
        Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
        builder.entity(exception.getMessage());
        return builder.build();
    }


}
