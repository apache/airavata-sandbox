package org.apache.airavata.helix;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class HelixManager {
    public static void main(String args[]) {
        HelixCluster helixCluster = new HelixCluster("localhost:2199", "AiravataDemoCluster", 1);
        helixCluster.setup();
    }
}
