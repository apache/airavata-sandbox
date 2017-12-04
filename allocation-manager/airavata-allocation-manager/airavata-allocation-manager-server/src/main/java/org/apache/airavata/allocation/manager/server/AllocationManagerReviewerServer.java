package org.apache.airavata.allocation.manager.server;

import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.airavata.allocation.manager.service.cpi.AllocationRegistryService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;

public class AllocationManagerReviewerServer{
	public static void StartsimpleServer(
			AllocationRegistryService.Processor<AllocationManagerServerHandler> processor) {
		try {
			TServerTransport serverTransport = new TServerSocket(9097);
			TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));

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
