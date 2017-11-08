package org.apache.airavata.k8s.task.api;

import org.apache.airavata.k8s.api.resources.task.TaskResource;
import org.apache.airavata.k8s.api.resources.task.TaskStatusResource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskContext implements Serializable {

    private long processId;
    private long taskId;
    private int status;
    private String reason;

    public long getOutPortId() {
        return outPortId;
    }

    public TaskContext setOutPortId(long outPortId) {
        this.outPortId = outPortId;
        return this;
    }

    private long outPortId;
    private Map<String, String> contextVariableParams = new HashMap<>();
    private Map<String, String> contextDataParams = new HashMap<>();
    private transient Map<String, Object> localContext = new HashMap<>();

    private void resetStatus() {
        setStatus(-1);
        setReason("");
        setOutPortId(-1);
        setProcessId(-1);
        setTaskId(-1);
    }

    public void assignTask(TaskResource taskResource) {
        resetStatus();
        setTaskId(taskResource.getId());
        setProcessId(taskResource.getParentProcessId());
        setStatus(TaskStatusResource.State.SCHEDULED);
    }

    public void resetPublicContext() {
        this.contextVariableParams = new HashMap<>();
        this.contextDataParams = new HashMap<>();
    }

    public void resetLocalContext() {
        this.localContext = new HashMap<>();
    }

    public long getTaskId() {
        return taskId;
    }

    public TaskContext setTaskId(long taskId) {
        this.taskId = taskId;
        return this;
    }

    public Map<String, String> getContextVariableParams() {
        return contextVariableParams;
    }

    public TaskContext setContextVariableParams(Map<String, String> contextVariableParams) {
        this.contextVariableParams = contextVariableParams;
        return this;
    }

    public Map<String, String> getContextDataParams() {
        return contextDataParams;
    }

    public TaskContext setContextDataParams(Map<String, String> contextDataParams) {
        this.contextDataParams = contextDataParams;
        return this;
    }

    public Map<String, Object> getLocalContext() {
        return localContext;
    }

    public TaskContext setLocalContext(Map<String, Object> localContext) {
        this.localContext = localContext;
        return this;
    }

    public long getProcessId() {
        return processId;
    }

    public TaskContext setProcessId(long processId) {
        this.processId = processId;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public TaskContext setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public TaskContext setReason(String reason) {
        this.reason = reason;
        return this;
    }

}
