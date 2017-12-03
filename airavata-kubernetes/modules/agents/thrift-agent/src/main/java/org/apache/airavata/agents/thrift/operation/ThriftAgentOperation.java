package org.apache.airavata.agents.thrift.operation;

import org.apache.airavata.agents.core.AsyncOperation;
import org.apache.airavata.agents.thrift.stubs.OperationService;
import org.apache.airavata.k8s.api.resources.compute.ComputeResource;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class ThriftAgentOperation extends AsyncOperation {

    private OperationService.Client client;

    public ThriftAgentOperation(ComputeResource computeResource) {
        super(computeResource);
    }

    @Override
    public void executeCommandAsync(String command, long callbackWorkflowId) {
        try {
            System.out.println("Submitting command");
            try {
                TTransport transport = new TSocket(getComputeResource().getHost(), 9090);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                this.client = new OperationService.Client(protocol);
                client.executeCommand(command, callbackWorkflowId);
                System.out.println("Finished submitting");
                transport.close();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
