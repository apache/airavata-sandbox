# Using Apache Helix To Perform Distributed Task Execution in Apache Airavata

## Introduction

This project is a playground to test Apache Helix's task execution framework. Apache Helix is a generic cluster management framework, which allows one to build highly scalable and fault tolerant distributed systems. It provides a range of functionalities, including:
* Automatic assignment of resources (task executors) and it’s partitions (parallelism of resources) to nodes in the cluster.
* Detecting failure of nodes in the cluster, and taking appropriate actions to recover them back.
* Cluster management – adding nodes and resources to cluster dynamically, load balancing.
* Ability to define an IDEAL STATE for a node – and defining STATE transitions in case the state for a node deviates from the IDEAL one.

Apart from these, Helix also provides out-of-the-box APIs to perform Distributed Task Execution. Some of the concepts Helix uses are ideal to our Airavata task execution use-case. These concepts include:
* Tasks – actual runnable logic executors (eg: job submission, data staging, etc). Tasks return a TaskResult object which contains the state of the task once completed. These include, COMPLETED, FAILED, FATAL_FAILED. Difference between FAILED and FATAL_FAILED, is that FAILED tasks are re-run by Helix (threshold can be set), whereas FATAL_FAILED tasks are not.
* Jobs – A combination of tasks, without dependencies; i.e. if there are > 1 tasks, they are run in parallel across workers.
* Workflow – A combination of jobs arranged in a DAG. In a ONE-TIME workflow, once all jobs are completed, the workflow ends. In a RECURRING workflow, you can schedule workflows to run periodically.
* Job Queues – Another type of workflow, but never ends – keeps accepting new incoming jobs. Ends only when user terminates it.

![helix-task-framework](images/helix-task-framework.png "Helix Task Framework Workflow")

* Helix also allows users to share data (key-value pairs) across Tasks/Jobs/Workflows. The content stored at workflow layer can shared by different jobs belong to this workflow. Similarly content persisted at job layer can shared by different tasks nested in this job.
* Helix provides APIs to POLL either a JOB or WORKFLOW to reach a particular state.

Some core concepts used in Helix which are important to know:
* Participant – Is a node in a Helix cluster (a.k.a. an instance or worker), which host resources (a.k.a. tasks).
* Controller – Is a node in a Helix cluster that monitors and controls the Participant nodes. The controller is responsible for checking if the state of a participant node matches the IDEAL state, and if not, perform STATE TRANSITIONS in order to bring that node back to IDEAL state.
* State Model & State Transitions – Helix allows developers to define what state a participant node needs to be, in order to declare it healthy. Example, in an ONLINE-OFFLINE state model, a node is healthy if it is in ONLINE state; whereas if it goes OFFLINE (for any reason), we can define TRANSITION actions to bring it back ONLINE.
* Cluster – Contains participants and controller nodes. One can define the State model for a cluster.

More details about Apache Helix can be found on their website [http://helix.apache.org](http://helix.apache.org){:target="_blank"}.

## How can Helix be used in Airavata??
Assuming we use Helix just to perform distributed task execution, I have the following in mind:
* Create Helix Tasks (by implementing the Task interface) for each of our job-submission, data-staging, etc. These tasks are called resources.
* Create Participant nodes (a.k.a. workers) to hold these resources. Helix allows us to create resource partitions, such that if we need a Task to run in parallel across workers, we can set the num_partitions > 1 for that resource.
*  Define a StateModel, either an OnlineOffline or MasterSlave, and necessary state transitions. With state transitions we can control the behavior of the participant nodes.
* Create a WORKFLOW to execute a single experiment. This workflow will contain DAG necessary to run that experiment.
* Create a long running QUEUE to keep accepting in-coming experiment requests. Each new experiment request will result in creation of a new JOB to be added to this queue – this job will contain one task – which is to create and run the workflow (mentioned in bullet above).

## Building this Project
This project uses Apache Maven to build the artifacts. Run the following command to make sure the project builds successfully.
```cmd
$ mvn clean install
```

## Running the Prototype
Open the project in an IDE of your choice. Open the ```HelixClusterManager.java``` class. You can control the number of participant nodes (workers) in the cluster by updating the ```numWorkers``` field in the ```main``` method.

The output after running the program should look as follows:
```cmd
Starting helix manager for cluster [ HelixDemoCluster ], on ZK server [ localhost:2199 ], with [ 3 ] workers, having [ 1] partitions.
0    [main] WARN  org.apache.helix.manager.zk.ZKHelixAdmin  - Root directory exists.Cleaning the root directory:/HelixDemoCluster
Successfully created helix cluster: HelixDemoCluster, with [ 1 ] partitions.
Successfully started participant node: HelixDemoParticipant_0, on cluster: HelixDemoCluster
Successfully started participant node: HelixDemoParticipant_1, on cluster: HelixDemoCluster
Successfully started participant node: HelixDemoParticipant_2, on cluster: HelixDemoCluster
548  [pool-1-thread-3] WARN  org.apache.helix.healthcheck.ParticipantHealthReportTask  - ParticipantHealthReportTimerTask already stopped
548  [pool-1-thread-2] WARN  org.apache.helix.healthcheck.ParticipantHealthReportTask  - ParticipantHealthReportTimerTask already stopped
548  [pool-1-thread-1] WARN  org.apache.helix.healthcheck.ParticipantHealthReportTask  - ParticipantHealthReportTimerTask already stopped
631  [pool-1-thread-1] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.messaging.handling.HelixTaskExecutor@fc497fe, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_0/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
631  [pool-1-thread-3] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.messaging.handling.HelixTaskExecutor@7bd93e51, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_2/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
631  [pool-1-thread-2] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.messaging.handling.HelixTaskExecutor@3bcaa92a, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_1/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
Successfully added resources to cluster: HelixDemoCluster
736  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.messaging.handling.HelixTaskExecutor@3046372e, path: /HelixDemoCluster/CONTROLLER/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/CONFIGS/PARTICIPANT, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_2/CURRENTSTATES/15ca80eae2e02bd, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_1/CURRENTSTATES/15ca80eae2e02be, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_0/CURRENTSTATES/15ca80eae2e02bf, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_0/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_1/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/INSTANCES/HelixDemoParticipant_2/MESSAGES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/LIVEINSTANCES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/IDEALSTATES, expected types: [CALLBACK, FINALIZE] but was INIT
737  [Thread-17] WARN  org.apache.helix.manager.zk.CallbackHandler  - Skip processing callbacks for listener: org.apache.helix.controller.GenericHelixController@629ae1fd, path: /HelixDemoCluster/CONTROLLER, expected types: [CALLBACK, FINALIZE] but was INIT
Successfully started the controller node: HelixDemoController, on cluster: HelixDemoCluster
Successfully started the helix cluster manager (admin), on cluster: HelixDemoCluster
Submitting Workflow for DagType: TYPE_A
839  [ZkClient-EventThread-33-localhost:2199] WARN  org.apache.helix.ConfigAccessor  - No config found at /HelixDemoCluster/CONFIGS/RESOURCE/HelixTask_A
844  [ZkClient-EventThread-33-localhost:2199] WARN  org.apache.helix.ConfigAccessor  - No config found at /HelixDemoCluster/CONFIGS/RESOURCE/HelixTask_C
846  [ZkClient-EventThread-33-localhost:2199] WARN  org.apache.helix.ConfigAccessor  - No config found at /HelixDemoCluster/CONFIGS/RESOURCE/HelixTask_D
849  [ZkClient-EventThread-33-localhost:2199] WARN  org.apache.helix.ConfigAccessor  - No config found at /HelixDemoCluster/CONFIGS/RESOURCE/HelixTask_B
OnlineOfflineStateModelFactory.onBecomeOnlineFromOffline():HelixDemoParticipant_0 transitioning from OFFLINE to ONLINE for HelixTask_C HelixTask_C_0
OnlineOfflineStateModelFactory.onBecomeOnlineFromOffline():HelixDemoParticipant_0 transitioning from OFFLINE to ONLINE for HelixTask_A HelixTask_A_0
OnlineOfflineStateModelFactory.onBecomeOnlineFromOffline():HelixDemoParticipant_0 transitioning from OFFLINE to ONLINE for HelixTask_D HelixTask_D_0
OnlineOfflineStateModelFactory.onBecomeOnlineFromOffline():HelixDemoParticipant_0 transitioning from OFFLINE to ONLINE for HelixTask_B HelixTask_B_0
Started workflow for DagType: TYPE_A, in cluster: HelixDemoCluster
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322}{}{}
HelixTaskA | callbackContext: org.apache.helix.task.TaskCallbackContext@2e4d5c35
HelixTaskA | Inside run(), sleeping for 2 secs
HelixTaskA | Inside addToUserStore()
HelixTaskA | Returning status : COMPLETED.
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=IN_PROGRESS}}{}
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED}}{}
HelixTaskB | callbackContext: org.apache.helix.task.TaskCallbackContext@351b6cb3
HelixTaskB | Returning status FAILED, Helix will retry this task. Retry count: 1
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=IN_PROGRESS}}{}
HelixTaskB | callbackContext: org.apache.helix.task.TaskCallbackContext@40decb7c
HelixTaskB | Returning status FAILED, Helix will retry this task. Retry count: 2
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=IN_PROGRESS}}{}
HelixTaskB | callbackContext: org.apache.helix.task.TaskCallbackContext@38a77770
HelixTaskB | After 2 retries, Inside run(), sleeping for 2 secs
HelixTaskB | Inside getFromUserStore()
HelixTaskB | Retrieved from UserStore : Gourav Shenoy
HelixTaskB | Returning status : COMPLETED.
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=IN_PROGRESS}}{}
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=COMPLETED}}{}
HelixTaskC | callbackContext: org.apache.helix.task.TaskCallbackContext@36cf68e3
HelixTaskC | Inside run(), sleeping for 2 secs
HelixTaskC | Inside getFromUserStore()
HelixTaskC | Retrieved from UserStore : Gourav Shenoy
HelixTaskC | Returning status : COMPLETED.
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=COMPLETED, helix_workflow_helix_job_c=IN_PROGRESS}}{}
BLAH WKFLW: WorkflowContext, {START_TIME=1498159501322, STATE=IN_PROGRESS}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=COMPLETED, helix_workflow_helix_job_c=COMPLETED, helix_workflow_helix_job_d=IN_PROGRESS}}{}
HelixTaskD | callbackContext: org.apache.helix.task.TaskCallbackContext@79cba411
HelixTaskD | Inside run(), sleeping for 2 secs
HelixTaskD | Inside getFromUserStore()
HelixTaskD | Retrieved from UserStore : Gourav Shenoy
HelixTaskD | Returning status : COMPLETED.
1890 [Thread-19] WARN  org.apache.helix.task.TaskRebalancer  - Idealstate for resource helix_workflow_helix_job_a does not exist.
BLAH WKFLW: WorkflowContext, {FINISH_TIME=1498159502302, START_TIME=1498159501322, STATE=COMPLETED}{JOB_STATES={helix_workflow_helix_job_a=COMPLETED, helix_workflow_helix_job_b=COMPLETED, helix_workflow_helix_job_c=COMPLETED, helix_workflow_helix_job_d=COMPLETED}}{}
Successfully completed workflow for Dag: TYPE_A
*** Exiting System ***
1900 [Thread-19] WARN  org.apache.helix.task.TaskRebalancer  - Idealstate for resource helix_workflow_helix_job_b does not exist.
1910 [Thread-19] WARN  org.apache.helix.task.TaskRebalancer  - Idealstate for resource helix_workflow_helix_job_c does not exist.
1931 [Thread-19] ERROR org.apache.helix.task.JobRebalancer  - Job configuration is NULL for helix_workflow_helix_job_d
1939 [Thread-19] WARN  org.apache.helix.model.IdealState  - Resource key:helix_workflow_helix_job_d_0 does not have a pre-computed preference list.
HelixManager Admin disconnecting from cluster: HelixDemoCluster
6211 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6212 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6212 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6212 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6214 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6214 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6214 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
6214 [Thread-6] WARN  org.apache.helix.participant.statemachine.StateModel  - Default reset method invoked. Either because the process longer own this resource or session timedout
```
