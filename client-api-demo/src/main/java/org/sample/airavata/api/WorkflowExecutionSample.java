package org.sample.airavata.api;

import org.apache.airavata.client.AiravataClientUtils;
import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.AiravataAPIInvocationException;
import org.apache.airavata.client.api.ProvenanceManager;
import org.apache.airavata.registry.api.workflow.WorkflowInstance;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceData;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceNodeData;
import org.apache.airavata.registry.api.workflow.WorkflowInstanceNodePortData;
import org.apache.airavata.schemas.gfac.GlobusHostType;
import org.apache.airavata.workflow.model.wf.Workflow;
import org.apache.airavata.workflow.model.wf.WorkflowInput;
import org.apache.airavata.xbaya.interpretor.NameValue;
import org.apache.airavata.xbaya.util.XBayaUtil;
import org.apache.xmlbeans.SchemaType;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class WorkflowExecutionSample {

    public static void main(String[] args) throws Exception {
    }

    public static URI getWorkflowInterpreterServiceURL(String username, String password, String registryRMIURI) throws AiravataAPIInvocationException {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        return airavataAPI.getAiravataManager().getWorkflowInterpreterServiceURL();
    }

    public static URI getGFaCURL(String username, String password, String registryRMIURI) throws AiravataAPIInvocationException {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        return airavataAPI.getAiravataManager().getGFaCURL();
    }

    public static URI getRegistryURL(String username, String password, String registryRMIURI) throws AiravataAPIInvocationException {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        return airavataAPI.getAiravataManager().getRegistryURL();
    }

    public static URI getMessageBoxServiceURL(String username, String password, String registryRMIURI) throws AiravataAPIInvocationException {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        return airavataAPI.getAiravataManager().getMessageBoxServiceURL();
    }

    public static URI getEventingServiceURL(String username, String password, String registryRMIURI) throws AiravataAPIInvocationException {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        return airavataAPI.getAiravataManager().getEventingServiceURL();
    }

    public static String runWorkflow(String username, String password, String registryRMIURI, String workflowTemplateId) {

        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        List<WorkflowInput> workflowInputs = null;
        try {
            workflowInputs = airavataAPI.getWorkflowManager().getWorkflow(workflowTemplateId).getWorkflowInputs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = 10;
        for (WorkflowInput workflowInput : workflowInputs) {
            if ("int".equals(workflowInput.getType())) {
                workflowInput.setValue(count--);
                System.out.println("Input Name : " + workflowInput.getName());
                System.out.println("Input Value : " + count);
            }
        }


        try {
            System.out.println("Message Box Service URL : " + airavataAPI.getAiravataManager().getMessageBoxServiceURL());
            System.out.println("Eventing Service URL    : " + airavataAPI.getAiravataManager().getEventingServiceURL());
            System.out.println("Registry Service URL    : " + airavataAPI.getAiravataManager().getRegistryURL());
        } catch (AiravataAPIInvocationException e) {
            e.printStackTrace();
        }


        String result = null;
        try {
            // String workflowTemplateId,List<WorkflowInput> inputs, String user, String metadata, String workflowInstanceName
            result = airavataAPI.getExecutionManager().runExperiment(workflowTemplateId, workflowInputs, "admin", null, workflowTemplateId);
        } catch (AiravataAPIInvocationException e) {
            e.printStackTrace();
        }
        System.out.println("Result : " + result);
        return result;
    }


/*    public static List<WorkflowInstance> getWorkflowInstanceList(String username,
                                                                 String password,
                                                                 String registryRMIURI) {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        try {
            List<WorkflowInstance> workflowInstances = airavataAPI.getProvenanceManager().getWorkflowInstances(airavataAPI.getCurrentUser());
            return workflowInstances;
        } catch (AiravataAPIInvocationException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static WorkflowInstance getWorkflowInstanceData(String username,
                                                           String password,
                                                           String registryRMIURI,
                                                           WorkflowInstance workflowInstance) {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        try {
            WorkflowInstanceData workflowInstanceData = airavataAPI.getProvenanceManager().getWorkflowInstanceData(workflowInstance);
            return workflowInstance;
        } catch (AiravataAPIInvocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getUserExperimentsList(String username,
                                                      String password,
                                                      String registryRMIURI) {
        AiravataAPI airavataAPI = SampleUtil.getAiravataAPI();
        try {
            List<String> experiments = airavataAPI.getProvenanceManager().getExperimentIdList(airavataAPI.getCurrentUser());
            //airavataAPI.getProvenanceManager().getWorkflowInstanceData(new WorkflowInstance("MultiplyWorkflow_0c5c975e-0d8c-4ba9-833a-c7ee76c4ab49","MultiplyWorkflow_0c5c975e-0d8c-4ba9-833a-c7ee76c4ab49"));
            return experiments;
        } catch (AiravataAPIInvocationException e) {
            e.printStackTrace();
        }
        return null;
    }


}
