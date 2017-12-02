package org.apache.airavata.agents.thrift.server;

import org.apache.airavata.agents.thrift.handler.OperationHandler;
import org.apache.airavata.agents.thrift.stubs.OperationService;
import org.apache.airavata.helix.api.PropertyResolver;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.IOException;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class OperationServer {

    private static OperationHandler operationHandler;
    private static OperationService.Processor processor;

    public static void main(String args[]) throws IOException {
        PropertyResolver resolver = new PropertyResolver();
        resolver.loadInputStream(OperationService.class.getClassLoader().getResourceAsStream("application.properties"));
        operationHandler = new OperationHandler(resolver.get("kafka.bootstrap.url"), resolver.get("async.event.listener.topic"));
        processor = new OperationService.Processor(operationHandler);

        Runnable server = new Runnable() {
            @Override
            public void run() {
                simple(processor);
            }
        };

        new Thread(server).start();
    }

    public static void simple(OperationService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the operation server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
