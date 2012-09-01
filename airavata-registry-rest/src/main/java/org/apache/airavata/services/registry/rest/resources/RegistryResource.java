package org.apache.airavata.services.registry.rest.resources;

import org.apache.airavata.common.registry.api.exception.RegistryException;
import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.registry.api.AiravataRegistry;
import org.apache.airavata.registry.api.Axis2Registry;
import org.apache.airavata.registry.api.DataRegistry;
import org.apache.airavata.registry.api.workflow.WorkflowExecution;
import org.apache.airavata.registry.api.workflow.WorkflowIOData;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceStatus;
import org.apache.airavata.registry.api.workflow.WorkflowServiceIOData;
import org.apache.xmlbeans.XmlException;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Path("/airavataRegistry/api")
public class RegistryResource {
    private AiravataRegistry airavataRegistry;
    private Axis2Registry axis2Registry;
    private DataRegistry dataRegistry;

    @Context
    ServletContext context;

    @Path("/userName")
    @GET
    @Produces("text/plain")
    public String getUserName() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getUsername();
    }

    @Path("/repositoryUrl")
    @GET
    @Produces("text/plain")
    public String getRepositoryURI() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getRepositoryURI().toString();
    }

    @Path("/repositoryName")
    @GET
    @Produces("text/plain")
    public String getName() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getName();
    }

    @Path("/service/wsdl")
    @GET
    @Produces("text/xml")
    public Response getWSDL(@QueryParam("serviceName") String serviceName) {
        axis2Registry = (Axis2Registry) context.getAttribute("axis2Registry");
        try {
            String result = axis2Registry.getWSDL(serviceName);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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


    //need to check the name
    @Path("/service/description/wsdl")
    @GET
    @Produces("text/xml")
    public Response getWSDLFromServiceDescription(@FormParam("service") String service) {
        axis2Registry = (Axis2Registry) context.getAttribute("axis2Registry");
        try {
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            String result = axis2Registry.getWSDL(serviceDescription);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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



    @Path("/service/description")
    @GET
    @Produces("text/xml")
    public String getServiceDescription(@QueryParam("serviceID") String serviceId) throws RegistryException {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getServiceDescription(serviceId).toXML();
    }

    @Path("/service/deploymentDescription")
    @GET
    @Produces("text/xml")
    public String getDeploymentDescription(@QueryParam("serviceID") String serviceId,
                                           @QueryParam("hostId") String hostId)
            throws RegistryException {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getDeploymentDescription(serviceId, hostId).toXML();
    }

    @Path("host/description")
    @GET
    @Produces("text/xml")
    public String getHostDescription(@QueryParam("hostId") String hostId) throws RegistryException {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        return airavataRegistry.getHostDescription(hostId).toXML();
    }


    @POST
    @Path("save/hostDescription")
    @Produces("text/plain")
    public Response saveHostDescription(@FormParam("host") String host) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            HostDescription hostDescription = HostDescription.fromXML(host);
            String result = airavataRegistry.saveHostDescription(hostDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Invalid XML");
            return builder.build();
        }
    }

    @POST
    @Path("save/serviceDescription")
    @Produces("text/plain")
    public Response saveServiceDescription(@FormParam("service") String service) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            String result = airavataRegistry.saveServiceDescription(serviceDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Invalid XML");
            return builder.build();
        }
    }

    @POST
    @Path("save/deploymentDescription")
    @Produces("text/plain")
    public Response saveDeploymentDescription(@FormParam("serviceId") String serviceId,
                                              @FormParam("hostId") String hostId,
                                              @FormParam("app") String app) {

        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            ApplicationDeploymentDescription deploymentDescription =
                    ApplicationDeploymentDescription.fromXML(app);

            String result = airavataRegistry.saveDeploymentDescription(serviceId, hostId, deploymentDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    @POST
    @Path("service/deployOnHost")
    @Produces("text/plain")
    public Response deployServiceOnHost(@FormParam("serviceName") String serviceName,
                                        @FormParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");

        boolean state;
        try {
            state = airavataRegistry.deployServiceOnHost(serviceName, hostName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
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

    public List<HostDescription> searchHostDescription(String name) throws RegistryException {
        return null;
    }

    public List<ServiceDescription> searchServiceDescription(String nameRegEx) throws RegistryException {
        return null;
    }

    public List<ApplicationDeploymentDescription> searchDeploymentDescription(String serviceName, String hostName)
            throws RegistryException {
        return null;
    }

    public Map<HostDescription, List<ApplicationDeploymentDescription>> searchDeploymentDescription(String serviceName)
            throws RegistryException {
        return null;
    }

    public List<ApplicationDeploymentDescription> searchDeploymentDescription(String serviceName, String hostName,
                                                                              String applicationName) throws RegistryException {
        return null;
    }

    public Map<ApplicationDeploymentDescription, String> searchDeploymentDescription() throws RegistryException {
        return null;
    }

    @POST
    @Path("save/gfacDescriptor")
    @Produces("text/plain")
    public Response saveGFacDescriptor(@FormParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            Boolean result = airavataRegistry.saveGFacDescriptor(gfacURL);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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


    @DELETE
    @Path("delete/gfacDescriptor")
    @Produces("text/plain")
    public Response deleteGFacDescriptor(@QueryParam("gfacURL") String gfacURL) throws RegistryException {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            Boolean result = airavataRegistry.deleteGFacDescriptor(gfacURL);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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

    public List<URI> getInterpreterServiceURLList() throws RegistryException {
        return null;
    }


    @POST
    @Path("save/interpreterServiceUrl")
    @Produces("text/plain")
    public Response saveInterpreterServiceURL(@QueryParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.saveInterpreterServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
             Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }


    @DELETE
    @Path("delete/interpreterServiceURL")
    @Produces("text/plain")
    public Response deleteInterpreterServiceURL(URI gfacURL) throws RegistryException {
        return null;
    }

    public List<URI> getMessageBoxServiceURLList() throws RegistryException {
        return null;
    }

    public boolean saveMessageBoxServiceURL(URI gfacURL) throws RegistryException {
        return true;
    }

    public boolean deleteMessageBoxServiceURL(URI gfacURL) throws RegistryException {
        return true;
    }

    public List<URI> getEventingServiceURLList() throws RegistryException {
        return null;
    }

    public boolean saveEventingServiceURL(URI gfacURL) throws RegistryException {
        return true;
    }

    public boolean deleteEventingServiceURL(URI gfacURL) throws RegistryException {
        return true;
    }

    public List<String> getGFacDescriptorList() throws RegistryException {
        return null;
    }

    public boolean saveWorkflow(QName ResourceID, String workflowName, String resourceDesc, String workflowAsaString,
                                String owner, boolean isMakePublic) throws RegistryException {
        return true;
    }

    public Map<QName, Node> getWorkflows(String userName) throws RegistryException {
        return null;
    }

    public Node getWorkflow(QName templateID, String userName) throws RegistryException {
        return null;
    }

    public boolean deleteWorkflow(QName resourceID, String userName) throws RegistryException {
        return true;
    }

    public void deleteServiceDescription(String serviceId) throws RegistryException {

    }

    public void deleteDeploymentDescription(String serviceName, String hostName, String applicationName)
            throws RegistryException {

    }

    public void deleteHostDescription(String hostId) throws RegistryException {

    }

    public boolean saveWorkflowExecutionServiceInput(WorkflowServiceIOData workflowInputData) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowExecutionServiceOutput(WorkflowServiceIOData workflowOutputData) throws RegistryException {
        return true;
    }

    public List<WorkflowServiceIOData> searchWorkflowExecutionServiceInput(String experimentIdRegEx, String workflowNameRegEx, String nodeNameRegEx) throws RegistryException {
        return null;
    }

    public String getWorkflowExecutionTemplateName(String experimentId) throws RegistryException {
        return null;
    }

    public List<WorkflowServiceIOData> searchWorkflowExecutionServiceOutput(String experimentIdRegEx, String workflowNameRegEx, String nodeNameRegEx) throws RegistryException {
        return null;
    }

    public boolean saveWorkflowExecutionName(String experimentId, String workflowIntanceName) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowExecutionStatus(String experimentId, WorkflowInstanceStatus status) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowExecutionStatus(String experimentId, WorkflowInstanceStatus.ExecutionStatus status) throws RegistryException {
        return true;
    }

    public WorkflowInstanceStatus getWorkflowExecutionStatus(String experimentId) throws RegistryException {
        return null;
    }

    public boolean saveWorkflowExecutionOutput(String experimentId, String outputNodeName, String output) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowExecutionOutput(String experimentId, WorkflowIOData data) throws RegistryException {
        return true;
    }

    public WorkflowIOData getWorkflowExecutionOutput(String experimentId, String outputNodeName) throws RegistryException {
        return null;
    }

    public List<WorkflowIOData> getWorkflowExecutionOutput(String experimentId) throws RegistryException {
        return null;
    }

    public String[] getWorkflowExecutionOutputNames(String exeperimentId) throws RegistryException {
        return null;
    }

    public boolean saveWorkflowExecutionUser(String experimentId, String user) throws RegistryException {
        return true;
    }

    public String getWorkflowExecutionUser(String experimentId) throws RegistryException {
        return null;
    }

    public String getWorkflowExecutionName(String experimentId) throws RegistryException {
        return null;
    }

    public WorkflowExecution getWorkflowExecution(String experimentId) throws RegistryException {
        return null;
    }

    public List<String> getWorkflowExecutionIdByUser(String user) throws RegistryException {
        return null;
    }

    public List<WorkflowExecution> getWorkflowExecutionByUser(String user) throws RegistryException {
        return null;
    }

    public List<WorkflowExecution> getWorkflowExecutionByUser(String user, int pageSize, int pageNo) throws RegistryException {
        return null;
    }

    public String getWorkflowExecutionMetadata(String experimentId) throws RegistryException {
        return null;
    }

    public boolean saveWorkflowExecutionMetadata(String experimentId, String metadata) throws RegistryException {
        return true;
    }

//    public boolean saveWorkflowData(WorkflowRunTimeData workflowData)throws RegistryException{
//        return true;
//    }

    public boolean saveWorkflowLastUpdateTime(String experimentId, Timestamp timestamp) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowNodeStatus(String workflowInstanceID, String workflowNodeID, WorkflowInstanceStatus.ExecutionStatus status) throws RegistryException {
        return true;
    }

    public boolean saveWorkflowNodeLastUpdateTime(String workflowInstanceID, String workflowNodeID, Timestamp lastUpdateTime) throws RegistryException {
        return true;
    }

//    public boolean saveWorkflowNodeGramData(WorkflowNodeGramData workflowNodeGramData)throws RegistryException{
//        return true;
//    }

    public boolean saveWorkflowNodeGramLocalJobID(String workflowInstanceID, String workflowNodeID, String localJobID) throws RegistryException {
        return true;
    }


}

package org.apache.airavata.services.registry.rest.resources;

import org.apache.airavata.common.registry.api.exception.RegistryException;
import org.apache.airavata.commons.gfac.type.ApplicationDeploymentDescription;
import org.apache.airavata.commons.gfac.type.HostDescription;
import org.apache.airavata.commons.gfac.type.ServiceDescription;
import org.apache.airavata.registry.api.AiravataRegistry;
import org.apache.airavata.registry.api.Axis2Registry;
import org.apache.airavata.registry.api.DataRegistry;
import org.apache.airavata.registry.api.workflow.WorkflowExecution;
import org.apache.airavata.registry.api.workflow.WorkflowIOData;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceStatus;
import org.apache.airavata.registry.api.workflow.WorkflowServiceIOData;
import org.apache.airavata.services.registry.rest.resourcemappings.HostDescriptionList;
import org.apache.airavata.services.registry.rest.resourcemappings.ServiceURLList;
import org.apache.xmlbeans.XmlException;

import javax.jcr.Node;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 *  RegistryResource for REST interface of Registry API
 *  Three objects will be retrieved from the servelet context as
 *  airavataRegistry, axis2Registry, dataRegistry which are
 *  analogues to main API interfaces of Airavata
 */
@Path("/registry/api")
public class RegistryResource {
    private AiravataRegistry airavataRegistry;
    private Axis2Registry axis2Registry;
    private DataRegistry dataRegistry;

    @Context
    ServletContext context;


    /**
     *
     * @return usename of the JCR registry
     * at the moment, admin username is set
     * TO-DO - get the user from a configuration file
     */
    @Path("/username")
    @GET
    @Produces("text/plain")
    public Response getUserName() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getUsername();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    /**
     *
     * @return repository URL
     */
    @Path("/repositoryurl")
    @GET
    @Produces("text/plain")
    public Response getRepositoryURI() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getRepositoryURI().toString();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }


    }

    /**
     *
     * @return repository name
     */
    @Path("/repositoryname")
    @GET
    @Produces("text/plain")
    public Response getName() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getName();
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
                return builder.build();
            }
        } catch (Exception e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    /**
     * service name should be given as a query parameter. This
     * method will return service wsdl in xml format
     * @param serviceName
     * @return
     */
    @Path("/service/wsdl")
    @GET
    @Produces("text/xml")
    public Response getWSDL(@QueryParam("serviceName") String serviceName) {
        axis2Registry = (Axis2Registry) context.getAttribute("axis2Registry");
        try {
            String result = axis2Registry.getWSDL(serviceName);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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


    /**
     * service should be given as xml.
     * @param service
     * @return
     */
    @Path("/service/description/wsdl")
    @POST
    @Consumes("text/xml")
    public Response getWSDLFromServiceDescription(String service) {
        axis2Registry = (Axis2Registry) context.getAttribute("axis2Registry");
        try {
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            String result = axis2Registry.getWSDL(serviceDescription);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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


    /**
     *
     * @param serviceId
     * @return
     */
    @Path("/service/description")
    @GET
    @Produces("text/xml")
    public Response getServiceDescription(@QueryParam("serviceID") String serviceId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getServiceDescription(serviceId).toXML();
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

    /**
     *
     * @param serviceId
     * @param hostId
     * @return
     */
    @Path("/service/deploymentdescription")
    @GET
    @Produces("text/xml")
    public Response getDeploymentDescription(@QueryParam("serviceID") String serviceId,
                                             @QueryParam("hostId") String hostId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getDeploymentDescription(serviceId, hostId).toXML();
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

    /**
     *
     * @param hostId
     * @return
     */
    @Path("host/description")
    @GET
    @Produces("text/xml")
    public Response getHostDescription(@QueryParam("hostId") String hostId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getHostDescription(hostId).toXML();
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


    /**
     *
     * @param host
     * @return
     */
    @POST
    @Path("save/hostdescription")
    @Produces("text/plain")
    public Response saveHostDescription(@FormParam("host") String host) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            HostDescription hostDescription = HostDescription.fromXML(host);
            String result = airavataRegistry.saveHostDescription(hostDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Invalid XML");
            return builder.build();
        }
    }

    /**
     *
     * @param service
     * @return
     */
    @POST
    @Path("save/servicedescription")
    @Produces("text/plain")
    public Response saveServiceDescription(@FormParam("service") String service) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            ServiceDescription serviceDescription = ServiceDescription.fromXML(service);
            String result = airavataRegistry.saveServiceDescription(serviceDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            builder.entity("Invalid XML");
            return builder.build();
        }
    }

    /**
     *
     * @param serviceId
     * @param hostId
     * @param app
     * @return
     */
    @POST
    @Path("save/deploymentdescription")
    @Produces("text/plain")
    public Response saveDeploymentDescription(@FormParam("serviceId") String serviceId,
                                              @FormParam("hostId") String hostId,
                                              @FormParam("app") String app) {

        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            ApplicationDeploymentDescription deploymentDescription =
                    ApplicationDeploymentDescription.fromXML(app);

            String result = airavataRegistry.saveDeploymentDescription(serviceId, hostId, deploymentDescription);
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
        } catch (XmlException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @param serviceName
     * @param hostName
     * @return
     */
    @POST
    @Path("service/deployOnHost")
    @Produces("text/plain")
    public Response deployServiceOnHost(@FormParam("serviceName") String serviceName,
                                        @FormParam("hostName") String hostName) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");

        boolean state;
        try {
            state = airavataRegistry.deployServiceOnHost(serviceName, hostName);
            if (state) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("True");
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

    /**
     *
     * @param name
     * @return
     */
    @GET
    @Path("search/hostdescriptionlist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response searchHostDescription(@QueryParam("hostname") String name) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            List<HostDescription> hostDescriptionList = airavataRegistry.searchHostDescription(name);
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

    /**
     *
     * @param nameRegEx
     * @return
     */
    public List<ServiceDescription> searchServiceDescription(String nameRegEx) {
        return null;
    }

    /**
     *
     * @param serviceName
     * @param hostName
     * @return
     */
    public List<ApplicationDeploymentDescription> searchDeploymentDescription(String serviceName, String hostName) {
        return null;
    }

    /**
     *
     * @param serviceName
     * @return
     */
    public Map<HostDescription, List<ApplicationDeploymentDescription>> searchDeploymentDescription(String serviceName) {
        return null;
    }

    /**
     *
     * @param serviceName
     * @param hostName
     * @param applicationName
     * @return
     */
    public List<ApplicationDeploymentDescription> searchDeploymentDescription(String serviceName, String hostName,
                                                                              String applicationName) {
        return null;
    }

    /**
     *
     * @return
     */
    public Map<ApplicationDeploymentDescription, String> searchDeploymentDescription() {
        return null;
    }

    /**
     *
     * @param gfacURL
     * @return
     */
    @POST
    @Path("save/gfacdescriptor")
    @Produces("text/plain")
    public Response saveGFacDescriptor(@FormParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            Boolean result = airavataRegistry.saveGFacDescriptor(gfacURL);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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


    /**
     *
     * @param gfacURL
     * @return
     */
    @DELETE
    @Path("delete/gfacdescriptor")
    @Produces("text/plain")
    public Response deleteGFacDescriptor(@QueryParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            Boolean result = airavataRegistry.deleteGFacDescriptor(gfacURL);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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

    /**
     *
     * @return
     */
    @GET
    @Path("interpreter/serviceurilist")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getInterpreterServiceURLList() {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            List<URI> uris = airavataRegistry.getInterpreterServiceURLList();
            ServiceURLList list = new ServiceURLList();
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
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }

    }

    /**
     *
     * @param gfacURL
     * @return
     */
    @POST
    @Path("save/interpreterserviceurl")
    @Produces("text/plain")
    public Response saveInterpreterServiceURL(@FormParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.saveInterpreterServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }


    /**
     *
     * @param gfacURL
     * @return
     */
    @DELETE
    @Path("delete/interpreterserviceurl")
    @Produces("text/plain")
    public Response deleteInterpreterServiceURL(@QueryParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.deleteInterpreterServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @return
     */
    public List<URI> getMessageBoxServiceURLList() {
        return null;
    }

    /**
     *
     * @param gfacURL
     * @return
     */
    @POST
    @Path("save/messageboxserviceurl")
    @Produces("text/plain")
    public Response saveMessageBoxServiceURL(@FormParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.saveMessageBoxServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @param gfacURL
     * @return
     */
    @DELETE
    @Path("delete/messageboxserviceurl")
    @Produces("text/plain")
    public Response deleteMessageBoxServiceURL(@QueryParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.deleteMessageBoxServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @return
     */
    public List<URI> getEventingServiceURLList() {
        return null;
    }


    /**
     *
     * @param gfacURL
     * @return
     */
    @POST
    @Path("save/eventingserviceurl")
    @Produces("text/plain")
    public Response saveEventingServiceURL(@FormParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.saveEventingServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @param gfacURL
     * @return
     */
    @DELETE
    @Path("delete/eventingserviceurl")
    @Produces("text/plain")
    public Response deleteEventingServiceURL(@QueryParam("gfacURL") String gfacURL) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            URI gfacURI = new URI(gfacURL);
            Boolean result = airavataRegistry.deleteEventingServiceURL(gfacURI);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
                return builder.build();
            } else {
                Response.ResponseBuilder builder = Response.status(Response.Status.NOT_MODIFIED);
                return builder.build();
            }
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        } catch (URISyntaxException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
            return builder.build();
        }
    }

    /**
     *
     * @return
     */
    public List<String> getGFacDescriptorList() {
        return null;
    }


    /**
     *
     * @param resourceID
     * @param workflowName
     * @param resourceDesc
     * @param workflowAsaString
     * @param owner
     * @param isMakePublic
     * @return
     */
    @POST
    @Path("save/workflow")
    @Produces("text/plain")
    public Response saveWorkflow(@FormParam("resourceID") String resourceID,
                                 @FormParam("workflowName") String workflowName,
                                 @FormParam("resourceDesc") String resourceDesc,
                                 @FormParam("workflowAsString") String workflowAsaString,
                                 @FormParam("owner") String owner,
                                 @FormParam("isMakePublic") String isMakePublic) {
        boolean isMakePublicState = false;
        QName resourceId = null;
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            resourceId = new QName(resourceID);
            if (isMakePublic == "true") {
                isMakePublicState = true;
            }
            Boolean result = airavataRegistry.saveWorkflow(resourceId, workflowName, resourceDesc, workflowAsaString, owner, isMakePublicState);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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

    /**
     *
     * @param userName
     * @return
     */
    public Map<QName, Node> getWorkflows(String userName) {
        return null;
    }

    /**
     *
     * @param templateID
     * @param userName
     * @return
     */
    public Node getWorkflow(QName templateID, String userName) {
        return null;
    }

    /**
     *
     * @param resourceID
     * @param userName
     * @return
     */
    @DELETE
    @Path("delete/workflow")
    @Produces("text/plain")
    public Response deleteWorkflow(@QueryParam("resourceID") String resourceID,
                                   @QueryParam("userName") String userName) {
        QName resourceId = null;
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            resourceId = new QName(resourceID);
            Boolean result = airavataRegistry.deleteWorkflow(resourceId, userName);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
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

    /**
     *
     * @param serviceId
     * @return
     */
    @DELETE
    @Path("delete/serviceDescription")
    @Produces("text/plain")
    public Response deleteServiceDescription(@QueryParam("serviceID") String serviceId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            airavataRegistry.deleteServiceDescription(serviceId);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();

        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }


    }

    /**
     *
     * @param serviceName
     * @param hostName
     * @param applicationName
     * @return
     */
    @DELETE
    @Path("delete/deploymentDescription")
    @Produces("text/plain")
    public Response deleteDeploymentDescription(@QueryParam("serviceName") String serviceName,
                                                @QueryParam("hostName") String hostName,
                                                @QueryParam("applicationName") String applicationName) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            airavataRegistry.deleteDeploymentDescription(serviceName, hostName, applicationName);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();

        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    /**
     *
     * @param hostId
     * @return
     */
    @DELETE
    @Path("delete/hostDescription")
    @Produces("text/plain")
    public Response deleteHostDescription(@QueryParam("hostID") String hostId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            airavataRegistry.deleteHostDescription(hostId);
            Response.ResponseBuilder builder = Response.status(Response.Status.NO_CONTENT);
            return builder.build();
        } catch (RegistryException e) {
            Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            return builder.build();
        }
    }

    /**
     *
     * @param workflowInputData
     * @return
     */
    public boolean saveWorkflowExecutionServiceInput(WorkflowServiceIOData workflowInputData) {
        return true;
    }

    /**
     *
     * @param workflowOutputData
     * @return
     */
    public boolean saveWorkflowExecutionServiceOutput(WorkflowServiceIOData workflowOutputData) {
        return true;
    }

    /**
     *
     * @param experimentIdRegEx
     * @param workflowNameRegEx
     * @param nodeNameRegEx
     * @return
     */
    public List<WorkflowServiceIOData> searchWorkflowExecutionServiceInput(String experimentIdRegEx, String workflowNameRegEx, String nodeNameRegEx) {
        return null;
    }


    /**
     *
     * @param experimentId
     * @return
     */
    @GET
    @Path("workflow/executionTemplate")
    @Produces("text/plain")
    public Response getWorkflowExecutionTemplateName(@QueryParam("experimentID") String experimentId) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            String result = airavataRegistry.getWorkflowExecutionTemplateName(experimentId);
            if (result != null) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity(result);
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
     *
     * @param experimentIdRegEx
     * @param workflowNameRegEx
     * @param nodeNameRegEx
     * @return
     */
    public List<WorkflowServiceIOData> searchWorkflowExecutionServiceOutput(String experimentIdRegEx, String workflowNameRegEx, String nodeNameRegEx) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @param workflowIntanceName
     * @return
     */
    @POST
    @Path("save/workflowExecutionName")
    @Produces("text/plain")
    public Response saveWorkflowExecutionName(@FormParam("experimentID") String experimentId,
                                              @FormParam("workflowInstanceName") String workflowIntanceName) {
        airavataRegistry = (AiravataRegistry) context.getAttribute("airavataRegistry");
        try {
            Boolean result = airavataRegistry.saveWorkflowExecutionName(experimentId, workflowIntanceName);
            if (result) {
                Response.ResponseBuilder builder = Response.status(Response.Status.OK);
                builder.entity("true");
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

    /**
     *
     * @param experimentId
     * @param status
     * @return
     */
    public boolean saveWorkflowExecutionStatus(String experimentId, WorkflowInstanceStatus status) {
        return true;
    }

    /**
     *
     * @param experimentId
     * @param status
     * @return
     */
    public boolean saveWorkflowExecutionStatus(String experimentId, WorkflowInstanceStatus.ExecutionStatus status) {
        return true;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public WorkflowInstanceStatus getWorkflowExecutionStatus(String experimentId) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @param outputNodeName
     * @param output
     * @return
     */
    public boolean saveWorkflowExecutionOutput(String experimentId, String outputNodeName, String output) {
        return true;
    }

    /**
     *
     * @param experimentId
     * @param data
     * @return
     */
    public boolean saveWorkflowExecutionOutput(String experimentId, WorkflowIOData data) {
        return true;
    }

    /**
     *
     * @param experimentId
     * @param outputNodeName
     * @return
     */
    public WorkflowIOData getWorkflowExecutionOutput(String experimentId, String outputNodeName) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public List<WorkflowIOData> getWorkflowExecutionOutput(String experimentId) {
        return null;
    }

    /**
     *
     * @param exeperimentId
     * @return
     */
    public String[] getWorkflowExecutionOutputNames(String exeperimentId) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @param user
     * @return
     */
    public boolean saveWorkflowExecutionUser(String experimentId, String user) {
        return true;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public String getWorkflowExecutionUser(String experimentId) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public String getWorkflowExecutionName(String experimentId) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public WorkflowExecution getWorkflowExecution(String experimentId) {
        return null;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<String> getWorkflowExecutionIdByUser(String user) {
        return null;
    }

    /**
     *
     * @param user
     * @return
     */
    public List<WorkflowExecution> getWorkflowExecutionByUser(String user) {
        return null;
    }

    /**
     *
     * @param user
     * @param pageSize
     * @param pageNo
     * @return
     */
    public List<WorkflowExecution> getWorkflowExecutionByUser(String user, int pageSize, int pageNo) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @return
     */
    public String getWorkflowExecutionMetadata(String experimentId) {
        return null;
    }

    /**
     *
     * @param experimentId
     * @param metadata
     * @return
     */
    public boolean saveWorkflowExecutionMetadata(String experimentId, String metadata) {
        return true;
    }

//    public boolean saveWorkflowData(WorkflowRunTimeData workflowData) {
//        return true;
//    }

    /**
     *
     * @param experimentId
     * @param timestamp
     * @return
     */
    public boolean saveWorkflowLastUpdateTime(String experimentId, Timestamp timestamp) {
        return true;
    }

    /**
     *
     * @param workflowInstanceID
     * @param workflowNodeID
     * @param status
     * @return
     */
    public boolean saveWorkflowNodeStatus(String workflowInstanceID, String workflowNodeID, WorkflowInstanceStatus.ExecutionStatus status) {
        return true;
    }

    /**
     *
     * @param workflowInstanceID
     * @param workflowNodeID
     * @param lastUpdateTime
     * @return
     */
    public boolean saveWorkflowNodeLastUpdateTime(String workflowInstanceID, String workflowNodeID, Timestamp lastUpdateTime) {
        return true;
    }

//    public boolean saveWorkflowNodeGramData(WorkflowNodeGramData workflowNodeGramData){
//        return true;
//    }

    /**
     *
     * @param workflowInstanceID
     * @param workflowNodeID
     * @param localJobID
     * @return
     */
    public boolean saveWorkflowNodeGramLocalJobID(String workflowInstanceID, String workflowNodeID, String localJobID) {
        return true;
    }


}

