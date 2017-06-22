package edu.iu.helix.airavata;

import org.apache.helix.HelixManager;
import org.apache.helix.controller.HelixControllerMain;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;

/**
 * Created by goshenoy on 6/21/17.
 */
public class ControllerNode implements Runnable {

    private static final Logger logger = LogManager.getLogger(ControllerNode.class);

    private String clusterName;
    private String controllerName;
    private String zkAddress;
    private HelixManager zkHelixManager;

    private CountDownLatch startLatch = new CountDownLatch(1);
    private CountDownLatch stopLatch = new CountDownLatch(1);

    public ControllerNode(String zkAddress, String clusterName, String controllerName) {
        this.clusterName = clusterName;
        this.controllerName = controllerName;
        this.zkAddress = zkAddress;
    }

    @Override
    public void run() {
        try {
            zkHelixManager = HelixControllerMain.startHelixController(zkAddress, clusterName,
                    controllerName, HelixControllerMain.STANDALONE);
            startLatch.countDown();
            stopLatch.await();
        } catch (Exception ex) {
            logger.error("Error in run() for Controller: " + controllerName + ", reason: " + ex, ex);
        } finally {
            disconnect();
        }

    }

    public void start() {
        new Thread(this).start();
        try {
            startLatch.await();
            logger.info("Controller: " + controllerName + ", has connected to cluster: " + clusterName);
        } catch (InterruptedException ex) {
            logger.error("Controller: " + controllerName + ", is interrupted! reason: " + ex, ex);
        }

    }

    public void stop() {
        stopLatch.countDown();
    }

    private void disconnect() {
        if (zkHelixManager != null) {
            logger.info("Controller: " + controllerName + ", has disconnected from cluster: " + clusterName);
            zkHelixManager.disconnect();
        }
    }
}
