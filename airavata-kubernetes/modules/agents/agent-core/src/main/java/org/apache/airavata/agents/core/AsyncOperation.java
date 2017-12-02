package org.apache.airavata.agents.core;

import org.apache.airavata.k8s.api.resources.compute.ComputeResource;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public abstract class AsyncOperation {

    private ComputeResource computeResource;

    public AsyncOperation(ComputeResource computeResource) {
        this.computeResource = computeResource;
    }

    public abstract void executeCommandAsync(String command, long callbackWorkflowId);

    public ComputeResource getComputeResource() {
        return computeResource;
    }
}
