package org.apache.airavata.k8s.api.server.controller;

import org.apache.airavata.k8s.api.resources.experiment.ExperimentResource;
import org.apache.airavata.k8s.api.resources.workflow.WorkflowResource;
import org.apache.airavata.k8s.api.server.ServerRuntimeException;
import org.apache.airavata.k8s.api.server.service.WorkflowService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@RestController
@RequestMapping(path="/workflow")
public class WorkflowController {

    @Resource
    private WorkflowService workflowService;

    @PostMapping(path = "create/{name}")
    public long createWorkflow(@PathVariable("name") String name, @RequestBody String workflowGraph) {
        return this.workflowService.createWorkflow(new WorkflowResource().setName(name).setWorkflowGraphXML(workflowGraph));
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkflowResource> getAllWorkflows() {
        return this.workflowService.getAll();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkflowResource findExperimentById(@PathVariable("id") long id) {
        return this.workflowService.findById(id)
                .orElseThrow(() -> new ServerRuntimeException("Workflow with id " + id + " not found"));
    }

    @GetMapping(path = "{id}/launch", produces = MediaType.APPLICATION_JSON_VALUE)
    public long launchWorkflow(@PathVariable("id") long id) {
        return this.workflowService.launchWorkflow(id, null);
    }

    @PostMapping(path = "{id}/launch", produces = MediaType.APPLICATION_JSON_VALUE)
    public long launchWorkflow(@PathVariable("id") long id, @RequestBody Map<String, String> boostrapData) {
        return this.workflowService.launchWorkflow(id, boostrapData);
    }
}
