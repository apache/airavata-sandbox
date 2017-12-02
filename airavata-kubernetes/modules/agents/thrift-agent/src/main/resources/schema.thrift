namespace java org.apache.airavata.agents.thrift.stubs

exception OperationException {
  1: string message,
  2: string stacktrace
}

service OperationService
{
        void executeCommand(1:string command, 2:i64 callbackWorkflowId) throws (1:OperationException ex)
}
