package org.apache.airavata.jupyter.container.magic.api.handler;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.apache.airavata.jupyter.container.magic.api.ContainerMagicApiGrpc;
import org.apache.airavata.jupyter.container.magic.api.PythonCellExecutionRequest;
import org.apache.airavata.jupyter.container.magic.api.PythonCellExecutionResponse;
import org.apache.commons.text.StringSubstitutor;
import org.lognet.springboot.grpc.GRpcService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@GRpcService
public class ContainerMagicApiHandler extends ContainerMagicApiGrpc.ContainerMagicApiImplBase {

    private String tempDir = "C:\\Users\\dimut\\temp_shuttle";
    private String codeTemplate = "import dill as pickle\n" +
            "\n" +
            "with open('${baseDir}${separator}context.pickle', 'rb') as f:\n" +
            "    context = pickle.load(f)\n" +
            "\n" +
            "exec(\"${code}\", None, context)" +
            "\n" +
            "with open('${baseDir}${separator}final-context.pickle', 'wb') as f:\n" +
            "    pickle.dump(context, f)\n";

    @Override
    public void executePythonCell(PythonCellExecutionRequest request, StreamObserver<PythonCellExecutionResponse> responseObserver) {


        try {
            Path directory = Files.createDirectory(Path.of(tempDir, UUID.randomUUID().toString()));
            System.out.println("Directory " + directory.toAbsolutePath().toString());

            Path scriptPath = Path.of(directory.toAbsolutePath().toString(), "script.py");

            Map<String, String> parameters = new HashMap<>();
            parameters.put("code", request.getCellContent().trim());
            parameters.put("baseDir", directory.toAbsolutePath().toString().replace("\\", "\\\\"));
            parameters.put("separator", File.separator.replace("\\", "\\\\"));


            StringSubstitutor sub = new StringSubstitutor(parameters);
            Files.write(scriptPath, sub.replace(codeTemplate).getBytes());
            Files.write(Path.of(directory.toAbsolutePath().toString(), "context.pickle"), request.getLocalScope().toByteArray());

            Runtime rt = Runtime.getRuntime();
            String[] commands = {"python3", scriptPath.toAbsolutePath().toString()};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(proc.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            StringBuilder stdOut = new StringBuilder();
            StringBuilder stdErr = new StringBuilder();
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                stdOut.append(s).append("\n");
            }

            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                stdErr.append(s).append("\n");
            }

            byte[] finalContext = Files.readAllBytes(Path.of(directory.toAbsolutePath().toString(), "final-context.pickle"));
            PythonCellExecutionResponse executionResponse = PythonCellExecutionResponse.newBuilder()
                    .setStdOut(stdOut.toString())
                    .setStdErr(stdErr.toString())
                    .setLocalScope(ByteString.copyFrom(finalContext)).build();

            responseObserver.onNext(executionResponse);
            responseObserver.onCompleted();


        } catch (IOException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        }


    }
}
