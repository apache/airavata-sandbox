package com.smiles;

import com.smiles.calcinfo.CalcInfoImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpringApplication.run(ServerApplication.class, args);

        Server server = ServerBuilder.forPort(7594)
                .addService(new CalcInfoImpl()).build();
        server.start();
        System.out.println("\n Server running successfully");
        server.awaitTermination();
    }
}
