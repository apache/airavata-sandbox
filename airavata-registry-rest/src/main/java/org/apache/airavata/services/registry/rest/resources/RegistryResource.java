package org.apache.airavata.services.registry.rest.resources;

import org.apache.airavata.registry.api.exception.RegistryException;
import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.persistance.registry.jpa.JPAResourceAccessor;
import org.apache.airavata.registry.api.*;
import org.apache.airavata.registry.api.exception.gateway.*;
import org.apache.airavata.registry.api.exception.worker.*;
import org.apache.airavata.registry.api.workflow.*;
import org.apache.airavata.schemas.gfac.*;
import org.apache.airavata.services.registry.rest.resourcemappings.*;
import org.apache.airavata.registry.api.AiravataExperiment;
import org.apache.airavata.services.registry.rest.resourcemappings.WorkflowInstanceMapping;
import org.apache.airavata.services.registry.rest.utils.ApplicationDescriptorTypes;
import org.apache.airavata.services.registry.rest.utils.DescriptorUtil;
import org.apache.airavata.services.registry.rest.utils.RestServicesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * RegistryResource for REST interface of Registry API
 * Three objects will be retrieved from the servelet context as
 * airavataRegistry, axis2Registry, dataRegistry which are
 * analogues to main API interfaces of Airavata
 */
@Path("/registry/api")
//public class RegistryResource implements ConfigurationRegistryService,
//        ProjectsRegistryService, ProvenanceRegistryService, UserWorkflowRegistryService,
//        PublishedWorkflowRegistryService, DescriptorRegistryService{
public class RegistryResource {
    private final static Logger logger = LoggerFactory.getLogger(RegistryResource.class);
    private JPAResourceAccessor jpa;
    private boolean active = false;
    private static final String DEFAULT_PROJECT_NAME = "default";
    private AiravataRegistry2 airavataRegistry;

    @Context
    ServletContext context;

    public String getVersion() {
        return null;
    }

    protected void initialize() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        jpa = new JPAResourceAccessor(airavataRegistry);
        active = true;
    }

    /**
     * ---------------------------------Configuration Registry----------------------------------*
     */

    @Path("/configuration")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getConfiguration(@QueryParam("key") String key) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Object configuration = airavataRegistry.getConfiguration(key);
            if (configuration != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(configuration);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }


    @GET
    @Path("/configurationlist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getConfigurationList(@QueryParam("key") String key) {
        try {
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
            List<Object> configurationList = airavataRegistry.getConfigurationList(key);
            ConfigurationList list = new ConfigurationList();
            Object[] configValList = new Object[configurationList.size()];
            for (int i = 0; i < configurationList.size(); i++) {
                configValList[i] = configurationList.get(i);
            }
            list.setConfigValList(configValList);
            if (configurationList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @POST
    @Path("save/configuration")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setConfiguration(@FormParam("key") String key,
                                     @FormParam("value") String value,
                                     @FormParam("date") String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
            airavataRegistry.setConfiguration(key, value, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/configuration")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addConfiguration(@FormParam("key") String key,
                                     @FormParam("value") String value,
                                     @FormParam("date") String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
            airavataRegistry.addConfiguration(key, value, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allconfiguration")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllConfiguration(@QueryParam("key") String key) {
        try {
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
            airavataRegistry.removeAllConfiguration(key);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/configuration")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        try {
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
            airavataRegistry.removeConfiguration(key, value);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("gfac/urilist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGFacURIs() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<URI> uris = airavataRegistry.getGFacURIs();
            URLList list = new URLList();
            String[] urs = new String[uris.size()];
            for (int i = 0; i < uris.size(); i++) {
                urs[i] = uris.get(i).toString();
            }
            list.setUris(urs);
            if (urs.length != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("workflowinterpreter/urilist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkflowInterpreterURIs() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<URI> uris = airavataRegistry.getWorkflowInterpreterURIs();
            URLList list = new URLList();
            String[] urs = new String[uris.size()];
            for (int i = 0; i < uris.size(); i++) {
                urs[i] = uris.get(i).toString();
            }
            list.setUris(urs);
            if (urs.length != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("eventingservice/uri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getEventingServiceURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI eventingServiceURI = airavataRegistry.getEventingServiceURI();
            if (eventingServiceURI != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(eventingServiceURI.toString());
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("messagebox/uri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI eventingServiceURI = airavataRegistry.getMessageBoxURI();
            if (eventingServiceURI != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(eventingServiceURI.toString());
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/gfacuri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGFacURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI gfacURI = new URI(uri);
            airavataRegistry.addGFacURI(gfacURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/workflowinterpreteruri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI interpreterURI = new URI(uri);
            airavataRegistry.addWorkflowInterpreterURI(interpreterURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/eventinguri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setEventingURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI eventingURI = new URI(uri);
            airavataRegistry.setEventingURI(eventingURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/msgboxuri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setMessageBoxURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI msgBoxURI = new URI(uri);
            airavataRegistry.setMessageBoxURI(msgBoxURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/gfacuri/date")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGFacURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI gfacURI = new URI(uri);
            airavataRegistry.addGFacURI(gfacURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/workflowinterpreteruri/date")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI interpreterURI = new URI(uri);
            airavataRegistry.addWorkflowInterpreterURI(interpreterURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/eventinguri/date")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setEventingURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI eventingURI = new URI(uri);
            airavataRegistry.setEventingURI(eventingURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/msgboxuri/date")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setMessageBoxURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI msgBoxURI = new URI(uri);
            airavataRegistry.setMessageBoxURI(msgBoxURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/gfacuri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeGFacURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI gfacURI = new URI(uri);
            airavataRegistry.removeGFacURI(gfacURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allgfacuris")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllGFacURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeAllGFacURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/workflowinterpreteruri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeWorkflowInterpreterURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            URI intURI = new URI(uri);
            airavataRegistry.removeWorkflowInterpreterURI(intURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allworkflowinterpreteruris")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllWorkflowInterpreterURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeAllWorkflowInterpreterURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/eventinguri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response unsetEventingURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.unsetEventingURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/msgboxuri")
    @Produces(MediaType.TEXT_PLAIN)
    public Response unsetMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.unsetMessageBoxURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    /**
     * ---------------------------------Descriptor Registry----------------------------------*
     */


    @GET
    @Path("hostdescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isHostDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isHostDescriptorExists(descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/save/test")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addHostDescriptor(@FormParam("hostName") String hostName,
                                      @FormParam("hostAddress") String hostAddress,
                                      @FormParam("hostEndpoint") String hostEndpoint,
                                      @FormParam("gatekeeperEndpoint") String gatekeeperEndpoint) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = DescriptorUtil.createHostDescription(hostName, hostAddress, hostEndpoint, gatekeeperEndpoint);
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            // TODO : Use WEbapplicationExcpetion
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addJSONHostDescriptor(HostDescriptor host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = DescriptorUtil.createHostDescription(host);
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateHostDescriptor(HostDescriptor host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = DescriptorUtil.createHostDescription(host);
            airavataRegistry.updateHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("host/description")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            HostDescription hostDescription = airavataRegistry.getHostDescriptor(hostName);
            HostDescriptor hostDescriptor = DescriptorUtil.createHostDescriptor(hostDescription);
            if (hostDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(hostDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    @DELETE
    @Path("hostdescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeHostDescriptor(hostName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/hostdescriptors")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<HostDescription> hostDescriptionList = airavataRegistry.getHostDescriptors();
            HostDescriptionList list = new HostDescriptionList();
            HostDescriptor[] hostDescriptions = new HostDescriptor[hostDescriptionList.size()];
            for (int i = 0; i < hostDescriptionList.size(); i++) {
                HostDescriptor hostDescriptor = DescriptorUtil.createHostDescriptor(hostDescriptionList.get(i));
                hostDescriptions[i] = hostDescriptor;
            }
            list.setHostDescriptions(hostDescriptions);
            if (hostDescriptionList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("servicedescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isServiceDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isServiceDescriptorExists(descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("servicedescriptor/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addJSONServiceDescriptor(ServiceDescriptor service) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(service);
            airavataRegistry.addServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("servicedescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateServiceDescriptor(ServiceDescriptor service) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(service);
            airavataRegistry.updateServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("servicedescriptor/description")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptor(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            if (serviceDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(serviceDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("servicedescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeServiceDescriptor(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeServiceDescriptor(serviceName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/servicedescriptors")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<ServiceDescription> serviceDescriptors = airavataRegistry.getServiceDescriptors();
            ServiceDescriptionList list = new ServiceDescriptionList();
            ServiceDescriptor[] serviceDescriptions = new ServiceDescriptor[serviceDescriptors.size()];
            for (int i = 0; i < serviceDescriptors.size(); i++) {
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescriptors.get(i));
                serviceDescriptions[i] = serviceDescriptor;
            }
            list.setServiceDescriptions(serviceDescriptions);
            if (serviceDescriptors.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(list);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("applicationdescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isApplicationDescriptorExists(@QueryParam("serviceName") String serviceName,
                                                  @QueryParam("hostName") String hostName,
                                                  @QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        boolean state;
        try {
            state = airavataRegistry.isApplicationDescriptorExists(serviceName, hostName, descriptorName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("applicationdescriptor/build/save/test")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addApplicationDescriptorTest(@FormParam("appName") String appName, @FormParam("exeuctableLocation") String exeuctableLocation, @FormParam("scratchWorkingDirectory") String scratchWorkingDirectory, @FormParam("hostName") String hostName,
                                                 @FormParam("projAccNumber") String projAccNumber, @FormParam("queueName") String queueName, @FormParam("cpuCount") String cpuCount, @FormParam("nodeCount") String nodeCount, @FormParam("maxMemory") String maxMemory,
                                                 @FormParam("serviceName") String serviceName, @FormParam("inputName1") String inputName1, @FormParam("inputType1") String inputType1, @FormParam("outputName") String outputName, @FormParam("outputType") String outputType) throws Exception {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);

        System.out.println("application descriptor save started ...");
        ServiceDescription serv = DescriptorUtil.getServiceDescription(serviceName, inputName1, inputType1, outputName, outputType);
        // Creating the descriptor as a temporary measure.
        ApplicationDeploymentDescription app = DescriptorUtil.registerApplication(appName, exeuctableLocation, scratchWorkingDirectory,
                hostName, projAccNumber, queueName, cpuCount, nodeCount, maxMemory);
        try {
            if (!airavataRegistry.isHostDescriptorExists(hostName)) {
                System.out.println(hostName + " host not exist");
//                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                return builder.build();
            }

            if (airavataRegistry.isServiceDescriptorExists(serv.getType().getName())) {
                System.out.println(serviceName + " service updated ");
                airavataRegistry.updateServiceDescriptor(serv);
            } else {
                System.out.println(serviceName + " service created ");
                airavataRegistry.addServiceDescriptor(serv);
            }

            if (airavataRegistry.isApplicationDescriptorExists(serv.getType().getName(), hostName, app.getType().getApplicationName().getStringValue())) {
                System.out.println(appName + " app already exists. retruning an error");
//                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//                return builder.build();
            } else {
                System.out.println(appName + " adding the app");
                airavataRegistry.addApplicationDescriptor(serv.getType().getName(), hostName, app);
            }

//            airavataRegistry.addApplicationDescriptor(serviceName, hostName, app);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("applicationdescriptor/build/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addJSONApplicationDescriptor(ApplicationDescriptor applicationDescriptor) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String hostdescName = applicationDescriptor.getHostdescName();
            if (!airavataRegistry.isHostDescriptorExists(hostdescName)) {
                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return builder.build();
            }
            ApplicationDeploymentDescription applicationDeploymentDescription = DescriptorUtil.createApplicationDescription(applicationDescriptor);
            ServiceDescriptor serviceDescriptor = applicationDescriptor.getServiceDescriptor();
            String serviceName;
            if (serviceDescriptor != null) {
                if (serviceDescriptor.getServiceName() == null) {
                    serviceName = applicationDescriptor.getName();
                    serviceDescriptor.setServiceName(serviceName);
                } else {
                    serviceName = serviceDescriptor.getServiceName();
                }
                ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(serviceDescriptor);
                airavataRegistry.addServiceDescriptor(serviceDescription);
            } else {
                serviceName = applicationDescriptor.getName();
            }
            airavataRegistry.addApplicationDescriptor(serviceName, hostdescName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


//
//    @POST
//    @Path("applicationdescriptor/save")
//    @Consumes(MediaType.TEXT_XML)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response addApplicationDesc(@FormParam("serviceName") String serviceName,
//                                       @FormParam("hostName") String hostName,
//                                       String application) {
//        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
//        try{
//            ApplicationDeploymentDescription applicationDeploymentDescription = ApplicationDeploymentDescription.fromXML(application);
//            airavataRegistry.addApplicationDescriptor(serviceName, hostName, applicationDeploymentDescription);
//            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
//            return builder.build();
//        } catch (DescriptorAlreadyExistsException e){
//            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
//            return builder.build();
//        } catch (XmlException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
//            return builder.build();
//        } catch (RegistryException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//            return builder.build();
//        }
//
//    }

    @POST
    @Path("applicationdescriptor/update")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response udpateApplicationDescriptorByDescriptors(ApplicationDescriptor applicationDescriptor) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String hostdescName = applicationDescriptor.getHostdescName();
            if (!airavataRegistry.isHostDescriptorExists(hostdescName)) {
                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return builder.build();
            }
            ApplicationDeploymentDescription applicationDeploymentDescription = DescriptorUtil.createApplicationDescription(applicationDescriptor);
            ServiceDescriptor serviceDescriptor = applicationDescriptor.getServiceDescriptor();
            String serviceName;
            if (serviceDescriptor != null) {
                if (serviceDescriptor.getServiceName() == null) {
                    serviceName = applicationDescriptor.getName();
                    serviceDescriptor.setServiceName(serviceName);
                } else {
                    serviceName = serviceDescriptor.getServiceName();
                }
                ServiceDescription serviceDescription = DescriptorUtil.createServiceDescription(serviceDescriptor);
                if (airavataRegistry.isServiceDescriptorExists(serviceName)) {
                    airavataRegistry.updateServiceDescriptor(serviceDescription);
                } else {
                    airavataRegistry.addServiceDescriptor(serviceDescription);
                }

            } else {
                serviceName = applicationDescriptor.getName();
            }
            airavataRegistry.updateApplicationDescriptor(serviceName, hostdescName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("applicationdescriptor/description")
    @Produces("text/xml")
    public Response getApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                             @QueryParam("hostName") String hostName,
                                             @QueryParam("applicationName") String applicationName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ApplicationDeploymentDescription applicationDeploymentDescription = airavataRegistry.getApplicationDescriptor(serviceName, hostName, applicationName);
            ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
            applicationDescriptor.setHostdescName(hostName);
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            applicationDescriptor.setServiceDescriptor(serviceDescriptor);

            if (applicationDeploymentDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptors/alldescriptors/host/service")
    @Produces("text/xml")
    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName,
                                              @QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            ApplicationDeploymentDescription applicationDeploymentDescription = airavataRegistry.getApplicationDescriptors(serviceName, hostName);
            ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
            applicationDescriptor.setHostdescName(hostName);
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
            applicationDescriptor.setServiceDescriptor(serviceDescriptor);

            if (applicationDeploymentDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptor);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptor/alldescriptors/service")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Map<String, ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors(serviceName);
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
            int i = 0;
            for (String hostName : applicationDeploymentDescriptionMap.keySet()) {
                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(hostName);
                ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
                applicationDescriptor.setHostdescName(hostName);

                ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
                applicationDescriptor.setServiceDescriptor(serviceDescriptor);

                applicationDescriptors[i] = applicationDescriptor;
                i++;
            }
            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
            if (applicationDeploymentDescriptionMap.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptorList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (MalformedDescriptorException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("applicationdescriptor/alldescriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Map<String[], ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors();
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
            int i = 0;
            for (String[] descriptors : applicationDeploymentDescriptionMap.keySet()) {
                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(descriptors);
                ApplicationDescriptor applicationDescriptor = DescriptorUtil.createApplicationDescriptor(applicationDeploymentDescription);
                applicationDescriptor.setHostdescName(descriptors[1]);
                ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(descriptors[0]);
                if (serviceDescription == null) {
                    Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                    return builder.build();
                }
                ServiceDescriptor serviceDescriptor = DescriptorUtil.createServiceDescriptor(serviceDescription);
                applicationDescriptor.setServiceDescriptor(serviceDescriptor);
                applicationDescriptors[i] = applicationDescriptor;
                i++;
            }
            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
            if (applicationDeploymentDescriptionMap.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(applicationDescriptorList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (MalformedDescriptorException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("applicationdescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                                @QueryParam("hostName") String hostName,
                                                @QueryParam("appName") String appName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeApplicationDescriptor(serviceName, hostName, appName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    /**
     * ---------------------------------Project Registry----------------------------------*
     */
    @GET
    @Path("project/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isWorkspaceProjectExists(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            boolean result = airavataRegistry.isWorkspaceProjectExists(projectName);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                builder.entity("False");
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("project/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isWorkspaceProjectExists(@FormParam("projectName") String projectName,
                                             @FormParam("createIfNotExists") String createIfNotExists) {
        boolean createIfNotExistStatus = false;
        if (createIfNotExists.equals("true")) {
            createIfNotExistStatus = true;
        }
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            boolean result = airavataRegistry.isWorkspaceProjectExists(projectName, createIfNotExistStatus);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                builder.entity("False");
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            builder.entity("False");
            return builder.build();
        }
    }

    @POST
    @Path("add/project")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addWorkspaceProject(@FormParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkspaceProject workspaceProject = new WorkspaceProject(projectName, airavataRegistry);
            airavataRegistry.addWorkspaceProject(workspaceProject);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (WorkspaceProjectAlreadyExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/project")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkspaceProject(@FormParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkspaceProject workspaceProject = new WorkspaceProject(projectName, airavataRegistry);
            airavataRegistry.updateWorkspaceProject(workspaceProject);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (WorkspaceProjectDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/project")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.deleteWorkspaceProject(projectName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (WorkspaceProjectDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/project")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkspaceProject workspaceProject = airavataRegistry.getWorkspaceProject(projectName);
            if (workspaceProject != null) {
                WorkspaceProjectMapping workspaceProjectMapping = new WorkspaceProjectMapping(workspaceProject.getProjectName());

                List<AiravataExperiment> airavataExperimentList = workspaceProject.getProjectsRegistry().getExperiments(workspaceProject.getProjectName());
                Experiment[] experiments = new Experiment[airavataExperimentList.size()];
                if (airavataExperimentList.size() != 0) {
                    for (int i = 0; i < airavataExperimentList.size(); i++) {
                        Experiment experiment = new Experiment();
                        AiravataExperiment airavataExperiment = airavataExperimentList.get(i);
                        experiment.setExperimentId(airavataExperiment.getExperimentId());
                        experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                        experiment.setProject(airavataExperiment.getProject().getProjectName());
                        experiment.setUser(airavataExperiment.getUser().getUserName());
                        experiment.setDate(airavataExperiment.getSubmittedDate());
                        experiments[i] = experiment;
                    }
                    workspaceProjectMapping.setExperimentsList(experiments);
                }

                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workspaceProjectMapping);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                return builder.build();
            }
        } catch (WorkspaceProjectDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/projects")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkspaceProjects() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkspaceProject> workspaceProjects = airavataRegistry.getWorkspaceProjects();
            WorkspaceProjectList workspaceProjectList = new WorkspaceProjectList();
            WorkspaceProjectMapping[] workspaceProjectSet = new WorkspaceProjectMapping[workspaceProjects.size()];
            for (int i = 0; i < workspaceProjects.size(); i++) {
                WorkspaceProject workspaceProject = workspaceProjects.get(i);
                WorkspaceProjectMapping workspaceProjectMapping = new WorkspaceProjectMapping(workspaceProject.getProjectName());

                List<AiravataExperiment> airavataExperimentList = workspaceProject.getProjectsRegistry().getExperiments(workspaceProject.getProjectName());
                Experiment[] experiments = new Experiment[airavataExperimentList.size()];
                if (airavataExperimentList.size() != 0) {
                    for (int j = 0; j < airavataExperimentList.size(); j++) {
                        Experiment experiment = new Experiment();
                        AiravataExperiment airavataExperiment = airavataExperimentList.get(j);
                        experiment.setExperimentId(airavataExperiment.getExperimentId());
                        experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                        experiment.setProject(airavataExperiment.getProject().getProjectName());
                        experiment.setUser(airavataExperiment.getUser().getUserName());
                        experiment.setDate(airavataExperiment.getSubmittedDate());
                        experiments[j] = experiment;
                    }
                    workspaceProjectMapping.setExperimentsList(experiments);
                }
                workspaceProjectSet[i] = workspaceProjectMapping;
            }
            workspaceProjectList.setWorkspaceProjectMappings(workspaceProjectSet);
            if (workspaceProjects.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workspaceProjectList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    /**
     * ---------------------------------Experiments----------------------------------*
     */

    @DELETE
    @Path("delete/experiment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeExperiment(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.removeExperiment(experimentId);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (ExperimentDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiments/all")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperiments() throws RegistryException {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments();
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i = 0; i < airavataExperimentList.size(); i++) {
                AiravataExperiment airavataExperiment = airavataExperimentList.get(i);
                Experiment experiment = new Experiment();
                experiment.setExperimentId(airavataExperiment.getExperimentId());
                experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                experiment.setProject(airavataExperiment.getProject().getProjectName());
                experiment.setUser(airavataExperiment.getUser().getUserName());
                experiment.setDate(airavataExperiment.getSubmittedDate());
                experiments[i] = experiment;
            }
            experimentList.setExperiments(experiments);
            if (airavataExperimentList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(experimentList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiments/project")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(projectName);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i = 0; i < airavataExperimentList.size(); i++) {
                AiravataExperiment airavataExperiment = airavataExperimentList.get(i);
                Experiment experiment = new Experiment();
                experiment.setExperimentId(airavataExperiment.getExperimentId());
                experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                experiment.setProject(airavataExperiment.getProject().getProjectName());
                experiment.setUser(airavataExperiment.getUser().getUserName());
                experiment.setDate(airavataExperiment.getSubmittedDate());
                experiments[i] = experiment;
            }
            experimentList.setExperiments(experiments);
            if (airavataExperimentList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(experimentList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiments/date")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByDate(@QueryParam("fromDate") String fromDate,
                                         @QueryParam("toDate") String toDate) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedFromDate = dateFormat.parse(fromDate);
            Date formattedToDate = dateFormat.parse(toDate);
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(formattedFromDate, formattedToDate);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i = 0; i < airavataExperimentList.size(); i++) {
                AiravataExperiment airavataExperiment = airavataExperimentList.get(i);
                Experiment experiment = new Experiment();
                experiment.setExperimentId(airavataExperiment.getExperimentId());
                experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                experiment.setProject(airavataExperiment.getProject().getProjectName());
                experiment.setUser(airavataExperiment.getUser().getUserName());
                experiment.setDate(airavataExperiment.getSubmittedDate());
                experiments[i] = experiment;
            }
            experimentList.setExperiments(experiments);
            if (airavataExperimentList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(experimentList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (ParseException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiments/project/date")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByProjectDate(@QueryParam("projectName") String projectName,
                                                @QueryParam("fromDate") String fromDate,
                                                @QueryParam("toDate") String toDate) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedFromDate = dateFormat.parse(fromDate);
            Date formattedToDate = dateFormat.parse(toDate);
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(projectName, formattedFromDate, formattedToDate);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i = 0; i < airavataExperimentList.size(); i++) {
                AiravataExperiment airavataExperiment = airavataExperimentList.get(i);
                Experiment experiment = new Experiment();
                experiment.setExperimentId(airavataExperiment.getExperimentId());
                experiment.setGatewayName(airavataExperiment.getGateway().getGatewayName());
                experiment.setProject(airavataExperiment.getProject().getProjectName());
                experiment.setUser(airavataExperiment.getUser().getUserName());
                experiment.setDate(airavataExperiment.getSubmittedDate());
                experiments[i] = experiment;
            }
            experimentList.setExperiments(experiments);
            if (airavataExperimentList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(experimentList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (ParseException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/experiment")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addExperiment(@FormParam("projectName") String projectName,
                                  @FormParam("experimentID") String experimentID,
                                  @FormParam("submittedDate") String submittedDate) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            AiravataExperiment experiment = new AiravataExperiment();
            experiment.setExperimentId(experimentID);
            Gateway gateway = (Gateway) context.getAttribute(RestServicesConstants.GATEWAY);
            AiravataUser airavataUser = (AiravataUser) context.getAttribute(RestServicesConstants.REGISTRY_USER);
            experiment.setGateway(gateway);
            experiment.setUser(airavataUser);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(submittedDate);
            experiment.setSubmittedDate(formattedDate);
            airavataRegistry.addExperiment(projectName, experiment);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (ExperimentDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        } catch (WorkspaceProjectDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (ParseException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    @GET
    @Path("experiment/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isExperimentExists(@QueryParam("experimentId") String experimentId) throws RegistryException {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.isExperimentExists(experimentId);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            builder.entity("True");
            return builder.build();
        } catch (ExperimentDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            builder.entity("False");
            return builder.build();
        }
    }

    @GET
    @Path("experiment/notexist/create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isExperimentExistsThenCreate(@QueryParam("experimentId") String experimentId,
                                                 @QueryParam("createIfNotPresent") String createIfNotPresent) {
        boolean createIfNotPresentStatus = false;
        if (createIfNotPresent.equals("true")) {
            createIfNotPresentStatus = true;
        }
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.isExperimentExists(experimentId, createIfNotPresentStatus);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            builder.entity("True");
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @POST
    @Path("update/experiment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateExperimentExecutionUser(@FormParam("experimentId") String experimentId,
                                                  @FormParam("user") String user) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.updateExperimentExecutionUser(experimentId, user);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiment/executionuser")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentExecutionUser(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String user = airavataRegistry.getExperimentExecutionUser(experimentId);
            if (user != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(user);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiment/name")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentName(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String result = airavataRegistry.getExperimentName(experimentId);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/experimentname")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateExperimentName(@FormParam("experimentId") String experimentId,
                                         @FormParam("experimentName") String experimentName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.updateExperimentName(experimentId, experimentName);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }


    @GET
    @Path("get/experimentmetadata")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentMetadata(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String result = airavataRegistry.getExperimentMetadata(experimentId);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/experimentmetadata")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateExperimentMetadata(@FormParam("experimentId") String experimentId,
                                             @FormParam("metadata") String metadata) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.updateExperimentMetadata(experimentId, metadata);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("get/workflowtemplatename")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflowExecutionTemplateName(@QueryParam("workflowInstanceId") String workflowInstanceId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            String result = airavataRegistry.getWorkflowExecutionTemplateName(workflowInstanceId);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/workflowinstancetemplatename")
    @Produces(MediaType.TEXT_PLAIN)
    public Response setWorkflowInstanceTemplateName(@FormParam("workflowInstanceId") String workflowInstanceId,
                                                    @FormParam("templateName") String templateName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            airavataRegistry.setWorkflowInstanceTemplateName(workflowInstanceId, templateName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("get/experimentworkflowinstances")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentWorkflowInstances(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkflowInstance> experimentWorkflowInstances = airavataRegistry.getExperimentWorkflowInstances(experimentId);
            WorkflowInstancesList workflowInstancesList = new WorkflowInstancesList();
            WorkflowInstanceMapping[] workflowInstanceMappings = new WorkflowInstanceMapping[experimentWorkflowInstances.size()];
            for (int i = 0; i < experimentWorkflowInstances.size(); i++) {
                WorkflowInstanceMapping workflowInstanceMapping = new WorkflowInstanceMapping();
                WorkflowInstance workflowInstance = experimentWorkflowInstances.get(i);
                workflowInstanceMapping.setExperimentId(workflowInstance.getExperimentId());
                workflowInstanceMapping.setTemplateName(workflowInstance.getTemplateName());
                workflowInstanceMapping.setWorkflowInstanceId(workflowInstance.getWorkflowInstanceId());
                workflowInstanceMappings[i] = workflowInstanceMapping;
            }
            workflowInstancesList.setWorkflowInstanceMappings(workflowInstanceMappings);
            if (experimentWorkflowInstances.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowInstancesList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("workflowinstance/exist/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isWorkflowInstanceExists(@QueryParam("instanceId") String instanceId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Boolean result = airavataRegistry.isWorkflowInstanceExists(instanceId);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                builder.entity("False");
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }

    }

    @GET
    @Path("workflowinstance/exist/create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isWorkflowInstanceExistsThenCreate(@QueryParam("instanceId") String instanceId,
                                                       @QueryParam("createIfNotPresent") boolean createIfNotPresent) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            Boolean result = airavataRegistry.isWorkflowInstanceExists(instanceId, createIfNotPresent);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @POST
    @Path("update/workflowinstancestatus/instanceid")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowInstanceStatusByInstance(@FormParam("instanceId") String instanceId,
                                                           @FormParam("executionStatus") String executionStatus) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkflowInstanceStatus.ExecutionStatus status = WorkflowInstanceStatus.ExecutionStatus.valueOf(executionStatus);
            airavataRegistry.updateWorkflowInstanceStatus(instanceId, status);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @POST
    @Path("update/workflowinstancestatus")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowInstanceStatus(@FormParam("workflowInstanceId") String workflowInstanceId,
                                                 @FormParam("executionStatus") String executionStatus,
                                                 @FormParam("statusUpdateTime") String statusUpdateTime) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(statusUpdateTime);
            WorkflowInstance workflowInstance = airavataRegistry.getWorkflowInstanceData(workflowInstanceId).getWorkflowInstance();
            WorkflowInstanceStatus.ExecutionStatus status = WorkflowInstanceStatus.ExecutionStatus.valueOf(executionStatus);
            WorkflowInstanceStatus workflowInstanceStatus = new WorkflowInstanceStatus(workflowInstance, status, formattedDate);
            airavataRegistry.updateWorkflowInstanceStatus(workflowInstanceStatus);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        } catch (ParseException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("get/workflowinstancestatus")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkflowInstanceStatus(@QueryParam("instanceId") String instanceId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkflowInstanceStatus workflowInstanceStatus = airavataRegistry.getWorkflowInstanceStatus(instanceId);
            WorkflowInstanceStatusMapping workflowInstanceStatusMapping = new WorkflowInstanceStatusMapping();
            workflowInstanceStatusMapping.setExecutionStatus(workflowInstanceStatus.getExecutionStatus().name());
            workflowInstanceStatusMapping.setStatusUpdateTime(workflowInstanceStatus.getStatusUpdateTime());
            WorkflowInstance workflowInstance = workflowInstanceStatus.getWorkflowInstance();
            WorkflowInstanceMapping workflowInstanceMapping = new WorkflowInstanceMapping();
            if (workflowInstance != null) {
                workflowInstanceStatusMapping.setExperimentId(workflowInstance.getExperimentId());
                workflowInstanceStatusMapping.setTemplateName(workflowInstance.getTemplateName());
                workflowInstanceStatusMapping.setWorkflowInstanceId(workflowInstance.getWorkflowInstanceId());
            }
//            workflowInstanceStatusMapping.setWorkflowInstanceMapping(workflowInstanceMapping);
            if (workflowInstanceStatusMapping != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowInstanceStatusMapping);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/workflownodeinput")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowNodeInput(@FormParam("nodeID") String nodeID,
                                            @FormParam("workflowInstanceId") String workflowInstanceID,
                                            @FormParam("data") String data) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkflowInstanceData workflowInstanceData = airavataRegistry.getWorkflowInstanceData(workflowInstanceID);
            WorkflowInstanceNode workflowInstanceNode = workflowInstanceData.getNodeData(nodeID).getWorkflowInstanceNode();
            airavataRegistry.updateWorkflowNodeInput(workflowInstanceNode, data);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }

    }

    @POST
    @Path("update/workflownodeoutput")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowNodeOutput(@FormParam("nodeID") String nodeID,
                                             @FormParam("workflowInstanceId") String workflowInstanceID,
                                             @FormParam("data") String data) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            WorkflowInstanceData workflowInstanceData = airavataRegistry.getWorkflowInstanceData(workflowInstanceID);
            WorkflowInstanceNode workflowInstanceNode = workflowInstanceData.getNodeData(nodeID).getWorkflowInstanceNode();
            airavataRegistry.updateWorkflowNodeOutput(workflowInstanceNode, data);
            Response.ResponseBuilder builder = Response.status(Response.Status.OK);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("search/workflowinstancenodeinput")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response searchWorkflowInstanceNodeInput(@QueryParam("experimentIdRegEx") String experimentIdRegEx,
                                                    @QueryParam("workflowNameRegEx") String workflowNameRegEx,
                                                    @QueryParam("nodeNameRegEx") String nodeNameRegEx) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeInput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i < workflowNodeIODataList.size(); i++) {
                WorkflowNodeIOData nodeIOData = workflowNodeIODataList.get(i);
                WorkflowNodeIODataMapping workflowNodeIODataMapping = new WorkflowNodeIODataMapping();

                workflowNodeIODataMapping.setExperimentId(nodeIOData.getExperimentId());
                workflowNodeIODataMapping.setWorkflowId(nodeIOData.getWorkflowId());
                workflowNodeIODataMapping.setWorkflowInstanceId(nodeIOData.getWorkflowInstanceId());
                workflowNodeIODataMapping.setWorkflowName(nodeIOData.getWorkflowName());
                workflowNodeIODataMapping.setWorkflowNodeType(nodeIOData.getNodeType().toString());
                workflowNodeIODataCollection[i] = workflowNodeIODataMapping;
            }
            workflowNodeIOData.setWorkflowNodeIODataMappings(workflowNodeIODataCollection);
            if (workflowNodeIODataList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowNodeIOData);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("search/workflowinstancenodeoutput")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response searchWorkflowInstanceNodeOutput(@QueryParam("experimentIdRegEx") String experimentIdRegEx,
                                                     @QueryParam("workflowNameRegEx") String workflowNameRegEx,
                                                     @QueryParam("nodeNameRegEx") String nodeNameRegEx) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeOutput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i < workflowNodeIODataList.size(); i++) {
                WorkflowNodeIOData nodeIOData = workflowNodeIODataList.get(i);
                WorkflowNodeIODataMapping workflowNodeIODataMapping = new WorkflowNodeIODataMapping();

                workflowNodeIODataMapping.setExperimentId(nodeIOData.getExperimentId());
                workflowNodeIODataMapping.setWorkflowId(nodeIOData.getWorkflowId());
                workflowNodeIODataMapping.setWorkflowInstanceId(nodeIOData.getWorkflowInstanceId());
                workflowNodeIODataMapping.setWorkflowName(nodeIOData.getWorkflowName());
                workflowNodeIODataMapping.setWorkflowNodeType(nodeIOData.getNodeType().toString());
                workflowNodeIODataCollection[i] = workflowNodeIODataMapping;
            }
            workflowNodeIOData.setWorkflowNodeIODataMappings(workflowNodeIODataCollection);
            if (workflowNodeIODataList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowNodeIOData);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/workflowinstancenodeinput")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkflowInstanceNodeInput(@QueryParam("workflowInstanceId") String workflowInstanceId,
                                                 @QueryParam("nodeType") String nodeType) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeInput(workflowInstanceId, nodeType);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i < workflowNodeIODataList.size(); i++) {
                WorkflowNodeIOData nodeIOData = workflowNodeIODataList.get(i);
                WorkflowNodeIODataMapping workflowNodeIODataMapping = new WorkflowNodeIODataMapping();

                workflowNodeIODataMapping.setExperimentId(nodeIOData.getExperimentId());
                workflowNodeIODataMapping.setWorkflowId(nodeIOData.getWorkflowId());
                workflowNodeIODataMapping.setWorkflowInstanceId(nodeIOData.getWorkflowInstanceId());
                workflowNodeIODataMapping.setWorkflowName(nodeIOData.getWorkflowName());
                workflowNodeIODataMapping.setWorkflowNodeType(nodeIOData.getNodeType().toString());
                workflowNodeIODataCollection[i] = workflowNodeIODataMapping;
            }
            workflowNodeIOData.setWorkflowNodeIODataMappings(workflowNodeIODataCollection);
            if (workflowNodeIODataList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowNodeIOData);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/workflowinstancenodeoutput")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkflowInstanceNodeOutput(@QueryParam("workflowInstanceId") String workflowInstanceId,
                                                  @QueryParam("nodeType") String nodeType) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeOutput(workflowInstanceId, nodeType);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i < workflowNodeIODataList.size(); i++) {
                WorkflowNodeIOData nodeIOData = workflowNodeIODataList.get(i);
                WorkflowNodeIODataMapping workflowNodeIODataMapping = new WorkflowNodeIODataMapping();

                workflowNodeIODataMapping.setExperimentId(nodeIOData.getExperimentId());
                workflowNodeIODataMapping.setWorkflowId(nodeIOData.getWorkflowId());
                workflowNodeIODataMapping.setWorkflowInstanceId(nodeIOData.getWorkflowInstanceId());
                workflowNodeIODataMapping.setWorkflowName(nodeIOData.getWorkflowName());
                workflowNodeIODataMapping.setWorkflowNodeType(nodeIOData.getNodeType().toString());
                workflowNodeIODataCollection[i] = workflowNodeIODataMapping;
            }
            workflowNodeIOData.setWorkflowNodeIODataMappings(workflowNodeIODataCollection);
            if (workflowNodeIODataList.size() != 0) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowNodeIOData);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    public Response getExperiment(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getExperimentIdByUser(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getExperimentByUser(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getExperimentByUser(String s, int i, int i1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflowNodeStatus(WorkflowInstanceNodeStatus workflowInstanceNodeStatus) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflowNodeStatus(String s, String s1, String s2) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflowNodeStatus(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowNodeStatus(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowNodeStartTime(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowStartTime(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflowNodeGramData(WorkflowNodeGramData workflowNodeGramData) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowInstanceData(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response isWorkflowInstanceNodePresent(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response isWorkflowInstanceNodePresent(String s, String s1, boolean b) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowInstanceNodeData(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response addWorkflowInstance(String s, String s1, String s2) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflowNodeType(String s, String s1, String s2) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response addWorkflowInstanceNode(String s, String s1) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isActive() {
        return active;
    }

    public Response isWorkflowExists(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response addWorkflow(String s, String s1) throws UserWorkflowAlreadyExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response updateWorkflow(String s, String s1) throws UserWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowGraphXML(String s) throws UserWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflows() throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getWorkflowMetadata(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response removeWorkflow(String s) throws UserWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response isPublishedWorkflowExists(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response publishWorkflow(String s, String s1) throws PublishedWorkflowAlreadyExistsException, UserWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response publishWorkflow(String s) throws PublishedWorkflowAlreadyExistsException, UserWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getPublishedWorkflowGraphXML(String s) throws PublishedWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getPublishedWorkflowNames() throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getPublishedWorkflows() throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response getPublishedWorkflowMetadata(String s) throws RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Response removePublishedWorkflow(String s) throws PublishedWorkflowDoesNotExistsException, RegistryException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

