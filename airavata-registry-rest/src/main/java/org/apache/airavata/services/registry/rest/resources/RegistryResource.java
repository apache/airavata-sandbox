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
import org.apache.airavata.registry.services.*;
import org.apache.airavata.services.registry.rest.resourcemappings.*;
import org.apache.airavata.registry.api.AiravataExperiment;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 *  RegistryResource for REST interface of Registry API
 *  Three objects will be retrieved from the servelet context as
 *  airavataRegistry, axis2Registry, dataRegistry which are
 *  analogues to main API interfaces of Airavata
 */
@Path("/registry/api")
//public class RegistryResource implements ConfigurationRegistryService,
//        ProjectsRegistryService, ProvenanceRegistryService, UserWorkflowRegistryService,
//        PublishedWorkflowRegistryService, DescriptorRegistryService{
    public class RegistryResource {
    private final static Logger logger = LoggerFactory.getLogger(RegistryResource.class);
    private JPAResourceAccessor jpa;
    private boolean active=false;
    private static final String DEFAULT_PROJECT_NAME = "default";
    private AiravataRegistry2 airavataRegistry;
    public static final String AIRAVATA_CONTEXT = "airavataRegistry";

    @Context
    ServletContext context;

    public String getVersion(){
        return  null;
    }

    protected void initialize() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        jpa = new JPAResourceAccessor(airavataRegistry);
        active=true;
    }

    /**---------------------------------Configuration Registry----------------------------------**/

    @Path("/configuration")
    @GET
    @Produces("text/plain")
    public Response getConfiguration(@QueryParam("key") String key) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
        try{
            airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
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
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
            airavataRegistry.setConfiguration(key, value, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("update/configuration")
    @Produces("text/plain")
    public Response addConfiguration(@FormParam("key") String key,
                                     @FormParam("value") String value,
                                     @FormParam("date") String date) {
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
            airavataRegistry.addConfiguration(key, value, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allconfiguration")
    @Produces("text/plain")
    public Response removeAllConfiguration(@QueryParam("key") String key) {
        try{
            airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
            airavataRegistry.removeAllConfiguration(key);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/configuration")
    @Produces("text/plain")
    public Response removeConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        try{
            airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
            airavataRegistry.removeConfiguration(key, value);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("gfac/urilist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getGFacURIs() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
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
    @Produces("text/plain")
    public Response getEventingServiceURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    @Produces("text/plain")
    public Response getMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    @Produces("text/plain")
    public Response addGFacURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI gfacURI = new URI(uri);
            airavataRegistry.addGFacURI(gfacURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/workflowinterpreteruri")
    @Produces("text/plain")
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI interpreterURI = new URI(uri);
            airavataRegistry.addWorkflowInterpreterURI(interpreterURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/eventinguri")
    @Produces("text/plain")
    public Response setEventingURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI eventingURI = new URI(uri);
            airavataRegistry.setEventingURI(eventingURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/msgboxuri")
    @Produces("text/plain")
    public Response setMessageBoxURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI msgBoxURI = new URI(uri);
            airavataRegistry.setMessageBoxURI(msgBoxURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/gfacuri/date")
    @Produces("text/plain")
    public Response addGFacURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI gfacURI = new URI(uri);
            airavataRegistry.addGFacURI(gfacURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/workflowinterpreteruri/date")
    @Produces("text/plain")
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI interpreterURI = new URI(uri);
            airavataRegistry.addWorkflowInterpreterURI(interpreterURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/eventinguri/date")
    @Produces("text/plain")
    public Response setEventingURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI eventingURI = new URI(uri);
            airavataRegistry.setEventingURI(eventingURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/msgboxuri/date")
    @Produces("text/plain")
    public Response setMessageBoxURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            URI msgBoxURI = new URI(uri);
            airavataRegistry.setMessageBoxURI(msgBoxURI, formattedDate);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/gfacuri")
    @Produces("text/plain")
    public Response removeGFacURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI gfacURI = new URI(uri);
            airavataRegistry.removeGFacURI(gfacURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allgfacuris")
    @Produces("text/plain")
    public Response removeAllGFacURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.removeAllGFacURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/workflowinterpreteruri")
    @Produces("text/plain")
    public Response removeWorkflowInterpreterURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            URI intURI = new URI(uri);
            airavataRegistry.removeWorkflowInterpreterURI(intURI);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/allworkflowinterpreteruris")
    @Produces("text/plain")
    public Response removeAllWorkflowInterpreterURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.removeAllWorkflowInterpreterURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/eventinguri")
    @Produces("text/plain")
    public Response unsetEventingURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.unsetEventingURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @DELETE
    @Path("delete/msgboxuri")
    @Produces("text/plain")
    public Response unsetMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.unsetMessageBoxURI();
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (Exception e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    /**---------------------------------Descriptor Registry----------------------------------**/


    @GET
    @Path("hostdescriptor/exist")
    @Produces("text/plain")
    public Response isHostDescriptorExists(@QueryParam("descriptorName") String descriptorName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        boolean state;
        try{
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
    @Path("hostdescriptor/save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/plain")
    public Response addHostDescriptor(@FormParam("host") String host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            HostDescription hostDescription = HostDescription.fromXML(host);
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/plain")
    public Response updateHostDescriptor(@FormParam("host") String host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            HostDescription hostDescription = HostDescription.fromXML(host);
            airavataRegistry.updateHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
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
    @Produces("text/xml")
    public Response getHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getHostDescriptor(hostName).toXML();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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
    @Produces("text/plain")
    public Response removeHostDescriptor(@QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.removeHostDescriptor(hostName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (DescriptorDoesNotExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }


    @GET
    @Path("get/hostdescriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptors(){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try {
            List<HostDescription> hostDescriptionList = airavataRegistry.getHostDescriptors();
            HostDescriptionList list = new HostDescriptionList();
            HostDescription[] hostDescriptions = new HostDescription[hostDescriptionList.size()];
            for (int i = 0; i < hostDescriptionList.size(); i++) {
                hostDescriptions[i] = hostDescriptionList.get(i);
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

    public Response getHostDescriptorMetadata(String s) {
        return null;
    }

    @GET
    @Path("servicedescriptor/exist")
    @Produces("text/plain")
    public Response isServiceDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        boolean state;
        try{
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response addServiceDescriptor(@FormParam("service") String service){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            airavataRegistry.addServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("servicedescriptor/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response updateServiceDescriptor(@FormParam("service") String service) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            airavataRegistry.updateServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("servicedescriptor/description")
    @Produces("text/xml")
    public Response getServiceDescriptor(@QueryParam("serviceName") String serviceName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getHostDescriptor(serviceName).toXML();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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
    @Produces("text/plain")
    public Response removeServiceDescriptor(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.removeServiceDescriptor(serviceName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        }catch (DescriptorDoesNotExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/servicedescriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptors(){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try {
            List<ServiceDescription> serviceDescriptors = airavataRegistry.getServiceDescriptors();
            ServiceDescriptionList list = new ServiceDescriptionList();
            ServiceDescription[] serviceDescriptions = new ServiceDescription[serviceDescriptors.size()];
            for (int i = 0; i < serviceDescriptors.size(); i++) {
                serviceDescriptions[i] = serviceDescriptors.get(i);
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
        }    }

    public Response getServiceDescriptorMetadata(String s){
        return null;
    }


    @GET
    @Path("applicationdescriptor/exist")
    @Produces("text/plain")
    public Response isApplicationDescriptorExists(@QueryParam("serviceName")String serviceName,
                                                  @QueryParam("hostName")String hostName,
                                                  @QueryParam("descriptorName")String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        boolean state;
        try{
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
    @Path("applicationdescriptor/build/save/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response addApplicationDescriptor(@FormParam("service") String service,
                                         @FormParam("host") String host,
                                         @FormParam("application") String application){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            String serviceName = serviceDescription.getType().getName();
            HostDescription hostDescription = HostDescription.fromXML(host);
            String hostName = hostDescription.getType().getHostName();
            ApplicationDeploymentDescription applicationDeploymentDescription = ApplicationDeploymentDescription.fromXML(application);
            airavataRegistry.addApplicationDescriptor(serviceName, hostName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("applicationdescriptor/save")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response addApplicationDesc(@FormParam("serviceName") String serviceName,
                                       @FormParam("hostName") String hostName,
                                       @FormParam("application") String application) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ApplicationDeploymentDescription applicationDeploymentDescription = ApplicationDeploymentDescription.fromXML(application);
            airavataRegistry.addApplicationDescriptor(serviceName, hostName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    @POST
    @Path("applicationdescriptor/update/descriptor")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response udpateApplicationDescriptorByDescriptors(@FormParam("service") String service,
                                                             @FormParam("host") String host,
                                                             @FormParam("application") String application) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            HostDescription hostDescription = HostDescription.fromXML(host);
            ApplicationDeploymentDescription applicationDeploymentDescription = ApplicationDeploymentDescription.fromXML(application);
            airavataRegistry.udpateApplicationDescriptor(serviceDescription, hostDescription, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    @POST
    @Path("applicationdescriptor/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/xml")
    public Response updateApplicationDescriptor(@FormParam("serviceName") String serviceName,
                                                @FormParam("hostName")String hostName,
                                                @FormParam("application") String application){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            ApplicationDeploymentDescription applicationDeploymentDescription = ApplicationDeploymentDescription.fromXML(application);
            airavataRegistry.updateApplicationDescriptor(serviceName, hostName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (XmlException e) {
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getApplicationDescriptor(serviceName, hostName, applicationName).toXML();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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
    @Path("applicationdescriptor/host/descriptors")
    @Produces("text/xml")
    public Response getApplicationDescriptors(@QueryParam("serviceName")String serviceName,
                                              @QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getApplicationDescriptors(serviceName, hostName).toXML();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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
    @Path("applicationdescriptor/descriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Map<String, ApplicationDeploymentDescription> applicationDescriptors = airavataRegistry.getApplicationDescriptors(serviceName);
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            applicationDescriptorList.setMap(applicationDescriptors);
            if(applicationDescriptors.size() != 0){
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
    @Path("applicationdescriptor/all/descriptors")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getApplicationDescriptors(){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Map<String[], ApplicationDeploymentDescription> applicationDescriptors = airavataRegistry.getApplicationDescriptors();
            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
            applicationDescriptorList.setApplicationDeploymentDescriptionMap(applicationDescriptors);
            if(applicationDescriptors.size() != 0){
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
    @Produces("text/plain")
    public Response removeApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                                @QueryParam("hostName") String hostName,
                                                @QueryParam("appName") String appName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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

    public Response getApplicationDescriptorMetadata(String s, String s1, String s2) {
        return null;
    }

    /**---------------------------------Project Registry----------------------------------**/
    @GET
    @Path("project/exist")
    @Produces("text/plain")
    public Response isWorkspaceProjectExists(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            boolean result = airavataRegistry.isWorkspaceProjectExists(projectName);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("project/exist")
    @Produces("text/plain")
    public Response isWorkspaceProjectExists(@FormParam("projectName") String projectName,
                                             @FormParam("createIfNotExists") String createIfNotExists) {
        boolean createIfNotExistStatus = false;
        if(createIfNotExists.equals("true")){
            createIfNotExistStatus = true;
        }
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            boolean result = airavataRegistry.isWorkspaceProjectExists(projectName, createIfNotExistStatus);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("add/project")
    @Produces("text/plain")
    public Response addWorkspaceProject(@FormParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    @Produces("text/plain")
    public Response updateWorkspaceProject(@FormParam("projectName") String projectName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    @Produces("text/plain")
    public Response deleteWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    @Produces("text/plain")
    public Response getWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkspaceProject workspaceProject = airavataRegistry.getWorkspaceProject(projectName);
            if(workspaceProject != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workspaceProject);
                return builder.build();
            } else{
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkspaceProject> workspaceProjects = airavataRegistry.getWorkspaceProjects();
            WorkspaceProjectList workspaceProjectList = new WorkspaceProjectList();
            WorkspaceProject[] workspaceProjectSet = new WorkspaceProject[workspaceProjects.size()];
            for(int i = 0; i < workspaceProjects.size(); i++) {
                workspaceProjectSet[i] = workspaceProjects.get(i);
            }
            workspaceProjectList.setWorkspaceProjects(workspaceProjectSet);
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

    @DELETE
    @Path("delete/experiment")
    @Produces("text/plain")
    public Response removeExperiment(@QueryParam("experimentId") String experimentId){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<AiravataExperiment> experiments = airavataRegistry.getExperiments();
            ExperimentList experimentList = new ExperimentList();
            AiravataExperiment[] airavataExperiments = new AiravataExperiment[experiments.size()];
            for (int i =0; i < experiments.size(); i++){
                airavataExperiments[i] = experiments.get(i);
            }
            experimentList.setExperiments(airavataExperiments);
            if(experiments.size() != 0){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(experimentList);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
                return builder.build();
            }
        }catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("get/experiments/project")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<AiravataExperiment> experiments = airavataRegistry.getExperiments(projectName);
            ExperimentList experimentList = new ExperimentList();
            AiravataExperiment[] airavataExperiments = new AiravataExperiment[experiments.size()];
            for (int i =0; i < experiments.size(); i++){
                airavataExperiments[i] = experiments.get(i);
            }
            experimentList.setExperiments(airavataExperiments);
            if(experiments.size() != 0){
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByDate(@QueryParam("fromDate") Date fromDate,
                                         @QueryParam("toDate") Date toDate) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<AiravataExperiment> experiments = airavataRegistry.getExperiments(fromDate, toDate);
            ExperimentList experimentList = new ExperimentList();
            AiravataExperiment[] airavataExperiments = new AiravataExperiment[experiments.size()];
            for (int i =0; i < experiments.size(); i++){
                airavataExperiments[i] = experiments.get(i);
            }
            experimentList.setExperiments(airavataExperiments);
            if(experiments.size() != 0){
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
    @Path("get/experiments/project/date")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getExperimentsByProjectDate(@QueryParam("projectName") String projectName,
                                                @QueryParam("fromDate") Date fromDate,
                                                @QueryParam("toDate") Date toDate) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<AiravataExperiment> experiments = airavataRegistry.getExperiments(projectName, fromDate, toDate);
            ExperimentList experimentList = new ExperimentList();
            AiravataExperiment[] airavataExperiments = new AiravataExperiment[experiments.size()];
            for (int i =0; i < experiments.size(); i++){
                airavataExperiments[i] = experiments.get(i);
            }
            experimentList.setExperiments(airavataExperiments);
            if(experiments.size() != 0){
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

/*    @POST
    @Path("add/experiment")
    @Produces("text/plain")
    public Response addExperiment(@FormParam("projectName") String projectName,
                                  @FormParam("experimentID") AiravataExperiment experiment){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
        }

    }*/

    @GET
    @Path("experiment/exist/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isExperimentExists(@QueryParam("experimentId") String experimentId) throws RegistryException {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.isExperimentExists(experimentId);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (ExperimentDoesNotExistsException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @GET
    @Path("experiment/exist/create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isExperimentExistsThenCreate(@QueryParam("experimentId") String experimentId,
                                                 @QueryParam("createIfNotPresent") boolean createIfNotPresent){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            airavataRegistry.isExperimentExists(experimentId, createIfNotPresent);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.NOT_FOUND);
            return builder.build();
        }
    }

    @POST
    @Path("update/experiment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateExperimentExecutionUser(@QueryParam("experimentId") String experimentId,
                                                  @QueryParam("user") String user) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Boolean result = airavataRegistry.updateExperimentExecutionUser(experimentId,user);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    @GET
    @Path("experiment/executionuser")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentExecutionUser(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String user = airavataRegistry.getExperimentExecutionUser(experimentId);
            if(user != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(user);
                return builder.build();
            }else{
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @GET
    @Path("experiment/name")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentName(@QueryParam("experimentID") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getExperimentName(experimentId);
            if(result != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            }else{
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
    public Response updateExperimentName(@QueryParam("experimentId") String experimentId,
                                         @QueryParam("experimentName") String experimentName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Boolean result = airavataRegistry.updateExperimentName(experimentId, experimentName);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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


    @GET
    @Path("get/experimentmetadata")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentMetadata(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getExperimentMetadata(experimentId);
            if(result != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            }else{
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
    public Response updateExperimentMetadata(@FormParam("experimentId")String experimentId,
                                             @FormParam("metadata") String metadata) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Boolean result = airavataRegistry.updateExperimentMetadata(experimentId, metadata);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    @GET
    @Path("get/workflowtemplatename")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflowExecutionTemplateName(@QueryParam("workflowInstanceId") String workflowInstanceId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            String result = airavataRegistry.getWorkflowExecutionTemplateName(workflowInstanceId);
            if(result != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            }else{
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
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
    public Response getExperimentWorkflowInstances(@QueryParam("experimentId") String experimentId){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkflowInstance> experimentWorkflowInstances = airavataRegistry.getExperimentWorkflowInstances(experimentId);
            WorkflowInstancesList workflowInstancesList = new WorkflowInstancesList();
            WorkflowInstance[] workflowInstances = new WorkflowInstance[experimentWorkflowInstances.size()];
            for (int i=0; i<experimentWorkflowInstances.size(); i++){
                workflowInstances[i] = experimentWorkflowInstances.get(i);
            }
            workflowInstancesList.setWorkflowInstances(workflowInstances);
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
    public Response isWorkflowInstanceExists(@QueryParam("instanceId") String instanceId){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Boolean result = airavataRegistry.isWorkflowInstanceExists(instanceId);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    @GET
    @Path("workflowinstance/exist/create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isWorkflowInstanceExistsThenCreate(@QueryParam("instanceId") String instanceId,
                                                       @QueryParam("createIfNotPresent") boolean createIfNotPresent){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            Boolean result = airavataRegistry.isWorkflowInstanceExists(instanceId, createIfNotPresent);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkflowInstanceStatus.ExecutionStatus status = WorkflowInstanceStatus.ExecutionStatus.valueOf(executionStatus);
            Boolean result = airavataRegistry.updateWorkflowInstanceStatus(instanceId, status);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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
    @Path("update/workflowinstancestatus/experimentid")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowInstanceStatusByExperiment(@FormParam("experimentId") String experimentId,
                                                             @FormParam("workflowInstanceId") String workflowInstanceId,
                                                             @FormParam("executionStatus") String executionStatus,
                                                             @FormParam("statusUpdateTime") Date statusUpdateTime) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkflowInstance workflowInstance =  new WorkflowInstance(experimentId, workflowInstanceId);
            WorkflowInstanceStatus.ExecutionStatus status = WorkflowInstanceStatus.ExecutionStatus.valueOf(executionStatus);
            WorkflowInstanceStatus workflowInstanceStatus = new WorkflowInstanceStatus(workflowInstance, status, statusUpdateTime);
            Boolean result = airavataRegistry.updateWorkflowInstanceStatus(workflowInstanceStatus);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    @GET
    @Path("get/workflowinstancestatus")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflowInstanceStatus(@QueryParam("instanceId") String instanceId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkflowInstanceStatus workflowInstanceStatus = airavataRegistry.getWorkflowInstanceStatus(instanceId);
            if(workflowInstanceStatus != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowInstanceStatus);
                return builder.build();
            }else{
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
    public Response updateWorkflowNodeInput(@FormParam("experimentID") String experimentID,
                                            @FormParam("nodeID") String nodeID,
                                            @FormParam("workflowInstanceID") String workflowInstanceID,
                                            @FormParam("data") String data){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkflowInstance workflowInstance = new WorkflowInstance(experimentID, nodeID);
            WorkflowInstanceNode workflowInstanceNode = new WorkflowInstanceNode(workflowInstance, nodeID);
            boolean result = airavataRegistry.updateWorkflowNodeInput(workflowInstanceNode, data);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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
    @Path("update/workflownodeoutput")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkflowNodeOutput(@FormParam("experimentID") String experimentID,
                                             @FormParam("nodeID") String nodeID,
                                             @FormParam("workflowInstanceID") String workflowInstanceID,
                                             @FormParam("data") String data) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            WorkflowInstance workflowInstance = new WorkflowInstance(experimentID, nodeID);
            WorkflowInstanceNode workflowInstanceNode = new WorkflowInstanceNode(workflowInstance, nodeID);
            boolean result = airavataRegistry.updateWorkflowNodeOutput(workflowInstanceNode, data);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    @GET
    @Path("search/workflowinstancenodeinput")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response searchWorkflowInstanceNodeInput(@QueryParam("experimentIdRegEx") String experimentIdRegEx,
                                                    @QueryParam("workflowNameRegEx") String workflowNameRegEx,
                                                    @QueryParam("nodeNameRegEx") String nodeNameRegEx) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeInput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIOData[] workflowNodeIODataCollection = new WorkflowNodeIOData[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
                workflowNodeIODataCollection[i] = workflowNodeIODataList.get(i);
            }
            workflowNodeIOData.setWorkflowNodeIODatas(workflowNodeIODataCollection);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeOutput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIOData[] workflowNodeIODataCollection = new WorkflowNodeIOData[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
                workflowNodeIODataCollection[i] = workflowNodeIODataList.get(i);
            }
            workflowNodeIOData.setWorkflowNodeIODatas(workflowNodeIODataCollection);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeInput(workflowInstanceId, nodeType);
            WorkflowNodeIOData[] workflowNodeIODataCollection = new WorkflowNodeIOData[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
                workflowNodeIODataCollection[i] = workflowNodeIODataList.get(i);
            }
            workflowNodeIOData.setWorkflowNodeIODatas(workflowNodeIODataCollection);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(AIRAVATA_CONTEXT);
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeOutput(workflowInstanceId, nodeType);
            WorkflowNodeIOData[] workflowNodeIODataCollection = new WorkflowNodeIOData[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
                workflowNodeIODataCollection[i] = workflowNodeIODataList.get(i);
            }
            workflowNodeIOData.setWorkflowNodeIODatas(workflowNodeIODataCollection);
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

