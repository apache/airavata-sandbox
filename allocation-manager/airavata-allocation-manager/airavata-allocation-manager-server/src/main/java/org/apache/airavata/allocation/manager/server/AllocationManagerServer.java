/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.server;

import org.apache.airavata.common.utils.IServer;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.allocation.manager.messaging.AllocationServiceDBEventMessagingFactory;
import org.apache.airavata.allocation.manager.models.AllocationManagerException;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import org.apache.airavata.allocation.manager.utils.Constants;
import org.apache.airavata.common.exception.AiravataException;

/**
 *
 * @author madrinathapa
 */

public class AllocationManagerServer implements IServer {
    private final static Logger logger = LoggerFactory.getLogger(AllocationManagerServer.class);

    public static final String ALLOCATION_REG_SERVER_HOST = "allocation.registry.server.host";
    public static final String ALLOCATION_REG_SERVER_PORT = "allocation.registry.server.port";

    private static final String SERVER_NAME = "Allocation Registry Server";
    private static final String SERVER_VERSION = "1.0";

    private IServer.ServerStatus status;
    private TServer server;

    public AllocationManagerServer() {
        setStatus(IServer.ServerStatus.STOPPED);
    }

    @Override
    public String getName() {
        return SERVER_NAME;
    }

    @Override
    public String getVersion() {
        return SERVER_VERSION;
    }

    @Override
    public void start() throws Exception {
        try {
            setStatus(IServer.ServerStatus.STARTING);

            final int serverPort = Integer.parseInt(ServerSettings.getSetting(ALLOCATION_REG_SERVER_PORT));
            final String serverHost = ServerSettings.getSetting(ALLOCATION_REG_SERVER_HOST);
            AllocationRegistryService.Processor processor = new AllocationRegistryService.Processor(new AllocationManagerServerHandler());

            TServerTransport serverTransport;

            if (!ServerSettings.isTLSEnabled()) {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(serverHost, serverPort);
                serverTransport = new TServerSocket(inetSocketAddress);
                TThreadPoolServer.Args options = new TThreadPoolServer.Args(serverTransport);
                options.minWorkerThreads = 30;
                server = new TThreadPoolServer(options.processor(processor));
            }else{
                TSSLTransportFactory.TSSLTransportParameters TLSParams =
                        new TSSLTransportFactory.TSSLTransportParameters();
                TLSParams.requireClientAuth(true);
                TLSParams.setKeyStore(ServerSettings.getKeyStorePath(), ServerSettings.getKeyStorePassword());
                TLSParams.setTrustStore(ServerSettings.getTrustStorePath(), ServerSettings.getTrustStorePassword());
                TServerSocket TLSServerTransport = TSSLTransportFactory.getServerSocket(
                        serverPort, ServerSettings.getTLSClientTimeout(),
                        InetAddress.getByName(serverHost), TLSParams);
                TThreadPoolServer.Args options = new TThreadPoolServer.Args(TLSServerTransport);
                options.minWorkerThreads = 30;
                server = new TThreadPoolServer(options.processor(processor));
            }

            new Thread() {
                public void run() {
                    server.serve();
                    setStatus(IServer.ServerStatus.STOPPED);
                    logger.info("Allocation Registry Server Stopped.");
                }
            }.start();
            new Thread() {
                public void run() {
                    while (!server.isServing()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    if (server.isServing()) {

                        try {
                            logger.info("Register allocation manager service with DB Event publishers");
                            AllocationServiceDBEventMessagingFactory.registerAllocationServiceWithPublishers(Constants.PUBLISHERS);

                            logger.info("Start allocation manager service DB Event subscriber");
                            AllocationServiceDBEventMessagingFactory.getDBEventSubscriber();
                        } catch (AllocationManagerException e) {
                            logger.error("Error starting allocation Manager service. Error setting up DB event services.");
                            server.stop();
                        } catch (AiravataException ex) {
                            java.util.logging.Logger.getLogger(AllocationManagerServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        setStatus(IServer.ServerStatus.STARTED);
                        logger.info("Starting Allocation manager Registry Server on Port " + serverPort);
                        logger.info("Listening to Allocation manager Registry server clients ....");
                    }
                }
            }.start();

        } catch (TTransportException e) {
            setStatus(IServer.ServerStatus.FAILED);
            throw new Exception("Error while starting the Allocation manager Registry service", e);
        }
    }

    @Override
    public void stop() throws Exception {
        if (server!=null && server.isServing()){
            setStatus(IServer.ServerStatus.STOPING);
            server.stop();
        }
    }

    @Override
    public void restart() throws Exception {
        stop();
        start();
    }

    @Override
    public void configure() throws Exception {

    }

    @Override
    public IServer.ServerStatus getStatus() throws Exception {
        return status;
    }

    private void setStatus(IServer.ServerStatus stat){
        status=stat;
        status.updateTime();
    }

    public TServer getServer() {
        return server;
    }

    public void setServer(TServer server) {
        this.server = server;
    }
}
