package org.apache.airavata.k8s.task.api;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class TaskContext {

    private long taskId;
    private Map<String, String> contextVariableParams = new HashMap<>();
    private Map<String, String> contextDataParams = new HashMap<>();
    private Map<String, Object> localContext = new HashMap<>();

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
}
