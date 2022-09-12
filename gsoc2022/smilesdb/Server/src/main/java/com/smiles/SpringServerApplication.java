package com.smiles;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SpringServerApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        int localport = 50051;

        SpringApplication.run(SpringServerApplication.class, args);
        System.out.println("TomCat is Running Successfully");
        System.out.println("Starting the gRPC services for SMILES Data Models");
        Server server = ServerBuilder.forPort(localport)
                .addService(new CalcInfoImpl())
                .addService(new MoleculeImpl())
//                .addService(new GreeterImpl())
                .build();
        server.start();

        System.out.println("Started gRPC services at port: localhost: "+ localport);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            ((Server) server).shutdown();
            System.out.println("Successfully stopped the server");
        }));

        server.awaitTermination();
    }
}
