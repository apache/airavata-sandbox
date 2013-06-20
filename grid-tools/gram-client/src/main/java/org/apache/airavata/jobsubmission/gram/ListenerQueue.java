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

package org.apache.airavata.jobsubmission.gram;

import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.ietf.jgss.GSSException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 6/17/13
 * Time: 2:15 PM
 */

public class ListenerQueue extends Thread {

    private final Queue<JobListenerThread> qe;

    private static ListenerQueue listenerQueue;

    private boolean shutDown = false;

    private ListenerQueue() {
        qe = new ConcurrentLinkedQueue<JobListenerThread>();
    }

    public static ListenerQueue getInstance() {

        if (null == listenerQueue) {
            listenerQueue = new ListenerQueue();
            return listenerQueue;
        } else {
            return listenerQueue;
        }
    }

    public void run() {

        while (!shutDown) {

            startListener();

            try {
                synchronized (qe) {
                    qe.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void     stopListenerQueue() {
        shutDown = true;

        synchronized (qe) {
            qe.notify();
        }

        listenerQueue = null;

        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void startListenerQueue() {
        shutDown = false;
        this.start();
    }

    public void startListener() {

        while(!qe.isEmpty()) {
            JobListenerThread jobListenerThread = qe.poll();
            jobListenerThread.start();
        }
    }

    public synchronized void addJob(GramJob job) {

        qe.offer(new JobListenerThread(job));

        synchronized (qe) {
            qe.notifyAll();
        }
    }

    class JobListenerThread extends Thread {

        private GramJob listeningJob;

        public JobListenerThread(GramJob job) {
            listeningJob = job;
        }

        public void run() {
            try {
                listeningJob.bind();
            } catch (GramException e) {
                e.printStackTrace();
            } catch (GSSException e) {
                e.printStackTrace();
            }
        }
    }


}
