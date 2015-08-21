/*
*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements.  See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership.  The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License.  You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*
*/
package org.apache.airavata.datacat.agent.monitor.impl;

import org.apache.airavata.datacat.agent.dispatcher.DispatcherService;
import org.apache.airavata.datacat.agent.monitor.IMonitor;
import org.apache.airavata.datacat.agent.util.AgentProperties;
import org.apache.airavata.datacat.agent.util.Constants;
import org.apache.airavata.datacat.models.OutputMonitorMessage;
import org.apache.airavata.datacat.models.OutputMonitorMessageType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * This monitor is specifically targeting the GridChem use case
 */

public class LocalFileSystemMonitor implements IMonitor {

    private final Logger logger = LogManager.getLogger(LocalFileSystemMonitor.class);
    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private boolean runMonitor = false;
    private boolean trace = false;
    private long waitTime;

    private ArrayList<OutputInfo> snapshot;
    private String serialisedFileName = "snapshot.ser";

    private DispatcherService dispatcherService;

    public LocalFileSystemMonitor() throws Exception {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        waitTime = Integer.parseInt(
                AgentProperties.getInstance().getProperty(Constants.BATCH_MONITOR_WAIT_TIME, "1000"));
        dispatcherService = DispatcherService.getInstance();

        runMonitor = false;
        if ((new File(serialisedFileName)).exists()) {
                snapshot = (ArrayList<OutputInfo>) (new ObjectInputStream(new FileInputStream(serialisedFileName))).readObject();
        } else {

            logger.info("Starting Batch Monitor Scan...");

            ArrayList<OutputInfo> currentFiles = getCurrentFilesList(
                    getCurrentFilesMap(AgentProperties.getInstance().getProperty(Constants.DATA_ROOT, "")));

            ArrayList<OutputMonitorMessage> outputMonitorMessages = new ArrayList<OutputMonitorMessage>();
            for (int i = 0; i < currentFiles.size(); i++) {
                OutputMonitorMessage fileWatcherMessage = new OutputMonitorMessage();
                //fileWatcherMessage.setOutputPath(currentFiles.get(i).getOutputName());
                fileWatcherMessage.setOutputPath(currentFiles.get(i).getOutputPath());
                fileWatcherMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_CREATED);

                outputMonitorMessages.add(fileWatcherMessage);
            }

            logger.info("Ending Batch Monitor Scan...");

            for (int i = 0; i < outputMonitorMessages.size(); i++) {
                logger.info("Adding file monitor message for "+ outputMonitorMessages.get(i).getOutputPath());
                dispatcherService.addFileMonitorMessage(outputMonitorMessages.get(i));
            }

            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(serialisedFileName));
            outputStream.writeObject(currentFiles);
            this.snapshot = currentFiles;
        }
    }

    /**
     * Start directory monitoring
     */
    @Override
    public void startMonitor(Path path) throws IOException {
        init(path);
        runMonitor = true;
        processEvents();
        logger.info("Started directory watching");
    }

    /**
     * Stop directory monitoring
     */
    @Override
    public void stopMonitor() {
        runMonitor = false;
        logger.info("Stopped directory watching");
    }

    /**
     * Initializing the FileSystemMonitor
     *
     * @param path
     * @throws java.io.IOException
     */
    private void init(Path path) throws IOException {
        this.trace = false;

        logger.info("Scanning " + path);
        registerAll(path);
        logger.info("Done.");

        // enable trace after initial registration
        this.trace = true;

    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                logger.info("register: " + dir);
            } else {
                if (!dir.equals(prev)) {
                    logger.info("update: " + prev + " -> " + dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Processing directory update events
     */
    @SuppressWarnings("unchecked")
    private void processEvents() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (runMonitor) {
                    try {
                        logger.info("Starting Batch Monitor Scan...");
                        Thread.sleep(waitTime);
                        HashMap<String, OutputInfo> currentFilesMap = getCurrentFilesMap(
                                AgentProperties.getInstance().getProperty(Constants.DATA_ROOT, ""));
                        ArrayList<OutputInfo> currentFilesList = getCurrentFilesList(currentFilesMap);
                        ArrayList<OutputMonitorMessage> outputMonitorMessages = new ArrayList<OutputMonitorMessage>();
                        for (int i = 0; i < snapshot.size(); i++) {
                            OutputInfo fSnap = snapshot.get(i);
                            OutputInfo fCurrent = currentFilesMap.get(fSnap.getOutputPath());
                            if (fCurrent == null) {
                                OutputMonitorMessage fileWatcherMessage = new OutputMonitorMessage();
                                fileWatcherMessage.setOutputPath(fSnap.getOutputName());
                                fileWatcherMessage.setOutputPath(fSnap.getOutputPath());
                                fileWatcherMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_DELETED);
                                outputMonitorMessages.add(fileWatcherMessage);
                            } else {
                                if (fSnap.getLastModifiedTime() != fCurrent.getLastModifiedTime()) {
                                    OutputMonitorMessage fileWatcherMessage = new OutputMonitorMessage();
                                    fileWatcherMessage.setOutputPath(fSnap.getOutputName());
                                    fileWatcherMessage.setOutputPath(fSnap.getOutputPath());
                                    fileWatcherMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_MODIFIED);
                                    outputMonitorMessages.add(fileWatcherMessage);
                                }
                                currentFilesMap.remove(fSnap.getOutputPath());
                            }
                        }
                        ArrayList<OutputInfo> newlyCreatedFiles = new ArrayList<OutputInfo>(currentFilesMap.values());
                        for (int i = 0; i < newlyCreatedFiles.size(); i++) {
                            OutputInfo fNew = newlyCreatedFiles.get(i);
                            OutputMonitorMessage outputMonitorMessage = new OutputMonitorMessage();
                            outputMonitorMessage.setOutputPath(fNew.getOutputName());
                            outputMonitorMessage.setOutputPath(fNew.getOutputPath());
                            outputMonitorMessage.setFileMonitorMessageType(OutputMonitorMessageType.FILE_MODIFIED);
                            outputMonitorMessages.add(outputMonitorMessage);
                        }
                        logger.info("Ending Batch Monitor Scan...");

                        for (int i = 0; i < outputMonitorMessages.size(); i++) {
                            logger.info("Adding file monitor message for "+ outputMonitorMessages.get(i).getOutputPath());
                            dispatcherService.addFileMonitorMessage(outputMonitorMessages.get(i));
                        }


                        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(serialisedFileName));
                        outputStream.writeObject(currentFilesList);

                        logger.info("Successfully serialized new directory snapshot...");
                        snapshot = currentFilesList;

                    } catch (InterruptedException e) {
                        logger.error(e.toString());
                    } catch (IOException e){
                        logger.error(e.toString());
                    }
                }
            }
        })).start();
    }

    private HashMap<String, OutputInfo> getCurrentFilesMap(String path) {
        HashMap<String, OutputInfo> currentFiles = new HashMap<String, OutputInfo>();
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) return currentFiles;

        for (File f : list) {
            if (f.isDirectory()) {
                OutputInfo newOutputInfo = new OutputInfo(f.getName(), f.getPath(), f.lastModified());
                currentFiles.put(f.getPath(), newOutputInfo);
            }
        }

        return currentFiles;
    }

    private ArrayList<OutputInfo> getCurrentFilesList(HashMap<String, OutputInfo> currentFilesMap) {
        ArrayList<OutputInfo> currentFiles = new ArrayList<OutputInfo>(currentFilesMap.values());
        return currentFiles;
    }
}