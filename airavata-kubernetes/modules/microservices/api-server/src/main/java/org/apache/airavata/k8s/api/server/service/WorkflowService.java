package org.apache.airavata.k8s.api.server.service;

import org.apache.airavata.k8s.api.resources.process.ProcessResource;
import org.apache.airavata.k8s.api.resources.workflow.WorkflowResource;
import org.apache.airavata.k8s.api.server.ServerRuntimeException;
import org.apache.airavata.k8s.api.server.model.process.ProcessModel;
import org.apache.airavata.k8s.api.server.model.task.TaskDAG;
import org.apache.airavata.k8s.api.server.model.task.TaskModel;
import org.apache.airavata.k8s.api.server.model.task.TaskOutPort;
import org.apache.airavata.k8s.api.server.model.workflow.Workflow;
import org.apache.airavata.k8s.api.server.repository.task.TaskDAGRepository;
import org.apache.airavata.k8s.api.server.repository.task.TaskOutPortRepository;
import org.apache.airavata.k8s.api.server.repository.task.TaskRepository;
import org.apache.airavata.k8s.api.server.repository.workflow.WorkflowRepository;
import org.apache.airavata.k8s.api.server.service.task.TaskService;
import org.apache.airavata.k8s.api.server.service.util.GraphParser;
import org.apache.airavata.k8s.api.server.service.util.ToResourceUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
@Service
public class WorkflowService {

    private ProcessService processService;
    private TaskService taskService;

    private WorkflowRepository workflowRepository;
    private TaskOutPortRepository taskOutPortRepository;
    private TaskRepository taskRepository;
    private TaskDAGRepository taskDAGRepository;

    public WorkflowService(ProcessService processService,
                           TaskService taskService,
                           WorkflowRepository workflowRepository,
                           TaskOutPortRepository taskOutPortRepository,
                           TaskRepository taskRepository,
                           TaskDAGRepository taskDAGRepository) {

        this.processService = processService;
        this.taskService = taskService;
        this.workflowRepository = workflowRepository;
        this.taskOutPortRepository = taskOutPortRepository;
        this.taskRepository = taskRepository;
        this.taskDAGRepository = taskDAGRepository;
    }

    public long createWorkflow(WorkflowResource resource) {
        Workflow workflow = new Workflow();
        workflow.setName(resource.getName());
        workflow.setWorkFlowGraph(resource.getWorkflowGraphXML().getBytes());

        Workflow saved = workflowRepository.save(workflow);
        return saved.getId();
    }

    public long launchWorkflow(long id) {
        Workflow workflow = this.workflowRepository.findById(id)
                .orElseThrow(() -> new ServerRuntimeException("Workflow with id " + id + "can not be found"));


        long processId = processService.create(new ProcessResource()
                .setName("Workflow Process : " + workflow.getName() + "-" + UUID.randomUUID().toString())
                .setCreationTime(System.currentTimeMillis()).setProcessType("WORKFLOW"));

        try {
            GraphParser.ParseResult parseResult = GraphParser.parse(new String(workflow.getWorkFlowGraph()));
            parseResult.getTasks().forEach((mockId, task) -> {
                task.getTaskResource().setParentProcessId(processId);

                for (GraphParser.OutPort outPort : task.getOutPorts().values()) {
                    if (outPort.getNextPort() != null && outPort.getNextPort().getParentOperation() != null) {
                        if ("Stop".equals(outPort.getNextPort().getParentOperation().getName())) {
                            task.getTaskResource().setStoppingTask(true);
                        }
                    }
                }

                for (GraphParser.InPort inPort : task.getInPorts().values()) {
                    for (GraphParser.OutPort outPort : inPort.getPreviousPorts().values()) {
                        if (outPort.getParentOperation() != null && "Start".equals(outPort.getParentOperation().getName())) {
                            task.getTaskResource().setStartingTask(true);
                        }
                    }
                }

                long taskId = taskService.create(task.getTaskResource());
                task.getTaskResource().setId(taskId);
            });

            parseResult.getEdgeCache().forEach(((outPort, inPort) -> {
                if (outPort.getParentOperation() != null && outPort.getNextPort().getParentOperation() != null) {
                    Optional<TaskOutPort> sourceOutPort = taskOutPortRepository
                            .findByReferenceIdAndTaskModel_Id(outPort.getId(), outPort.getParentTask().getId());
                    Optional<TaskModel> targetTask = taskRepository.findById(inPort.getParentTask().getId());

                    taskDAGRepository.save(new TaskDAG()
                            .setSourceOutPort(sourceOutPort.get())
                            .setTargetTask(targetTask.get()));
                }
            }));

        } catch (Exception e) {
            throw new ServerRuntimeException("Failed to create workflow", e);
        }
        return 0;
    }

    public List<WorkflowResource> getAll() {
        List<WorkflowResource> workflowResources = new ArrayList<>();
        Iterable<Workflow> workFlows = this.workflowRepository.findAll();
        Optional.ofNullable(workFlows)
                .ifPresent(wfs -> wfs.forEach(wf -> workflowResources.add(ToResourceUtil.toResource(wf).get())));
        return workflowResources;
    }

    public Optional<Workflow> findEntityById(long id) {
        return this.workflowRepository.findById(id);
    }
}
