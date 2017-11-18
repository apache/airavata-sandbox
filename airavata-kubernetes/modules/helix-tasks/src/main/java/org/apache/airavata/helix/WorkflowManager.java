package org.apache.airavata.helix;

import org.apache.airavata.helix.tasks.command.CommandTask;
import org.apache.airavata.helix.tasks.DataCollectingTask;
import org.apache.airavata.helix.tasks.DataPushingTask;
import org.apache.helix.*;
import org.apache.helix.task.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class WorkflowManager {

    private static final Logger logger = LogManager.getLogger(WorkflowManager.class);


    public static void main(String args[]) {
        Workflow workflow = createWorkflow().build();

        org.apache.helix.HelixManager helixManager = HelixManagerFactory.getZKHelixManager("AiravataDemoCluster", "Admin",
                InstanceType.SPECTATOR, "localhost:2199");

        try {
            helixManager.connect();
            TaskDriver taskDriver = new TaskDriver(helixManager);

            Runtime.getRuntime().addShutdownHook(
                    new Thread() {
                        @Override
                        public void run() {
                            helixManager.disconnect();
                        }
                    }
            );

            taskDriver.start(workflow);
            logger.info("Started workflow");
            TaskState taskState = taskDriver.pollForWorkflowState(workflow.getName(), TaskState.COMPLETED, TaskState.FAILED, TaskState.STOPPED, TaskState.ABORTED);
            System.out.println("Task state " + taskState.name());

        } catch (Exception ex) {
            logger.error("Error in connect() for Admin, reason: " + ex, ex);
        }
    }

    private static Workflow.Builder createWorkflow() {
        List<TaskConfig> downloadDataTasks = new ArrayList<>();
        downloadDataTasks.add(new TaskConfig.Builder().setTaskId("Download_Task").setCommand(DataCollectingTask.NAME).build());

        List<TaskConfig> commandExecuteTasks = new ArrayList<>();
        commandExecuteTasks.add(new TaskConfig.Builder().setTaskId("Command_Task").setCommand(CommandTask.NAME).build());

        List<TaskConfig> pushDataTasks = new ArrayList<>();
        pushDataTasks.add(new TaskConfig.Builder().setTaskId("Push_Task").setCommand(DataPushingTask.NAME).build());

        JobConfig.Builder downloadDataJob = new JobConfig.Builder()
                .addTaskConfigs(downloadDataTasks)
                .setMaxAttemptsPerTask(3).setInstanceGroupTag("p1");

        JobConfig.Builder commandExecuteJob = new JobConfig.Builder()
                .addTaskConfigs(commandExecuteTasks)
                .setMaxAttemptsPerTask(3).setInstanceGroupTag("p2");

        JobConfig.Builder dataPushJob = new JobConfig.Builder()
                .addTaskConfigs(pushDataTasks)
                .setMaxAttemptsPerTask(3).setInstanceGroupTag("p3");

        Workflow.Builder workflow = new Workflow.Builder("Airavata_Workflow3").setExpiry(0);
        workflow.addJob("downloadDataJob", downloadDataJob);
        workflow.addJob("commandExecuteJob", commandExecuteJob);
        workflow.addJob("dataPushJob", dataPushJob);

        workflow.addParentChildDependency("downloadDataJob", "commandExecuteJob");
        workflow.addParentChildDependency("downloadDataJob", "dataPushJob");

        return workflow;
    }
}
