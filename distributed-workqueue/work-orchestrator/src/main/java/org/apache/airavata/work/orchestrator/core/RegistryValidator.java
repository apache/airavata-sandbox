package org.apache.airavata.work.orchestrator.core;

import org.apache.airavata.common.exception.AiravataException;
import org.apache.airavata.common.utils.AiravataUtils;
import org.apache.airavata.common.utils.ServerSettings;
import org.apache.airavata.common.utils.ThriftClientPool;
import org.apache.airavata.messaging.core.*;
import org.apache.airavata.messaging.core.impl.RabbitMQPublisher;
import org.apache.airavata.model.appcatalog.appinterface.ApplicationInterfaceDescription;
import org.apache.airavata.model.appcatalog.groupresourceprofile.GroupComputeResourcePreference;
import org.apache.airavata.model.appcatalog.groupresourceprofile.GroupResourceProfile;
import org.apache.airavata.model.experiment.ExperimentCleanupStrategy;
import org.apache.airavata.model.experiment.ExperimentModel;
import org.apache.airavata.model.experiment.ExperimentType;
import org.apache.airavata.model.experiment.UserConfigurationDataModel;
import org.apache.airavata.model.messaging.event.ExperimentSubmitEvent;
import org.apache.airavata.model.messaging.event.MessageType;
import org.apache.airavata.model.scheduling.ComputationalResourceSchedulingModel;
import org.apache.airavata.model.workspace.Gateway;
import org.apache.airavata.registry.api.RegistryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.TException;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RegistryValidator {
    private static ThriftClientPool<RegistryService.Client> registryClientPool;
    private static Publisher experimentPublisher;

    private static <T> GenericObjectPoolConfig<T> createGenericObjectPoolConfig() {

        GenericObjectPoolConfig<T> poolConfig = new GenericObjectPoolConfig<T>();
        poolConfig.setMaxTotal(100);
        poolConfig.setMinIdle(5);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        // must set timeBetweenEvictionRunsMillis since eviction doesn't run unless that is positive
        poolConfig.setTimeBetweenEvictionRunsMillis(5L * 60L * 1000L);
        poolConfig.setNumTestsPerEvictionRun(10);
        poolConfig.setMaxWaitMillis(3000);
        return poolConfig;
    }

    private static RabbitMQProperties getProperties() {
        return (new RabbitMQProperties())
                .setBrokerUrl("amqp://guest:guest@localhost:5672/develop")
                .setDurable(false)
                .setPrefetchCount(200)
                .setAutoRecoveryEnable(true)
                .setConsumerTag("default")
                .setExchangeName("experiment_exchange")
                .setExchangeType(RabbitMQProperties.EXCHANGE_TYPE.TOPIC);
    }

    public static void main(String args[]) throws TException, AiravataException {
        registryClientPool = new ThriftClientPool<>(
                tProtocol -> new RegistryService.Client(tProtocol),
                createGenericObjectPoolConfig(),
                "localhost",8970);

        RabbitMQProperties rProperties = getProperties();
        experimentPublisher = new RabbitMQPublisher(rProperties, (messageContext) -> rProperties.getExchangeName());

        RegistryService.Client resource = registryClientPool.getResource();
        List<Gateway> allGateways = resource.getAllGateways();
        String expName = "VizfoldRepr-" + UUID.randomUUID().toString();
        ExperimentModel experimentModel = new ExperimentModel();

        experimentModel.setExperimentName(expName);
        experimentModel.setProjectId("Default_Project_93d9a30a-4299-44dd-9e48-a7b8961464dc");
        experimentModel.setUserName("default-admin");
        experimentModel.setGatewayId("default");
        experimentModel.setExecutionId("VizfoldRepresentation_c2c025a7-36f7-4c7d-b7b2-970f02de40f9");

        String groupResourceProfileId = "02881ab4-51c6-462d-a4dd-1b3a0d0fa921"; // Validate the access

        ComputationalResourceSchedulingModel computationalResourceSchedulingModel =
                new ComputationalResourceSchedulingModel();

        List<GroupResourceProfile> groupResourceList = resource.getGroupResourceList("default", Collections.singletonList(groupResourceProfileId));
        GroupResourceProfile grp = groupResourceList.get(0);
        Optional<GroupComputeResourcePreference> grpCrp = grp.getComputePreferences().stream().filter(pre -> pre.getComputeResourceId().equals("NCSADelta_e75b0d04-8b4b-417b-8ab4-da76bbd835f5")).findFirst();
        GroupComputeResourcePreference groupCompResourcePref = grpCrp.get();

        computationalResourceSchedulingModel.setQueueName("gpuA100x4");
        computationalResourceSchedulingModel.setNodeCount(1);
        computationalResourceSchedulingModel.setTotalCPUCount(8);
        computationalResourceSchedulingModel.setWallTimeLimit(10);
        computationalResourceSchedulingModel.setTotalPhysicalMemory(0);
        computationalResourceSchedulingModel.setResourceHostId(groupCompResourcePref.getComputeResourceId());
        computationalResourceSchedulingModel.setOverrideScratchLocation(groupCompResourcePref.getScratchLocation());
        computationalResourceSchedulingModel.setOverrideAllocationProjectNumber("bbol-delta-gpu");
        computationalResourceSchedulingModel.setOverrideLoginUserName(groupCompResourcePref.getLoginUserName());

        UserConfigurationDataModel userConfigurationDataModel = new UserConfigurationDataModel();
        userConfigurationDataModel.setComputationalResourceScheduling(computationalResourceSchedulingModel);
        userConfigurationDataModel.setAiravataAutoSchedule(false);
        userConfigurationDataModel.setOverrideManualScheduledParams(false);
        userConfigurationDataModel.setInputStorageResourceId("default_9c15d8af-3d36-4c3c-a07a-0f3b4bb5b903");
        userConfigurationDataModel.setOutputStorageResourceId("vizfold_9c15d8af-3d36-4c3c-a07a-0f3b4bb5b904");
        String experimentDataDir = "/reprs/" + expName;
        userConfigurationDataModel.setExperimentDataDir(experimentDataDir);
        userConfigurationDataModel.setGroupResourceProfileId(groupCompResourcePref.getGroupResourceProfileId());

        experimentModel.setUserConfigurationData(userConfigurationDataModel);

        String appInterfaceId = "VizfoldRepresentation_c2c025a7-36f7-4c7d-b7b2-970f02de40f9";

        ApplicationInterfaceDescription applicationInterface = resource.getApplicationInterface("VizfoldRepresentation_c2c025a7-36f7-4c7d-b7b2-970f02de40f9");

        experimentModel.setExperimentInputs(applicationInterface.getApplicationInputs());
        experimentModel.setExperimentOutputs(applicationInterface.getApplicationOutputs());

        experimentModel.setExperimentType(ExperimentType.SINGLE_APPLICATION);
        experimentModel.setCleanUpStrategy(ExperimentCleanupStrategy.ONLY_COMPLETED);

        String experimentId = resource.createExperiment("default", experimentModel);


        ExperimentSubmitEvent event = new ExperimentSubmitEvent(experimentId, "default");
        MessageContext messageContext = new MessageContext(
                event, MessageType.EXPERIMENT, "LAUNCH.EXP-" + UUID.randomUUID().toString(), "default");
        messageContext.setUpdatedTime(AiravataUtils.getCurrentTimestamp());

        experimentPublisher.publish(messageContext);
    }
}
