package edu.iu.helix.airavata;

import org.apache.helix.HelixManager;
import org.apache.helix.HelixManagerFactory;
import org.apache.helix.InstanceType;
import org.apache.helix.task.TaskDriver;
import org.apache.helix.task.TaskState;
import org.apache.helix.task.Workflow;
import org.apache.helix.task.WorkflowContext;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.print.attribute.standard.JobState;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by goshenoy on 6/21/17.
 */
public class HelixClusterManager {

    private static final Logger logger = LogManager.getLogger(HelixClusterManager.class);

    private int numWorkers;
    private int numPartitions;
    private String clusterName;
    private String zkAddress;
    private HelixManager helixManager;
    private TaskDriver taskDriver;

    private ControllerNode controllerNode;
    private  HelixCluster helixCluster;
    private static final String CONTROLLER_NAME = "HelixDemoController";
    private static final String PARTICIPANT_NAME = "HelixDemoParticipant_";

    public HelixClusterManager(String clusterName, String zkAddress, int numWorkers, int numPartitions) {
        this.numWorkers = numWorkers;
        this.numPartitions = numPartitions;
        this.clusterName = clusterName;
        this.zkAddress = zkAddress;
    }

    public void startHelixCluster() {
        // create the cluster
        helixCluster = new HelixCluster(zkAddress, clusterName, numPartitions);
        helixCluster.setup();
        System.out.println("Successfully created helix cluster: " + clusterName + ", with [ " + numPartitions + " ] partitions.");

        // start the participants
        Executor executor = Executors.newFixedThreadPool(numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            String participantName = PARTICIPANT_NAME + i;
            ParticipantNode participant = new ParticipantNode(zkAddress, clusterName, participantName);
            executor.execute(participant);
            System.out.println("Successfully started participant node: " + participantName + ", on cluster: " + clusterName);
        }

        // add resources to cluster
        helixCluster.addResourcesToCluster();
        System.out.println("Successfully added resources to cluster: " + clusterName);

        // start the controller
        controllerNode = new ControllerNode(zkAddress, clusterName, CONTROLLER_NAME);
        controllerNode.start();
        System.out.println("Successfully started the controller node: " + CONTROLLER_NAME + ", on cluster: " + clusterName);

        // start the cluster manager
        connect();
        System.out.println("Successfully started the helix cluster manager (admin), on cluster: " + clusterName);
    }

    private void connect() {

        this.helixManager = HelixManagerFactory.getZKHelixManager(clusterName, "Admin",
                InstanceType.SPECTATOR, zkAddress);
        try {
            this.helixManager.connect();
        } catch (Exception ex) {
            logger.error("Error in connect() for Admin, reason: " + ex, ex);
        }

        this.taskDriver = new TaskDriver(helixManager);
        logger.debug("HelixManager Admin connected.");

        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    @Override
                    public void run() {
                        disconnect();
                        controllerNode.stop();
                        helixCluster.disconnect();
                    }
                }
        );
    }

    private void disconnect() {
        if (helixManager != null) {
            System.out.println("HelixManager Admin disconnecting from cluster: " + clusterName);
            helixManager.disconnect();
        }
    }

    public boolean submitDag(HelixUtil.DAGType dagType) {
        try {
            Workflow workflow = HelixUtil.getWorkflow(dagType);
            taskDriver.start(workflow);
            System.out.println("Started workflow for DagType: " + dagType + ", in cluster: " + clusterName);
            taskDriver.pollForWorkflowState(workflow.getName(), TaskState.COMPLETED, TaskState.FAILED);
//            while (true) {
//                Thread.sleep(100);
//                WorkflowContext context = taskDriver.getWorkflowContext(workflow.getName());
//                System.out.println(context);
//                if (context != null && context.getWorkflowState() != null) {
//                    if (context.getWorkflowState().equals(TaskState.COMPLETED)) {
//                        System.out.println("Workflow completed!");
//                        break;
//                    } else if (context.getWorkflowState().equals(TaskState.FAILED)) {
//                        System.err.println("Workflow failed!");
//                        break;
//                    }
//                }
//            }
            return true;
        } catch (Exception ex) {
            logger.error("Error submitting Dag for type: " + dagType + ", reason: " + ex, ex);
            return false;
        }
    }

    public static void main(String[] args) {
        String clusterName = HelixUtil.CLUSTER_NAME;
        String zkAddress = HelixUtil.ZK_ADDRESS;
        int numWorkers = 3;
        int numPartitions = 1;

        try {
            System.out.println("Starting helix manager for cluster [ " + clusterName + " ], " +
                    "on ZK server [ " + zkAddress + " ], " +
                    "with [ " + numWorkers + " ] workers, " +
                    "having [ " + numPartitions + "] partitions.");
            HelixClusterManager manager = new HelixClusterManager(clusterName, zkAddress, numWorkers, numPartitions);
            manager.startHelixCluster();

            System.out.println("Submitting Workflow for DagType: " + HelixUtil.DAGType.SSH);
            if (manager.submitDag(HelixUtil.DAGType.SSH)) {
                System.out.println("Successfully completed workflow for Dag: " + HelixUtil.DAGType.SSH);
            } else {
                throw new Exception("Failed to run workflow for Dag: " + HelixUtil.DAGType.SSH);
            }
        } catch (Exception ex) {
            logger.error("Something went wrong while running helix cluster manager. Reason: " + ex, ex);
        }

        System.out.println("*** Exiting System ***");
//        System.exit(0);
    }
}
