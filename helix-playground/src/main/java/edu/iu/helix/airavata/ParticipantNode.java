package edu.iu.helix.airavata;

import edu.iu.helix.airavata.tasks.HelixTaskA;
import edu.iu.helix.airavata.tasks.HelixTaskB;
import edu.iu.helix.airavata.tasks.HelixTaskC;
import edu.iu.helix.airavata.tasks.HelixTaskD;
import edu.iu.helix.airavata.tasks.ssh.SSHTask;
import org.apache.helix.InstanceType;
import org.apache.helix.examples.OnlineOfflineStateModelFactory;
import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.manager.zk.ZKHelixManager;
import org.apache.helix.manager.zk.ZNRecordSerializer;
import org.apache.helix.manager.zk.ZkClient;
import org.apache.helix.model.BuiltInStateModelDefinitions;
import org.apache.helix.model.InstanceConfig;
import org.apache.helix.participant.StateMachineEngine;
import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskFactory;
import org.apache.helix.task.TaskStateModelFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by goshenoy on 6/21/17.
 */
public class ParticipantNode implements Runnable {

    private static final Logger logger = LogManager.getLogger(ParticipantNode.class);

    private String zkAddress;
    private String clusterName;
    private String participantName;
    private ZKHelixManager zkHelixManager;

    public ParticipantNode(String zkAddress, String clusterName, String participantName) {
        logger.debug("Initializing Participant Node");
        this.zkAddress = zkAddress;
        this.clusterName = clusterName;
        this.participantName = participantName;
    }

    @Override
    public void run() {
        ZkClient zkClient = null;
        try {
            zkClient = new ZkClient(zkAddress, ZkClient.DEFAULT_SESSION_TIMEOUT,
                    ZkClient.DEFAULT_CONNECTION_TIMEOUT, new ZNRecordSerializer());
            ZKHelixAdmin zkHelixAdmin = new ZKHelixAdmin(zkClient);

            List<String> nodesInCluster = zkHelixAdmin.getInstancesInCluster(clusterName);
            if (!nodesInCluster.contains(participantName)) {
                InstanceConfig instanceConfig = new InstanceConfig(participantName);
                instanceConfig.setHostName("localhost");
                instanceConfig.setInstanceEnabled(true);
                zkHelixAdmin.addInstance(clusterName, instanceConfig);
                logger.debug("Instance: " + participantName + ", has been added to cluster: " + clusterName);
            }

            Runtime.getRuntime().addShutdownHook(
                    new Thread() {
                        @Override
                        public void run() {
                            logger.debug("Participant: " + participantName + ", shutdown hook called.");
                            disconnect();
                        }
                    }
            );

            // connect the participant manager
            connect();
        } catch (Exception ex) {
            logger.error("Error in run() for Participant: " + participantName + ", reason: " + ex, ex);
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }

    }

    private void connect() {
        try {
            zkHelixManager = new ZKHelixManager(clusterName, participantName, InstanceType.PARTICIPANT, zkAddress);

            // register online-offline model
            StateMachineEngine machineEngine = zkHelixManager.getStateMachineEngine();
            OnlineOfflineStateModelFactory factory = new OnlineOfflineStateModelFactory(participantName);
            machineEngine.registerStateModelFactory(BuiltInStateModelDefinitions.OnlineOffline.name(), factory);

            // register task model
            Map<String, TaskFactory> taskRegistry = new HashMap<String, TaskFactory>();
            taskRegistry.put(SSHTask.TASK_COMMAND, new TaskFactory() {
                @Override
                public Task createNewTask(TaskCallbackContext context) {
                    return new SSHTask(context);
                }
            });
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
            taskRegistry.put(HelixTaskC.TASK_COMMAND, new TaskFactory() {
                @Override
                public Task createNewTask(TaskCallbackContext context) {
                    return new HelixTaskC(context);
                }
            });
            taskRegistry.put(HelixTaskD.TASK_COMMAND, new TaskFactory() {
                @Override
                public Task createNewTask(TaskCallbackContext context) {
                    return new HelixTaskD(context);
                }
            });

            machineEngine.registerStateModelFactory(HelixUtil.TASK_STATE_DEF,
                    new TaskStateModelFactory(zkHelixManager, taskRegistry));
            logger.debug("Participant: " + participantName + ", registered state model factories.");

            zkHelixManager.connect();
            logger.info("Participant: " + participantName + ", has connected to cluster: " + clusterName);

            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            logger.error("Participant: " + participantName + ", is interrupted! reason: " + ex, ex);
        }
        catch (Exception ex) {
            logger.error("Error in connect() for Participant: " + participantName + ", reason: " + ex, ex);
        } finally {
            disconnect();
        }
    }

    private void disconnect() {
        if (zkHelixManager != null) {
            logger.info("Participant: " + participantName + ", has disconnected from cluster: " + clusterName);
            zkHelixManager.disconnect();
        }
    }
}
