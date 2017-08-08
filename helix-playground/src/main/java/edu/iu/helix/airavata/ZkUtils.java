package edu.iu.helix.airavata;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by goshenoy on 6/28/17.
 */
public class ZkUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZkUtils.class);
    private static CuratorFramework curatorClient;

    /**
     *  Get curatorFramework instance
     * @return
     * @throws Exception
     */
    public static CuratorFramework getCuratorClient() throws Exception {
        if (curatorClient == null) {
            synchronized (ZkUtils.class) {
                if (curatorClient == null) {
                    String connectionSting = HelixUtil.ZK_ADDRESS;
                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
                    curatorClient = CuratorFrameworkFactory.newClient(connectionSting, retryPolicy);
                    curatorClient.start();
                }
            }
        }

        return curatorClient;
    }

    public static String getZkPath(String parentNode, String childNode) throws Exception {
        return ZKPaths.makePath(parentNode, childNode);
    }

    public static void createZkNode(CuratorFramework curatorClient, String parentNode, String childNode, String data) throws Exception {
        String zkPath = getZkPath(parentNode, childNode);
        logger.debug("Creating Zk node for: " + zkPath);
        ZKPaths.mkdirs(curatorClient.getZookeeperClient().getZooKeeper(), zkPath);

        setZkData(curatorClient, zkPath, data.getBytes());
    }

    public static void createZkNode(CuratorFramework curatorClient, String parentNode, String childNode, byte[] data) throws Exception {
        String zkPath = getZkPath(parentNode, childNode);
        logger.debug("Creating Zk node for: " + zkPath);
        ZKPaths.mkdirs(curatorClient.getZookeeperClient().getZooKeeper(), zkPath);

        setZkData(curatorClient, zkPath, data);
    }

    public static void setZkData(CuratorFramework curatorClient, String zkPath, byte[] data) throws Exception {
        curatorClient.setData().withVersion(-1).forPath(zkPath, data);
    }

    public static Object getZkData(CuratorFramework curatorClient, String parentNode, String childNode) throws Exception {
        String zkPath = getZkPath(parentNode, childNode);
        return getZkData(curatorClient, zkPath);
    }

    public static Object getZkData(CuratorFramework curatorClient, String zkPath) throws Exception {
        byte[] data = curatorClient.getData().forPath(zkPath);
        return HelixUtil.getObject(data);
    }
 }
