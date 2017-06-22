package edu.iu.helix.play;

import org.apache.helix.HelixManager;
import org.apache.helix.HelixManagerFactory;
import org.apache.helix.InstanceType;
import org.apache.helix.examples.OnlineOfflineStateModelFactory;
import org.apache.helix.manager.zk.ZKHelixManager;
import org.apache.helix.model.BuiltInStateModelDefinitions;
import org.apache.helix.participant.StateMachineEngine;
import org.apache.helix.task.*;
import org.apache.helix.tools.ClusterSetup;
import org.apache.helix.tools.ClusterStateVerifier;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by goshenoy on 6/18/17.
 */
public class HelixDAGFramework {

    private static Logger logger = LogManager.getLogger(HelixDAGFramework.class);

    private static final String ZK_ADDRESS = "localhost:2199";
    private static final String PARTICIPANT_ADDRESS = "localhost:12918";

    private static final String CLUSTER_NAME = "HelixDagCluster";
    private static final String RESOURCE_NAME = "HelixResource";
    private static final String CONTROLLER_NAME = "HelixController";
    private static final String ADMIN_NAME = "HelixAdmin";

    private static final String WORKFLOW_NAME = "helix_workflow";
    private static final String JOB_NAME = "helix_job";
    private static final String TASK_A_NAME = "helix_task_id_a";
    private static final String TASK_B_NAME = "helix_task_id_b";

    private static final String ONLINE_OFFLINE = "OnlineOffline";

    private static HelixManager adminManager;
    private static ZKHelixManager participantManager;
    private static ZKHelixManager controllerManager;

    private static ClusterSetup _setupTool;


    public static void main(String[] args) {
        try {
            // create cluster
            createCluster();

            // add instance, resource to cluster
            addInstanceToCluster();
            addResourceToCluster();

            // create participant, controller & admin manager
            createParticipantManager();
            createControllerManager();
            createAdminManager();

            // verifying cluster state
            verifyClusterState();

            // create task-driver
            logger.info("edu.iu.helix.play.HelixDAGFramework | Creating TaskDriver.");
            TaskDriver taskDriver = new TaskDriver(adminManager);

            // create task-config list
            logger.info("edu.iu.helix.play.HelixDAGFramework | Creating TaskConfig list.");
            List<TaskConfig> taskConfigList1 = new ArrayList<TaskConfig>();
            List<TaskConfig> taskConfigList2 = new ArrayList<TaskConfig>();
            taskConfigList1.add(
                    new TaskConfig.Builder().setTaskId(TASK_A_NAME).setCommand(HelixTaskA.TASK_COMMAND).build()
            );
            taskConfigList2.add(
                    new TaskConfig.Builder().setTaskId(TASK_B_NAME).setCommand(HelixTaskB.TASK_COMMAND).build()
            );

            // create job-config-builder
            logger.info("edu.iu.helix.play.HelixDAGFramework | Creating JobConfig.Builder.");
            JobConfig.Builder jobConfigBuilder1 = new JobConfig.Builder().addTaskConfigs(taskConfigList1);
            JobConfig.Builder jobConfigBuilder2 = new JobConfig.Builder().addTaskConfigs(taskConfigList2);

            // create workflow-builder & add job
            logger.info("edu.iu.helix.play.HelixDAGFramework | Creating Workflow.Builder.");
            Workflow.Builder workflowBuilder = new Workflow.Builder(WORKFLOW_NAME).setExpiry(0);

            logger.info("edu.iu.helix.play.HelixDAGFramework | Adding Jobs (a,b) to Workflow.Builder.");
            workflowBuilder.addJob(JOB_NAME + "_a", jobConfigBuilder1);
            workflowBuilder.addJob(JOB_NAME + "_b", jobConfigBuilder2);

            logger.info("edu.iu.helix.play.HelixDAGFramework | Setting Job A parent of Job B.");
            workflowBuilder.addParentChildDependency(JOB_NAME + "_a", JOB_NAME + "_b");

            // start the workflow
            logger.info("edu.iu.helix.play.HelixDAGFramework | Starting the Workflow.");
            taskDriver.start(workflowBuilder.build());

            // waiting for job to complete
            logger.info("edu.iu.helix.play.HelixDAGFramework | Waiting for Workflow to COMPLETE.");
//            taskDriver.pollForJobState(WORKFLOW_NAME, WORKFLOW_NAME + "_" + JOB_NAME, TaskState.COMPLETED);
            taskDriver.pollForWorkflowState(WORKFLOW_NAME, TaskState.COMPLETED);

            // job completed, exit
            logger.info("edu.iu.helix.play.HelixDAGFramework | Job Completed, Exiting.");
        } catch (Exception ex) {
            logger.error("Exception caught | ex: " + ex.getMessage(), ex);
        } finally {
            // disconnect all managers
            logger.info("edu.iu.helix.play.HelixDAGFramework | Disconnecting All Managers (Participant, Controller, Admin).");
            disconnectManagers();

            logger.info("edu.iu.helix.play.HelixDAGFramework | Bye!");
        }
    }

    private static void createCluster() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Creating a cluster.");
        _setupTool = new ClusterSetup(ZK_ADDRESS);
        _setupTool.addCluster(CLUSTER_NAME, true);
    }

    private static void addInstanceToCluster() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Adding instanace to cluster.");
        _setupTool.addInstanceToCluster(CLUSTER_NAME, PARTICIPANT_ADDRESS);
    }

    private static void addResourceToCluster() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Adding resource to cluster.");
        _setupTool.addResourceToCluster(CLUSTER_NAME, RESOURCE_NAME, 1, ONLINE_OFFLINE);
        _setupTool.rebalanceStorageCluster(CLUSTER_NAME, RESOURCE_NAME, 1);
    }

    private static void createParticipantManager() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Creating a Participant Manager.");
        String instanceName = PARTICIPANT_ADDRESS.replaceAll(":", "_");
        participantManager = new ZKHelixManager(CLUSTER_NAME, instanceName, InstanceType.PARTICIPANT, ZK_ADDRESS);

        logger.info("edu.iu.helix.play.HelixDAGFramework | Registering SMF for Participant.");
        StateMachineEngine sme = participantManager.getStateMachineEngine();
        sme.registerStateModelFactory(BuiltInStateModelDefinitions.OnlineOffline.name(), new OnlineOfflineStateModelFactory());

        try {
            logger.info("edu.iu.helix.play.HelixDAGFramework | Registering Task for Participant.");
            registerTaskAndStateModel();

            logger.info("edu.iu.helix.play.HelixDAGFramework | Starting Participant Manager.");
            participantManager.connect();
        } catch (Exception ex) {
            logger.error("Error creating Participant Manager, ex: " + ex, ex);
        }
    }

    private static void createAdminManager() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Creating a Admin Manager.");
        adminManager = HelixManagerFactory.getZKHelixManager(CLUSTER_NAME, ADMIN_NAME, InstanceType.ADMINISTRATOR, ZK_ADDRESS);
        try {
            logger.info("edu.iu.helix.play.HelixDAGFramework | Starting Admin Manager.");
            adminManager.connect();
        } catch (Exception ex) {
            logger.error("Error creating Admin Manager, ex: " + ex, ex);
        }
    }

    private static void createControllerManager() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Creating a Controller Manager.");
        controllerManager = new ZKHelixManager(CLUSTER_NAME, CONTROLLER_NAME, InstanceType.CONTROLLER, ZK_ADDRESS);
        try {
            logger.info("edu.iu.helix.play.HelixDAGFramework | Starting Controller Manager.");
            controllerManager.connect();
        } catch (Exception ex) {
            logger.error("Error creating Controller Manager, ex: " + ex, ex);
        }
    }

    private static void registerTaskAndStateModel() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Registering Task.");
        Map<String, TaskFactory> taskRegistry = new HashMap<String, TaskFactory>();
        taskRegistry.put(HelixTaskA.TASK_COMMAND, new TaskFactory() {
            @Override
            public Task createNewTask(TaskCallbackContext context) {
                return new HelixTaskA(context);
            }
        });

        taskRegistry.put(HelixTaskB.TASK_COMMAND, new TaskFactory() {
            @Override
            public Task createNewTask(TaskCallbackContext context) {
                return new HelixTaskB(context);
            }
        });

        logger.info("edu.iu.helix.play.HelixDAGFramework | Registering Task StateModel Factory.");
        StateMachineEngine sme = participantManager.getStateMachineEngine();
        sme.registerStateModelFactory("Task", new TaskStateModelFactory(participantManager, taskRegistry));
    }

    private static void verifyClusterState() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Verifying Cluster State.");
        boolean result = ClusterStateVerifier.verifyByZkCallback(
                new ClusterStateVerifier.BestPossAndExtViewZkVerifier(ZK_ADDRESS, CLUSTER_NAME));
        Assert.assertTrue(result);
    }

    private static void disconnectManagers() {
        logger.info("edu.iu.helix.play.HelixDAGFramework | Disconnecting Admin Manager.");
        adminManager.disconnect();

        logger.info("edu.iu.helix.play.HelixDAGFramework | Disconnecting Participant Manager.");
        participantManager.disconnect();

        logger.info("edu.iu.helix.play.HelixDAGFramework | Disconnecting Contoller Manager.");
        controllerManager.disconnect();

        System.exit(0);
    }
 }
