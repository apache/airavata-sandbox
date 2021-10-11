package org.apache.airavata.jupyter.container.magic.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ApiClient {
    public static void main(String args[]) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9999).usePlaintext().build();
        ContainerMagicApiGrpc.ContainerMagicApiBlockingStub containerMagicApiBlockingStub = ContainerMagicApiGrpc.newBlockingStub(channel);
        PythonCellExecutionResponse response = containerMagicApiBlockingStub
                .executePythonCell(PythonCellExecutionRequest.newBuilder().setCellContent("some content").build());

        System.out.println(response);
    }
}
