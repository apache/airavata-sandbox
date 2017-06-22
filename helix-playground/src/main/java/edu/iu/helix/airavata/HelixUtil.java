package edu.iu.helix.airavata;

import edu.iu.helix.airavata.tasks.HelixTaskA;
import edu.iu.helix.airavata.tasks.HelixTaskB;
import edu.iu.helix.airavata.tasks.HelixTaskC;
import edu.iu.helix.airavata.tasks.HelixTaskD;
import org.apache.helix.task.*;
import org.jboss.netty.util.internal.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goshenoy on 6/21/17.
 */
public class HelixUtil {

    public static final String TASK_STATE_DEF = "Task";

    public enum DAGType {
        TYPE_A,
        TYPE_B,
        TYPE_C
    }

    private static Workflow.Builder getWorkflowBuilder(DAGType dagType) {
        // create task configs
        List<TaskConfig> taskConfig1 = new ArrayList<TaskConfig>();
        List<TaskConfig> taskConfig2 = new ArrayList<TaskConfig>();
        List<TaskConfig> taskConfig3 = new ArrayList<TaskConfig>();
        List<TaskConfig> taskConfig4 = new ArrayList<TaskConfig>();
        taskConfig1.add(new TaskConfig.Builder().setTaskId("helix_task_a").setCommand(HelixTaskA.TASK_COMMAND).build());
        taskConfig2.add(new TaskConfig.Builder().setTaskId("helix_task_b").setCommand(HelixTaskB.TASK_COMMAND).build());
        taskConfig3.add(new TaskConfig.Builder().setTaskId("helix_task_c").setCommand(HelixTaskC.TASK_COMMAND).build());
        taskConfig4.add(new TaskConfig.Builder().setTaskId("helix_task_d").setCommand(HelixTaskD.TASK_COMMAND).build());

        // create job configs
        JobConfig.Builder jobConfig1 = new JobConfig.Builder().addTaskConfigs(taskConfig1).setMaxAttemptsPerTask(3);
        JobConfig.Builder jobConfig2 = new JobConfig.Builder().addTaskConfigs(taskConfig2).setMaxAttemptsPerTask(3);
        JobConfig.Builder jobConfig3 = new JobConfig.Builder().addTaskConfigs(taskConfig3).setMaxAttemptsPerTask(3);
        JobConfig.Builder jobConfig4 = new JobConfig.Builder().addTaskConfigs(taskConfig4).setMaxAttemptsPerTask(3);

        // create workflow
        Workflow.Builder workflowBuilder = new Workflow.Builder("helix_workflow").setExpiry(0);
        workflowBuilder.addJob("helix_job_a", jobConfig1);
        workflowBuilder.addJob("helix_job_b", jobConfig2);
        workflowBuilder.addJob("helix_job_c", jobConfig3);
        workflowBuilder.addJob("helix_job_d", jobConfig4);


        switch (dagType) {
            case TYPE_A:
                workflowBuilder.addParentChildDependency("helix_job_a", "helix_job_b");
                workflowBuilder.addParentChildDependency("helix_job_b", "helix_job_c");
                workflowBuilder.addParentChildDependency("helix_job_c", "helix_job_d");
                break;

            case TYPE_B:
                workflowBuilder.addParentChildDependency("helix_job_a", "helix_job_c");
                workflowBuilder.addParentChildDependency("helix_job_c", "helix_job_d");
                workflowBuilder.addParentChildDependency("helix_job_d", "helix_job_b");
                break;

            case TYPE_C:
                workflowBuilder.addParentChildDependency("helix_job_a", "helix_job_d");
                workflowBuilder.addParentChildDependency("helix_job_d", "helix_job_b");
                workflowBuilder.addParentChildDependency("helix_job_b", "helix_job_c");
                break;
        }

        return workflowBuilder;
    }

    public static Workflow getWorkflow(DAGType dagType) {
        Workflow.Builder workflowBuilder = getWorkflowBuilder(dagType);
        return workflowBuilder.build();
    }

    private static String generateWorkflowName() {
        return "workflow_" + ThreadLocalRandom.current().nextInt(9999);
    }
}
