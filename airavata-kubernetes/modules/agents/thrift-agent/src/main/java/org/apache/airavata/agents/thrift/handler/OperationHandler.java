package org.apache.airavata.agents.thrift.handler;

import org.apache.airavata.agents.core.StatusPublisher;
import org.apache.airavata.agents.thrift.stubs.OperationException;
import org.apache.airavata.agents.thrift.stubs.OperationService;
import org.apache.thrift.TException;

import static org.apache.airavata.agents.core.AsyncCommandStatus.*;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class OperationHandler extends StatusPublisher implements OperationService.Iface {

    public OperationHandler(String brokerUrl, String topicName) {
        super(brokerUrl, topicName);
    }

    @Override
    public void executeCommand(String command, long callbackWorkflowId) throws OperationException, TException {
        publishStatus(callbackWorkflowId, PENDING, "Pending for execution");
        publishStatus(callbackWorkflowId, RUNNING, "Starting command execution");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("Executing command " + command);
                publishStatus(callbackWorkflowId, SUCCESS, "Command execution succeeded");
            }
        };

        new Thread(task).start();
    }
}
