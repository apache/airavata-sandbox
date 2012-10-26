package org.apache.airavata.services.registry.rest.resources;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.airavata.registry.api.AiravataRegistry2;
import org.apache.airavata.services.registry.rest.resourcemappings.ApplicationDescriptor;
import org.apache.airavata.services.registry.rest.utils.RestServicesConstants;

@Path("/api/application")
public class ApplicationRegistration {

    protected static AiravataRegistry2 airavataRegistry;

    @Context
    ServletContext context;

    public ApplicationRegistration() {
//        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
    }
    // Sample JSON is : {"applicationName":"Testing","cpuCount":"12","maxMemory":"0","maxWallTime":"0","minMemory":"0","nodeCount":"0","processorsPerNode":"0"}
	@POST
    @Path("save")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addServiceDescriptor(ApplicationDescriptor application){
        try{
        	application.getApplicationName();
        	Response.ResponseBuilder builder = Response.status(Response.Status.ACCEPTED);
            return builder.build();
        } catch (Exception e) {
        	throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

	@GET
    @Path("get")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ApplicationDescriptor getServiceDescriptor(String applicationName){
        try{
        	ApplicationDescriptor application = new ApplicationDescriptor();
        	application.setApplicationName(applicationName);
            return application;
        } catch (Exception e) {
        	throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}
