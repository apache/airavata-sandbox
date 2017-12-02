package org.apache.airavata.helix.api;

import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.helix.InstanceType;
import org.apache.helix.examples.OnlineOfflineStateModelFactory;
import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.manager.zk.ZKHelixManager;
import org.apache.helix.manager.zk.ZNRecordSerializer;
import org.apache.helix.manager.zk.ZkClient;
import org.apache.helix.model.BuiltInStateModelDefinitions;
import org.apache.helix.model.InstanceConfig;
import org.apache.helix.participant.StateMachineEngine;
import org.apache.helix.task.TaskFactory;
import org.apache.helix.task.TaskStateModelFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public abstract class HelixParticipant implements Runnable {

    private static final Logger logger = LogManager.getLogger(HelixParticipant.class);

    private String zkAddress;
    private String clusterName;
    private String participantName;
    private ZKHelixManager zkHelixManager;
    private String taskTypeName;
    private String apiServerUrl;
    private RestTemplate restTemplate;
    private PropertyResolver propertyResolver;

    public HelixParticipant(String propertyFile) throws IOException {

        logger.debug("Initializing Participant Node");

        this.propertyResolver = new PropertyResolver();
        propertyResolver.loadInputStream(this.getClass().getClassLoader().getResourceAsStream(propertyFile));

        this.zkAddress = propertyResolver.get("zookeeper.connection.url");
        this.clusterName = propertyResolver.get("helix.cluster.name");
        this.participantName = propertyResolver.get("participant.name");
        this.taskTypeName = propertyResolver.get("task.type.name");
        this.apiServerUrl = propertyResolver.get("api.server.url");

        this.restTemplate = new RestTemplate();
    }

    public abstract Map<String, TaskFactory> getTaskFactory();

    public abstract TaskTypeResource getTaskType();

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
                instanceConfig.addTag(taskTypeName);
                zkHelixAdmin.addInstance(clusterName, instanceConfig);
                logger.debug("Instance: " + participantName + ", has been added to cluster: " + clusterName);
            } else {
                zkHelixAdmin.addInstanceTag(clusterName, participantName, taskTypeName);
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
            register();
            connect();
        } catch (Exception ex) {
            logger.error("Error in run() for Participant: " + participantName + ", reason: " + ex, ex);
        } finally {
            if (zkClient != null) {
                zkClient.close();
            }
        }
    }

    private void register() {
        this.restTemplate.postForObject("http://" + apiServerUrl + "/taskType", getTaskType(), Long.class);
    }

    private void connect() {
        try {
            zkHelixManager = new ZKHelixManager(clusterName, participantName, InstanceType.PARTICIPANT, zkAddress);
            // register online-offline model
            StateMachineEngine machineEngine = zkHelixManager.getStateMachineEngine();
            OnlineOfflineStateModelFactory factory = new OnlineOfflineStateModelFactory(participantName);
            machineEngine.registerStateModelFactory(BuiltInStateModelDefinitions.OnlineOffline.name(), factory);

            // register task model
            machineEngine.registerStateModelFactory("Task", new TaskStateModelFactory(zkHelixManager, getTaskFactory()));
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

    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }
}
