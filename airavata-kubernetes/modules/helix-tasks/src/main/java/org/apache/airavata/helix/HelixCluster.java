package org.apache.airavata.helix;

import org.apache.helix.manager.zk.ZKHelixAdmin;
import org.apache.helix.manager.zk.ZNRecordSerializer;
import org.apache.helix.manager.zk.ZkClient;
import org.apache.helix.model.OnlineOfflineSMD;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
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

    public void disconnect() {
        if (zkClient != null) {
            zkClient.close();
        }
    }
}
