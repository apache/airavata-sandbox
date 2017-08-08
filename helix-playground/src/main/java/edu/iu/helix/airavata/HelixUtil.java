package edu.iu.helix.airavata;

import edu.iu.helix.airavata.tasks.HelixTaskA;
import edu.iu.helix.airavata.tasks.HelixTaskB;
import edu.iu.helix.airavata.tasks.HelixTaskC;
import edu.iu.helix.airavata.tasks.HelixTaskD;
import edu.iu.helix.airavata.tasks.ssh.SSHKeyAuthentication;
import edu.iu.helix.airavata.tasks.ssh.SSHServerInfo;
import edu.iu.helix.airavata.tasks.ssh.SSHTask;
import edu.iu.helix.airavata.tasks.ssh.SSHTaskContext;
import org.apache.commons.io.IOUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.helix.task.*;
import org.jboss.netty.util.internal.ThreadLocalRandom;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by goshenoy on 6/21/17.
 */
public class HelixUtil {

    public static final String TASK_STATE_DEF = "Task";
    public static final String ZK_ADDRESS = "localhost:2199";
    public static final String CLUSTER_NAME = "HelixDemoCluster";

    public static final String SSH_WORKFLOW = "SSH_Workflow";
    public static final String CREATE_DIR_TASK = "Task_CreateDir";
    public static final String COPY_PBS_TASK = "Task_CopyPBS";
    public static final String COPY_PY_TASK = "Task_CopyPY";
    public static final String QSUB_TASK = "Task_QSUB";


    public static final String USERNAME = "username";
    public static final String PRIVATE_KEY = "private_key";
    public static final String PUBLIC_KEY  = "public_key";
    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String COMMAND = "command";
    public static final String SRC_PATH = "src_path";
    public static final String DEST_PATH = "dest_path";


    public enum DAGType {
        TYPE_A,
        TYPE_B,
        TYPE_C,
        SSH
    }

    private static Workflow.Builder getWorkflowBuilder(DAGType dagType) throws Exception {
        Workflow.Builder workflow = null;

        if (dagType.equals(DAGType.SSH)) {
            if (!setWorkflowData()) {
                throw new Exception("Failed to create zk data for SSH workflow");
            }
            // create dir task - 1 task for job
            List<TaskConfig> createDirTask = new ArrayList<TaskConfig>();
            createDirTask.add(new TaskConfig.Builder().setTaskId(CREATE_DIR_TASK).setCommand(SSHTask.TASK_COMMAND).build());

            // copy files task - 2 tasks for job
            List<TaskConfig> copyFilesTask = new ArrayList<TaskConfig>();
            copyFilesTask.add(new TaskConfig.Builder().setTaskId(COPY_PBS_TASK).setCommand(SSHTask.TASK_COMMAND).build());
            copyFilesTask.add(new TaskConfig.Builder().setTaskId(COPY_PY_TASK).setCommand(SSHTask.TASK_COMMAND).build());

            // qsub task - 1 task for job
            List<TaskConfig> qsubTask = new ArrayList<TaskConfig>();
            qsubTask.add(new TaskConfig.Builder().setTaskId(QSUB_TASK).setCommand(SSHTask.TASK_COMMAND).build());

            // create-dir job config
            JobConfig.Builder createDirJob = new JobConfig.Builder().addTaskConfigs(createDirTask).setMaxAttemptsPerTask(3);

            // copy-files job config
            JobConfig.Builder copyFilesJob = new JobConfig.Builder().addTaskConfigs(copyFilesTask).setMaxAttemptsPerTask(3);

            // qsub job config
            JobConfig.Builder qsubJob = new JobConfig.Builder().addTaskConfigs(qsubTask).setMaxAttemptsPerTask(1);

            // create workflow
            workflow = new Workflow.Builder(SSH_WORKFLOW).setExpiry(0);
            workflow.addJob("createDirJob", createDirJob);
            workflow.addJob("copyFilesJob", copyFilesJob);
            workflow.addJob("qsubJob", qsubJob);
        } else {
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
            workflow = new Workflow.Builder("helix_workflow").setExpiry(0);
            workflow.addJob("helix_job_a", jobConfig1);
            workflow.addJob("helix_job_b", jobConfig2);
            workflow.addJob("helix_job_c", jobConfig3);
            workflow.addJob("helix_job_d", jobConfig4);
        }

        switch (dagType) {
            case TYPE_A:
                workflow.addParentChildDependency("helix_job_a", "helix_job_b");
                workflow.addParentChildDependency("helix_job_b", "helix_job_c");
                workflow.addParentChildDependency("helix_job_c", "helix_job_d");
                break;

            case TYPE_B:
                workflow.addParentChildDependency("helix_job_a", "helix_job_c");
                workflow.addParentChildDependency("helix_job_c", "helix_job_d");
                workflow.addParentChildDependency("helix_job_d", "helix_job_b");
                break;

            case TYPE_C:
                workflow.addParentChildDependency("helix_job_a", "helix_job_d");
                workflow.addParentChildDependency("helix_job_d", "helix_job_b");
                workflow.addParentChildDependency("helix_job_b", "helix_job_c");
                break;

            case SSH:
                workflow.addParentChildDependency("createDirJob", "copyFilesJob");
                workflow.addParentChildDependency("copyFilesJob", "qsubJob");
                break;
        }

        return workflow;
    }

    public static Workflow getWorkflow(DAGType dagType) throws Exception {
        Workflow.Builder workflowBuilder = getWorkflowBuilder(dagType);
        return workflowBuilder.build();
    }

    private static String generateWorkflowName() {
        return "workflow_" + ThreadLocalRandom.current().nextInt(9999);
    }

    private static boolean setWorkflowData() {
        try {
            CuratorFramework curatorClient = ZkUtils.getCuratorClient();

            SSHKeyAuthentication br2SshAuthentication = new SSHKeyAuthentication(
                    "snakanda",
                    IOUtils.toByteArray(HelixUtil.class.getClassLoader().getResourceAsStream("ssh/id_rsa")),
                    IOUtils.toByteArray(HelixUtil.class.getClassLoader().getResourceAsStream("ssh/id_rsa.pub")),
                    "dummy",
                    HelixUtil.class.getClassLoader().getResource("ssh/known_hosts").getPath(),
                    false
            );

            SSHServerInfo br2 = new SSHServerInfo("snakanda", "bigred2.uits.iu.edu", br2SshAuthentication,22);

            SSHTaskContext createDirTC = new SSHTaskContext(SSHTaskContext.TASK_TYPE.EXECUTE_COMMAND,
                    br2SshAuthentication, null, br2, "mkdir -p airavata");

            SSHTaskContext qsubTC = new SSHTaskContext(SSHTaskContext.TASK_TYPE.EXECUTE_COMMAND,
                    br2SshAuthentication, null, br2, "qsub ~/airavata/job_tf.pbs");


            SSHTaskContext copyPbsTC = new SSHTaskContext(SSHTaskContext.TASK_TYPE.FILE_COPY,
                    br2SshAuthentication, null, br2,
                    HelixUtil.class.getClassLoader().getResource("job_tf.pbs").getPath(), "~/airavata/");

            SSHTaskContext copyPyTC = new SSHTaskContext(SSHTaskContext.TASK_TYPE.FILE_COPY,
                    br2SshAuthentication, null, br2,
                    HelixUtil.class.getClassLoader().getResource("job_tf.pbs").getPath(), "~/airavata/");

            ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, CREATE_DIR_TASK, getBytes(createDirTC));
            ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, COPY_PBS_TASK, getBytes(copyPbsTC));
            ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, COPY_PY_TASK, getBytes(copyPyTC));
            ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, QSUB_TASK, getBytes(qsubTC));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static byte[] getBytes(Object object) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos);
        out.writeObject(object);
        out.flush();
        return bos.toByteArray();
    }

    public static Object getObject(byte[] objectBytes) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
        ObjectInput in = new ObjectInputStream(bis);
        return in.readObject();
    }

//    public static void main(String[] args) throws  Exception {
//
//        CuratorFramework curatorClient = ZkUtils.getCuratorClient();
//
//        // set common data
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, USERNAME, "snakanda");
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, HOSTNAME, "bigred2.uits.iu.edu");
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, PORT, "22");
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, PUBLIC_KEY,
//                IOUtils.toByteArray(HelixUtil.class.getClassLoader().getResourceAsStream("ssh/id_rsa.pub")));
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW, PRIVATE_KEY,
//                IOUtils.toByteArray(HelixUtil.class.getClassLoader().getResourceAsStream("ssh/id_rsa")));
//
//
//        // set data for mkdir
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + CREATE_DIR_TASK, COMMAND, "mkdir -p airavata");
//
//        // set data for copy files
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + COPY_PBS_TASK, SRC_PATH,
//                HelixUtil.class.getClassLoader().getResource("job_tf.pbs").getPath());
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + COPY_PBS_TASK, DEST_PATH, "~/airavata/");
//
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + COPY_PY_TASK, SRC_PATH,
//                HelixUtil.class.getClassLoader().getResource("code_tf.py").getPath());
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + COPY_PY_TASK, DEST_PATH, "~/airavata/");
//
//        // set data for qsub
//        ZkUtils.createZkNode(curatorClient, SSH_WORKFLOW + "/" + CREATE_DIR_TASK, COMMAND, "qsub ~/airavata/job_tf.pbs");
//    }
}
