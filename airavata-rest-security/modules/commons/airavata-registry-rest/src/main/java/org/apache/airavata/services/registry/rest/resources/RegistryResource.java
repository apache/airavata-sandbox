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
    //    return "Amila Jayasekara";
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
    /*@Path("/service/description/wsdl")
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
    } */



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

