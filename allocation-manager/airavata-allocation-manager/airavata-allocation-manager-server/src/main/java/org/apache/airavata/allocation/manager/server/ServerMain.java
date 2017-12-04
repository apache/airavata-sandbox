/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author madrinathapa
 */

public class ServerMain {
    private final static Logger logger = LoggerFactory.getLogger(ServerMain.class);

    private static int serverPID = -1;
    private static final String stopFileNamePrefix = "server_stop";
    private static final String serverStartedFileNamePrefix = "server_start";

    public static void main(String[] args) {
        try {
            setServerStarted();
            new AllocationManagerServer().start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings({"resource"})
    private static void setServerStarted() {
        try {
            serverPID = getPID();
            deleteOldStopRequests();
            File serverStartedFile = null;
            serverStartedFile = new File(getServerStartedFileName());
            serverStartedFile.createNewFile();
            serverStartedFile.deleteOnExit();
            new RandomAccessFile(serverStartedFile, "rw").getChannel().lock();
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage(), e);
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    private static String getServerStartedFileName() {
        String ALLOCATION_MANAGER_HOME = System.getenv("" +"ALLOCATION_MANAGER_HOME");
        if(ALLOCATION_MANAGER_HOME==null)
            ALLOCATION_MANAGER_HOME = "/tmp";
        else
            ALLOCATION_MANAGER_HOME = ALLOCATION_MANAGER_HOME + "/bin";
        return new File(ALLOCATION_MANAGER_HOME, serverStartedFileNamePrefix + "_" + Integer.toString(serverPID)).toString();
    }

    private static int getPID() {
        try {
            java.lang.management.RuntimeMXBean runtime = java.lang.management.ManagementFactory
                    .getRuntimeMXBean();
            java.lang.reflect.Field jvm = runtime.getClass()
                    .getDeclaredField("jvm");
            jvm.setAccessible(true);
            sun.management.VMManagement mgmt = (sun.management.VMManagement) jvm
                    .get(runtime);
            java.lang.reflect.Method pid_method = mgmt.getClass()
                    .getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);

            int pid = (Integer) pid_method.invoke(mgmt);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }

    private static void deleteOldStopRequests() {
        File[] files = new File(".").listFiles();
        for (File file : files) {
            if (file.getName().contains(stopFileNamePrefix)) {
                file.delete();
            }
        }
    }
}