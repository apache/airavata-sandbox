package org.apache.airavata.allocation.manager.server;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.airavata.allocation.manager.models.AllocationManagerException;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;

public class AllocationManagerAdminServer {
	public static void StartsimpleServer(
			AllocationRegistryService.Processor<AllocationManagerServerHandler> processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(9091);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

			// Use this for a multithreaded server
			// TServer server = new TThreadPoolServer(new
			// TThreadPoolServer.Args(serverTransport).processor(processor));

			System.out.println("Starting the simple server...");
			server.serve();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			StartsimpleServer(new AllocationRegistryService.Processor<AllocationManagerServerHandler>(
					new AllocationManagerServerHandler()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
