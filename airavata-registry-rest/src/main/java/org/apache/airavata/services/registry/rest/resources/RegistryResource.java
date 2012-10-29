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
import org.apache.airavata.schemas.gfac.impl.InputParameterTypeImpl;
import org.apache.airavata.services.registry.rest.resourcemappings.*;
import org.apache.airavata.registry.api.AiravataExperiment;
import org.apache.airavata.services.registry.rest.resourcemappings.WorkflowInstanceMapping;
import org.apache.airavata.services.registry.rest.utils.DescriptorUtil;
import org.apache.airavata.services.registry.rest.utils.HostTypes;
import org.apache.airavata.services.registry.rest.utils.RestServicesConstants;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.util.AXIOMUtil;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLStreamException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Context
    ServletContext context;

    public String getVersion(){
        return  null;
    }

    protected void initialize() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        jpa = new JPAResourceAccessor(airavataRegistry);
        active=true;
    }

    /**---------------------------------Configuration Registry----------------------------------**/

    @Path("/configuration")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getConfiguration(@QueryParam("key") String key) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response addConfiguration(@FormParam("key") String key,
                                     @FormParam("value") String value,
                                     @FormParam("date") String date) {
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedDate = dateFormat.parse(date);
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllConfiguration(@QueryParam("key") String key) {
        try{
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeConfiguration(@QueryParam("key") String key, @QueryParam("value") String value) {
        try{
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGFacURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response setEventingURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response setMessageBoxURI(@FormParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGFacURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response addWorkflowInterpreterURI(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response setEventingURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response setMessageBoxURIByDate(@FormParam("uri") String uri, @FormParam("date") String date) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeGFacURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllGFacURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeWorkflowInterpreterURI(@QueryParam("uri") String uri) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeAllWorkflowInterpreterURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response unsetEventingURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response unsetMessageBoxURI() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response isHostDescriptorExists(@QueryParam("descriptorName") String descriptorName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addHostDescriptor(@FormParam("hostName") String hostName,
                                      @FormParam("hostAddress") String hostAddress,
                                      @FormParam("hostEndpoint") String hostEndpoint,
                                      @FormParam("gatekeeperEndpoint") String gatekeeperEndpoint) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            HostDescription hostDescription = DescriptorUtil.createHostDescription(hostName, hostAddress, hostEndpoint, gatekeeperEndpoint);
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            // TODO : Use WEbapplicationExcpetion
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    @POST
    @Path("hostdescriptor/save/jason")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addJasonHostDescriptor(HostDescriptor host) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            HostDescription hostDescription = new HostDescription();
            hostDescription.getType().setHostAddress(host.getHostAddress());
            hostDescription.getType().setHostName(host.getHostname());
            if(host.getHostType().equals(HostTypes.GLOBUS_HOST_TYPE)){
                ((GlobusHostType)hostDescription.getType()).addGlobusGateKeeperEndPoint(host.getGlobusGateKeeperEndPoint().get(0));
                ((GlobusHostType)hostDescription.getType()).addGridFTPEndPoint(host.getGridFTPEndPoint().get(0));
            }else if (host.getHostType().equals(HostTypes.GSISSH_HOST_TYPE)){
                ((GsisshHostType)hostDescription).addGridFTPEndPoint(host.getGridFTPEndPoint().get(0));
            } else if (host.getHostType().equals(HostTypes.EC2_HOST_TYPE)){
                ((Ec2HostType)hostDescription).addImageID(host.getImageID().get(0));
                ((Ec2HostType)hostDescription).addInstanceID(host.getInstanceID().get(0));
            }
            airavataRegistry.addHostDescriptor(hostDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
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
        try{
            HostDescription hostDescription = new HostDescription();
            hostDescription.getType().setHostAddress(host.getHostAddress());
            hostDescription.getType().setHostName(host.getHostname());
            if(host.getHostType().equals(HostTypes.GLOBUS_HOST_TYPE)){
                ((GlobusHostType)hostDescription.getType()).addGlobusGateKeeperEndPoint(host.getGlobusGateKeeperEndPoint().get(0));
                ((GlobusHostType)hostDescription.getType()).addGridFTPEndPoint(host.getGridFTPEndPoint().get(0));
            }else if (host.getHostType().equals(HostTypes.GSISSH_HOST_TYPE)){
                ((GsisshHostType)hostDescription).addGridFTPEndPoint(host.getGridFTPEndPoint().get(0));
            } else if (host.getHostType().equals(HostTypes.EC2_HOST_TYPE)){
                ((Ec2HostType)hostDescription).addImageID(host.getImageID().get(0));
                ((Ec2HostType)hostDescription).addInstanceID(host.getInstanceID().get(0));
            }
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
        try{
            List<String> hostType = new ArrayList<String>();
            List<String> gridFTPEndPoint = new ArrayList<String>();
            List<String> globusGateKeeperEndPoint  = new ArrayList<String>();
            List<String> imageID  = new ArrayList<String>();
            List<String> instanceID  = new ArrayList<String>();

            HostDescription hostDescription = airavataRegistry.getHostDescriptor(hostName);
            HostDescriptor hostDescriptor = new HostDescriptor();
            hostDescriptor.setHostname(hostDescription.getType().getHostName());
            hostDescriptor.setHostAddress(hostDescription.getType().getHostAddress());

            HostDescriptionType hostDescriptionType = hostDescription.getType();
            if (hostDescriptionType instanceof GlobusHostType){
                GlobusHostType globusHostType = (GlobusHostType) hostDescriptionType;
                hostType.add(HostTypes.GLOBUS_HOST_TYPE);
                String[] globusGateKeeperEndPointArray = globusHostType.getGlobusGateKeeperEndPointArray();
                for (int i = 0; i < globusGateKeeperEndPointArray.length ; i++){
                    globusGateKeeperEndPoint.add(globusGateKeeperEndPointArray[i]);
                }

                String[] gridFTPEndPointArray = globusHostType.getGridFTPEndPointArray();
                for (int i = 0; i < gridFTPEndPointArray.length ; i++){
                    gridFTPEndPoint.add(globusGateKeeperEndPointArray[i]);
                }

            }else if (hostDescriptionType instanceof GsisshHostType){
                GsisshHostType gsisshHostType = (GsisshHostType) hostDescriptionType;
                hostType.add(HostTypes.GSISSH_HOST_TYPE);

                String[] gridFTPEndPointArray = gsisshHostType.getGridFTPEndPointArray();
                for (int i = 0; i < gridFTPEndPointArray.length ; i++){
                    gridFTPEndPoint.add(gridFTPEndPointArray[i]);
                }
            }  else if (hostDescriptionType instanceof  Ec2HostType) {
                Ec2HostType ec2HostType = (Ec2HostType) hostDescriptionType;
                hostType.add(HostTypes.EC2_HOST_TYPE);

                String[] imageIDArray = ec2HostType.getImageIDArray();
                for (int i = 0; i < imageIDArray.length ; i++){
                    imageID.add(imageIDArray[i]);
                }

                String[] instanceIDArray = ec2HostType.getInstanceIDArray();
                for (int i = 0; i < instanceIDArray.length ; i++){
                    instanceID.add(instanceIDArray[i]);
                }
            } else {
                hostType.add(HostTypes.HOST_DESCRIPTION_TYPE);
            }
            hostDescriptor.setGlobusGateKeeperEndPoint(globusGateKeeperEndPoint);
            hostDescriptor.setGridFTPEndPoint(gridFTPEndPoint);
            hostDescriptor.setImageID(imageID);
            hostDescriptor.setInstanceID(instanceID);
            hostDescriptor.setHostType(hostType);
            if (hostDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(hostDescription);
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getHostDescriptors() {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<HostDescription> hostDescriptionList = airavataRegistry.getHostDescriptors();
            HostDescriptionList list = new HostDescriptionList();
            HostDescriptor[] hostDescriptions = new HostDescriptor[hostDescriptionList.size()];
            for (int i = 0; i < hostDescriptionList.size(); i++) {
                List<String> hostType = new ArrayList<String>();
                List<String> gridFTPEndPoint = new ArrayList<String>();
                List<String> globusGateKeeperEndPoint  = new ArrayList<String>();
                List<String> imageID  = new ArrayList<String>();
                List<String> instanceID  = new ArrayList<String>();
                HostDescriptor hostDescriptor = new HostDescriptor();
                hostDescriptor.setHostname(hostDescriptionList.get(i).getType().getHostName());
                hostDescriptor.setHostAddress(hostDescriptionList.get(i).getType().getHostAddress());

                HostDescriptionType hostDescriptionType = hostDescriptionList.get(i).getType();
                if (hostDescriptionType instanceof GlobusHostType){
                    GlobusHostType globusHostType = (GlobusHostType) hostDescriptionType;
                    hostType.add(HostTypes.GLOBUS_HOST_TYPE);
                    String[] globusGateKeeperEndPointArray = globusHostType.getGlobusGateKeeperEndPointArray();
                    for (int j = 0; j < globusGateKeeperEndPointArray.length ; j++){
                        globusGateKeeperEndPoint.add(globusGateKeeperEndPointArray[j]);
                    }

                    String[] gridFTPEndPointArray = globusHostType.getGridFTPEndPointArray();
                    for (int j = 0; j < gridFTPEndPointArray.length ; j++){
                        gridFTPEndPoint.add(globusGateKeeperEndPointArray[j]);
                    }

                }else if (hostDescriptionType instanceof GsisshHostType){
                    GsisshHostType gsisshHostType = (GsisshHostType) hostDescriptionType;
                    hostType.add(HostTypes.GSISSH_HOST_TYPE);

                    String[] gridFTPEndPointArray = gsisshHostType.getGridFTPEndPointArray();
                    for (int j = 0; j < gridFTPEndPointArray.length ; j++){
                        gridFTPEndPoint.add(gridFTPEndPointArray[j]);
                    }
                }  else if (hostDescriptionType instanceof  Ec2HostType) {
                    Ec2HostType ec2HostType = (Ec2HostType) hostDescriptionType;
                    hostType.add(HostTypes.EC2_HOST_TYPE);

                    String[] imageIDArray = ec2HostType.getImageIDArray();
                    for (int j = 0; j < imageIDArray.length ; j++){
                        imageID.add(imageIDArray[j]);
                    }

                    String[] instanceIDArray = ec2HostType.getInstanceIDArray();
                    for (int j = 0; j < instanceIDArray.length ; j++){
                        instanceID.add(instanceIDArray[j]);
                    }
                } else {
                    hostType.add(HostTypes.HOST_DESCRIPTION_TYPE);
                }
                hostDescriptor.setGlobusGateKeeperEndPoint(globusGateKeeperEndPoint);
                hostDescriptor.setGridFTPEndPoint(gridFTPEndPoint);
                hostDescriptor.setImageID(imageID);
                hostDescriptor.setInstanceID(instanceID);
                hostDescriptor.setHostType(hostType);

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

    public Response getHostDescriptorMetadata(String s) {
        return null;
    }

    @GET
    @Path("servicedescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isServiceDescriptorExists(@QueryParam("descriptorName") String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addServiceDescriptor(ServiceDescriptor service){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.getType().setName(service.getServiceName());
            serviceDescription.getType().setDescription(service.getDescription());
            List<ServiceParameters> inputParams = service.getInputParams();
            InputParameterType[] inputParameterTypeArray = new InputParameterType[inputParams.size()];
            for (int i = 0; i < inputParams.size(); i++){
                InputParameterType parameter = InputParameterType.Factory.newInstance();
                parameter.setParameterName(inputParams.get(i).getType());
                parameter.setParameterValueArray(new String[]{inputParams.get(i).getName()});
                ParameterType parameterType = parameter.addNewParameterType();
                parameterType.setType(DataType.Enum.forString(inputParams.get(i).getDataType()));
                parameterType.setName(inputParams.get(i).getDataType());
                parameter.setParameterType(parameterType);
                inputParameterTypeArray[i] = parameter;
            }
            serviceDescription.getType().setInputParametersArray(inputParameterTypeArray);

            List<ServiceParameters> outputParams = service.getOutputParams();
            OutputParameterType[] outputParameterTypeArray = new OutputParameterType[outputParams.size()];
            for (int i = 0; i < outputParams.size(); i++){
                OutputParameterType parameter = OutputParameterType.Factory.newInstance();
                parameter.setParameterName(outputParams.get(i).getType());
                parameter.setParameterName(outputParams.get(i).getName());
                ParameterType parameterType = parameter.addNewParameterType();
                parameterType.setType(DataType.Enum.forString(outputParams.get(i).getDataType()));
                parameterType.setName(outputParams.get(i).getDataType());
                parameter.setParameterType(parameterType);
                outputParameterTypeArray[i] = parameter;
            }
            serviceDescription.getType().setOutputParametersArray(outputParameterTypeArray);
            airavataRegistry.addServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
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
        try{
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.getType().setName(service.getServiceName());
            serviceDescription.getType().setDescription(service.getDescription());
            List<ServiceParameters> inputParams = service.getInputParams();
            InputParameterType[] inputParameterTypeArray = new InputParameterType[inputParams.size()];
            for (int i = 0; i < inputParams.size(); i++){
                InputParameterType parameter = InputParameterType.Factory.newInstance();
                parameter.setParameterName(inputParams.get(i).getType());
                parameter.setParameterValueArray(new String[]{inputParams.get(i).getName()});
                ParameterType parameterType = parameter.addNewParameterType();
                parameterType.setType(DataType.Enum.forString(inputParams.get(i).getDataType()));
                parameterType.setName(inputParams.get(i).getDataType());
                parameter.setParameterType(parameterType);
                inputParameterTypeArray[i] = parameter;
            }
            serviceDescription.getType().setInputParametersArray(inputParameterTypeArray);

            List<ServiceParameters> outputParams = service.getOutputParams();
            OutputParameterType[] outputParameterTypeArray = new OutputParameterType[outputParams.size()];
            for (int i = 0; i < outputParams.size(); i++){
                OutputParameterType parameter = OutputParameterType.Factory.newInstance();
                parameter.setParameterName(outputParams.get(i).getType());
                parameter.setParameterName(outputParams.get(i).getName());
                ParameterType parameterType = parameter.addNewParameterType();
                parameterType.setType(DataType.Enum.forString(outputParams.get(i).getDataType()));
                parameterType.setName(outputParams.get(i).getDataType());
                parameter.setParameterType(parameterType);
                outputParameterTypeArray[i] = parameter;
            }
            serviceDescription.getType().setOutputParametersArray(outputParameterTypeArray);
            airavataRegistry.updateServiceDescriptor(serviceDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
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
    public Response getServiceDescriptor(@QueryParam("serviceName") String serviceName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            ServiceDescription serviceDescription = airavataRegistry.getServiceDescriptor(serviceName);
            ServiceDescriptor serviceDescriptor = new ServiceDescriptor();
            serviceDescriptor.setServiceName(serviceDescription.getType().getName());
            serviceDescriptor.setDescription(serviceDescription.getType().getDescription());
            InputParameterType[] inputParametersArray = serviceDescription.getType().getInputParametersArray();
            OutputParameterType[] outputParametersArray = serviceDescription.getType().getOutputParametersArray();
            List<ServiceParameters> inputParams = new ArrayList<ServiceParameters>();
            List<ServiceParameters> outputParams = new ArrayList<ServiceParameters>();

            for (int i = 0; i < inputParametersArray.length; i++){
                ServiceParameters serviceParameters = new ServiceParameters();
                serviceParameters.setType(inputParametersArray[i].getParameterName());
                serviceParameters.setName(inputParametersArray[i].getParameterValueArray().toString());
                serviceParameters.setDescription(inputParametersArray[i].getParameterDescription());
                serviceParameters.setDataType(inputParametersArray[i].getParameterType().getType().toString());
                inputParams.add(serviceParameters);
            }
            serviceDescriptor.setInputParams(inputParams);

            for (int i = 0; i < outputParametersArray.length; i++){
                ServiceParameters serviceParameters = new ServiceParameters();
                serviceParameters.setType(outputParametersArray[i].getParameterName());
                serviceParameters.setName(outputParametersArray[i].getParameterName());
                serviceParameters.setDescription(outputParametersArray[i].getParameterDescription());
                serviceParameters.setDataType(outputParametersArray[i].getParameterType().getType().toString());
                inputParams.add(serviceParameters);
            }
            serviceDescriptor.setOutputParams(outputParams);

            if (serviceDescription != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(serviceDescription);
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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getServiceDescriptors(){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try {
            List<ServiceDescription> serviceDescriptors = airavataRegistry.getServiceDescriptors();
            ServiceDescriptionList list = new ServiceDescriptionList();
            ServiceDescriptor[] serviceDescriptions = new ServiceDescriptor[serviceDescriptors.size()];
            for (int i = 0; i < serviceDescriptors.size(); i++) {
                ServiceDescriptor serviceDescriptor = new ServiceDescriptor();
                serviceDescriptor.setServiceName(serviceDescriptors.get(i).getType().getName());
                serviceDescriptor.setDescription(serviceDescriptors.get(i).getType().getDescription());
                InputParameterType[] inputParametersArray = serviceDescriptors.get(i).getType().getInputParametersArray();
                OutputParameterType[] outputParametersArray = serviceDescriptors.get(i).getType().getOutputParametersArray();
                List<ServiceParameters> inputParams = new ArrayList<ServiceParameters>();
                List<ServiceParameters> outputParams = new ArrayList<ServiceParameters>();

                for (int j = 0; j < inputParametersArray.length; j++){
                    ServiceParameters serviceParameters = new ServiceParameters();
                    serviceParameters.setType(inputParametersArray[j].getParameterName());
                    serviceParameters.setName(inputParametersArray[j].getParameterValueArray().toString());
                    serviceParameters.setDescription(inputParametersArray[j].getParameterDescription());
                    serviceParameters.setDataType(inputParametersArray[j].getParameterType().getType().toString());
                    inputParams.add(serviceParameters);
                }
                serviceDescriptor.setInputParams(inputParams);

                for (int j = 0; j < outputParametersArray.length; j++){
                    ServiceParameters serviceParameters = new ServiceParameters();
                    serviceParameters.setType(outputParametersArray[j].getParameterName());
                    serviceParameters.setName(outputParametersArray[j].getParameterName());
                    serviceParameters.setDescription(outputParametersArray[j].getParameterDescription());
                    serviceParameters.setDataType(outputParametersArray[j].getParameterType().getType().toString());
                    inputParams.add(serviceParameters);
                }
                serviceDescriptor.setOutputParams(outputParams);
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
        }    }

    public Response getServiceDescriptorMetadata(String s){
        return null;
    }


    @GET
    @Path("applicationdescriptor/exist")
    @Produces(MediaType.TEXT_PLAIN)
    public Response isApplicationDescriptorExists(@QueryParam("serviceName")String serviceName,
                                                  @QueryParam("hostName")String hostName,
                                                  @QueryParam("descriptorName")String descriptorName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Path("applicationdescriptor/build/save")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addApplicationDescriptor(ApplicationDescriptor applicationDescriptor){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            String hostdescName = applicationDescriptor.getHostdescName();
            if(!airavataRegistry.isHostDescriptorExists(hostdescName)){
                Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
                return builder.build();
            }
            ApplicationDeploymentDescription applicationDeploymentDescription = new ApplicationDeploymentDescription();
            applicationDeploymentDescription.getType().getApplicationName().setStringValue(applicationDescriptor.getApplicationName());
            applicationDeploymentDescription.getType().setExecutableLocation(applicationDescriptor.getExecutablePath());
            applicationDeploymentDescription.getType().setOutputDataDirectory(applicationDescriptor.getWorkingDir());

            //set advanced options according app desc type


            String serviceName = applicationDescriptor.getServiceDesc().getServiceName();
            airavataRegistry.addApplicationDescriptor(serviceName, hostdescName, applicationDeploymentDescription);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (DescriptorAlreadyExistsException e){
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }



    @POST
    @Path("applicationdescriptor/save")
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addApplicationDesc(@FormParam("serviceName") String serviceName,
                                       @FormParam("hostName") String hostName,
                                       String application) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response udpateApplicationDescriptorByDescriptors(String service,
                                                             String host,
                                                             String application) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Consumes(MediaType.TEXT_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateApplicationDescriptor(@FormParam("serviceName") String serviceName,
                                                @FormParam("hostName")String hostName,
                                                String application){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Path("applicationdescriptors/alldescriptors/host/service")
    @Produces("text/xml")
    public Response getApplicationDescriptors(@QueryParam("serviceName")String serviceName,
                                              @QueryParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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

//    @GET
//    @Path("applicationdescriptor/alldescriptors/service")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Response getApplicationDescriptors(@QueryParam("serviceName") String serviceName) {
//        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
//        try{
//            Map<String, ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors(serviceName);
//            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
//            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
//            int i = 0;
//            for(String hostName : applicationDeploymentDescriptionMap.keySet()){
//                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(hostName);
//                ApplicationDescriptor applicationDescriptor = new ApplicationDescriptor();
//                applicationDescriptor.setHostdescName(hostName);
//                applicationDescriptor.setAppDocument(applicationDeploymentDescription.toXML());
//                applicationDescriptors[i] = applicationDescriptor;
//                i++;
//            }
//            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
//            if(applicationDeploymentDescriptionMap.size() != 0){
//                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
//                builder.entity(applicationDescriptorList);
//                return builder.build();
//            } else {
//                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
//                return builder.build();
//            }
//        } catch (MalformedDescriptorException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//            return builder.build();
//        } catch (RegistryException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//            return builder.build();
//        }
//    }

//    @GET
//    @Path("applicationdescriptor/alldescriptors")
//    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public Response getApplicationDescriptors(){
//        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
//        try{
//            Map<String[], ApplicationDeploymentDescription> applicationDeploymentDescriptionMap = airavataRegistry.getApplicationDescriptors();
//            ApplicationDescriptorList applicationDescriptorList = new ApplicationDescriptorList();
//            ApplicationDescriptor[] applicationDescriptors = new ApplicationDescriptor[applicationDeploymentDescriptionMap.size()];
//            int i = 0;
//            for (String[] descriptors : applicationDeploymentDescriptionMap.keySet()){
//                ApplicationDescriptor applicationDescriptor = new ApplicationDescriptor();
//                ApplicationDeploymentDescription applicationDeploymentDescription = applicationDeploymentDescriptionMap.get(descriptors);
//                applicationDescriptor.setServiceDescName(descriptors[0]);
//                applicationDescriptor.setHostdescName(descriptors[1]);
//                applicationDescriptor.setAppDocument(applicationDeploymentDescription.toXML());
//                applicationDescriptors[i] = applicationDescriptor;
//                i++;
//            }
//            applicationDescriptorList.setApplicationDescriptors(applicationDescriptors);
//            if(applicationDeploymentDescriptionMap.size() != 0){
//                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
//                builder.entity(applicationDescriptorList);
//                return builder.build();
//            } else {
//                Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
//                return builder.build();
//            }
//        } catch (MalformedDescriptorException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//            return builder.build();
//        } catch (RegistryException e) {
//            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
//            return builder.build();
//        }
//    }

    @DELETE
    @Path("applicationdescriptor/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeApplicationDescriptor(@QueryParam("serviceName") String serviceName,
                                                @QueryParam("hostName") String hostName,
                                                @QueryParam("appName") String appName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
        public Response isWorkspaceProjectExists(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        if(createIfNotExists.equals("true")){
            createIfNotExistStatus = true;
        }
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateWorkspaceProject(@FormParam("projectName") String projectName){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getWorkspaceProject(@QueryParam("projectName") String projectName) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            WorkspaceProject workspaceProject = airavataRegistry.getWorkspaceProject(projectName);
            if(workspaceProject != null){
                WorkspaceProjectMapping workspaceProjectMapping = new WorkspaceProjectMapping(workspaceProject.getProjectName());

                List<AiravataExperiment> airavataExperimentList = workspaceProject.getProjectsRegistry().getExperiments(workspaceProject.getProjectName());
                Experiment[] experiments = new Experiment[airavataExperimentList.size()];
                if(airavataExperimentList.size() != 0){
                    for (int i = 0; i < airavataExperimentList.size(); i++){
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            List<WorkspaceProject> workspaceProjects = airavataRegistry.getWorkspaceProjects();
            WorkspaceProjectList workspaceProjectList = new WorkspaceProjectList();
            WorkspaceProjectMapping[] workspaceProjectSet = new WorkspaceProjectMapping[workspaceProjects.size()];
            for(int i = 0; i < workspaceProjects.size(); i++) {
                WorkspaceProject workspaceProject = workspaceProjects.get(i);
                WorkspaceProjectMapping workspaceProjectMapping = new WorkspaceProjectMapping(workspaceProject.getProjectName());

                List<AiravataExperiment> airavataExperimentList = workspaceProject.getProjectsRegistry().getExperiments(workspaceProject.getProjectName());
                Experiment[] experiments = new Experiment[airavataExperimentList.size()];
                if(airavataExperimentList.size() != 0){
                    for (int j = 0; j < airavataExperimentList.size(); j++){
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

    @DELETE
    @Path("delete/experiment")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeExperiment(@QueryParam("experimentId") String experimentId){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments();
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i =0; i < airavataExperimentList.size(); i++){
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
            if(airavataExperimentList.size() != 0){
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(projectName);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i =0; i < airavataExperimentList.size(); i++){
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
            if(airavataExperimentList.size() != 0){
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
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedFromDate = dateFormat.parse(fromDate);
            Date formattedToDate = dateFormat.parse(toDate);
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(formattedFromDate, formattedToDate);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i =0; i < airavataExperimentList.size(); i++){
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
            if(airavataExperimentList.size() != 0){
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
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date formattedFromDate = dateFormat.parse(fromDate);
            Date formattedToDate = dateFormat.parse(toDate);
            List<AiravataExperiment> airavataExperimentList = airavataRegistry.getExperiments(projectName, formattedFromDate, formattedToDate);
            ExperimentList experimentList = new ExperimentList();
            Experiment[] experiments = new Experiment[airavataExperimentList.size()];
            for (int i =0; i < airavataExperimentList.size(); i++){
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
            if(airavataExperimentList.size() != 0){
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
                                  @FormParam("submittedDate") String submittedDate){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            AiravataExperiment experiment = new AiravataExperiment();
            experiment.setExperimentId(experimentID);
            Gateway gateway = (Gateway)context.getAttribute(RestServicesConstants.GATEWAY);
            AiravataUser airavataUser = (AiravataUser)context.getAttribute(RestServicesConstants.REGISTRY_USER);
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
        try{
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
                                                 @QueryParam("createIfNotPresent") String createIfNotPresent){
        boolean createIfNotPresentStatus = false;
        if(createIfNotPresent.equals("true")){
            createIfNotPresentStatus = true;
        }
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        try{
            airavataRegistry.updateExperimentExecutionUser(experimentId,user);
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
    @Path("get/experiment/name")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getExperimentName(@QueryParam("experimentId") String experimentId) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
    public Response updateExperimentName(@FormParam("experimentId") String experimentId,
                                         @FormParam("experimentName") String experimentName){
            airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
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
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
            List<WorkflowInstance> experimentWorkflowInstances = airavataRegistry.getExperimentWorkflowInstances(experimentId);
            WorkflowInstancesList workflowInstancesList = new WorkflowInstancesList();
            WorkflowInstanceMapping[] workflowInstanceMappings = new WorkflowInstanceMapping[experimentWorkflowInstances.size()];
            for (int i=0; i<experimentWorkflowInstances.size(); i++){
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
    public Response isWorkflowInstanceExists(@QueryParam("instanceId") String instanceId){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
                                                       @QueryParam("createIfNotPresent") boolean createIfNotPresent){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        try{
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
        try{
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
        try{
            WorkflowInstanceStatus workflowInstanceStatus = airavataRegistry.getWorkflowInstanceStatus(instanceId);
            WorkflowInstanceStatusMapping workflowInstanceStatusMapping = new WorkflowInstanceStatusMapping();
            workflowInstanceStatusMapping.setExecutionStatus(workflowInstanceStatus.getExecutionStatus().name());
            workflowInstanceStatusMapping.setStatusUpdateTime(workflowInstanceStatus.getStatusUpdateTime());
            WorkflowInstance workflowInstance = workflowInstanceStatus.getWorkflowInstance();
            WorkflowInstanceMapping workflowInstanceMapping = new WorkflowInstanceMapping();
            if(workflowInstance != null){
                workflowInstanceStatusMapping.setExperimentId(workflowInstance.getExperimentId());
                workflowInstanceStatusMapping.setTemplateName(workflowInstance.getTemplateName());
                workflowInstanceStatusMapping.setWorkflowInstanceId(workflowInstance.getWorkflowInstanceId());
            }
//            workflowInstanceStatusMapping.setWorkflowInstanceMapping(workflowInstanceMapping);
            if(workflowInstanceStatusMapping != null){
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(workflowInstanceStatusMapping);
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
    public Response updateWorkflowNodeInput(@FormParam("experimentId") String experimentID,
                                            @FormParam("nodeID") String nodeID,
                                            @FormParam("workflowInstanceId") String workflowInstanceID,
                                            @FormParam("data") String data){
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
                                             @FormParam("workflowInstanceID") String workflowInstanceID,
                                             @FormParam("data") String data) {
        airavataRegistry = (AiravataRegistry2) context.getAttribute(RestServicesConstants.AIRAVATA_REGISTRY);
        try{
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
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeInput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
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
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.searchWorkflowInstanceNodeOutput(experimentIdRegEx, workflowNameRegEx, nodeNameRegEx);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
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
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeInput(workflowInstanceId, nodeType);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
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
        try{
            List<WorkflowNodeIOData> workflowNodeIODataList = airavataRegistry.getWorkflowInstanceNodeOutput(workflowInstanceId, nodeType);
            WorkflowNodeIODataMapping[] workflowNodeIODataCollection = new WorkflowNodeIODataMapping[workflowNodeIODataList.size()];
            WorkflowNodeIODataList workflowNodeIOData = new WorkflowNodeIODataList();
            for (int i = 0; i<workflowNodeIODataList.size(); i++){
                WorkflowNodeIOData nodeIOData = workflowNodeIODataList.get(i);
                WorkflowNodeIODataMapping workflowNodeIODataMapping = new WorkflowNodeIODataMapping();

                workflowNodeIODataMapping.setExperimentId(nodeIOData.getExperimentId());
                workflowNodeIODataMapping.setWorkflowId(nodeIOData.getWorkflowId());
                workflowNodeIODataMapping.setWorkflowInstanceId(nodeIOData.getWorkflowInstanceId());
                workflowNodeIODataMapping.setWorkflowName(nodeIOData.getWorkflowName());
                workflowNodeIODataMapping.setWorkflowNodeType(nodeIOData.getNodeType().toString());
                workflowNodeIODataCollection[i] = workflowNodeIODataMapping;            }
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

