/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.client;

import org.apache.airavata.allocation.manager.models.AllocationManagerException;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author madrinathapa
 */

public class AllocationServiceClientFactory {
    private final static Logger logger = LoggerFactory.getLogger(AllocationServiceClientFactory.class);

    public static AllocationRegistryService.Client createAllocationRegistryClient(String serverHost, int serverPort) throws AllocationManagerException {
        try {
            TSocket e = new TSocket(serverHost, serverPort);
            e.open();
            TBinaryProtocol protocol = new TBinaryProtocol(e);
            return new AllocationRegistryService.Client(protocol);
        } catch (TTransportException var4) {
            logger.error("failed to create allocate registry client", var4);
            throw new AllocationManagerException();
        }
    }
}
