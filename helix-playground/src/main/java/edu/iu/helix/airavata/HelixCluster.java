package edu.iu.helix.airavata;

import edu.iu.helix.airavata.tasks.HelixTaskA;
import edu.iu.helix.airavata.tasks.HelixTaskB;
import edu.iu.helix.airavata.tasks.HelixTaskC;
import edu.iu.helix.airavata.tasks.HelixTaskD;
import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.manager.zk.ZNRecordSerializer;
import org.apache.helix.manager.zk.ZkClient;
import org.apache.helix.model.BuiltInStateModelDefinitions;
import org.apache.helix.model.OnlineOfflineSMD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by goshenoy on 6/21/17.
 */
public class HelixCluster {

    private static final Logger logger = LogManager.getLogger(HelixCluster.class);

    private String zkAddress;
    private String clusterName;
    private int numPartitions;

    private ZkClient zkClient;
    private ZKHelixAdmin zkHelixAdmin;

    public HelixCluster(String zkAddress, String clusterName, int numPartitions) {
        this.zkAddress = zkAddress;
        this.clusterName = clusterName;
        this.numPartitions = numPartitions;

        zkClient = new ZkClient(this.zkAddress, ZkClient.DEFAULT_SESSION_TIMEOUT,
                ZkClient.DEFAULT_CONNECTION_TIMEOUT, new ZNRecordSerializer());
        zkHelixAdmin = new ZKHelixAdmin(zkClient);
    }

    public void setup() {
        zkHelixAdmin.addCluster(clusterName, true);
        zkHelixAdmin.addStateModelDef(clusterName, OnlineOfflineSMD.name, OnlineOfflineSMD.build());
        logger.info("Cluster: " +  clusterName + ", has been added.");
    }

    public void addResourcesToCluster() {
        String stateModel = BuiltInStateModelDefinitions.OnlineOffline.name();
        zkHelixAdmin.addResource(clusterName, HelixTaskA.TASK_COMMAND, numPartitions, stateModel);
        zkHelixAdmin.addResource(clusterName, HelixTaskB.TASK_COMMAND, numPartitions, stateModel);
        zkHelixAdmin.addResource(clusterName, HelixTaskC.TASK_COMMAND, numPartitions, stateModel);
        zkHelixAdmin.addResource(clusterName, HelixTaskD.TASK_COMMAND, numPartitions, stateModel);
        logger.debug("Resources (A,B,C,D) with [ " + numPartitions + " ] partitions have been added to Cluster: " + clusterName);

        zkHelixAdmin.rebalance(clusterName, HelixTaskA.TASK_COMMAND, 1);
        zkHelixAdmin.rebalance(clusterName, HelixTaskB.TASK_COMMAND, 1);
        zkHelixAdmin.rebalance(clusterName, HelixTaskC.TASK_COMMAND, 1);
        zkHelixAdmin.rebalance(clusterName, HelixTaskD.TASK_COMMAND, 1);
        logger.debug("Resources (A,B,C,D) have been rebalanced");
    }

    public void disconnect() {
        if (zkClient != null) {
            zkClient.close();
        }
    }

}
