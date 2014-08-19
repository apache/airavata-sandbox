
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

package org.apache.airavata.scheduler.jobthrottler;

import java.util.*;

public class MetaSchedulerTest {

    public static void main(String[] args) {
        ArrayList<String> experimentData = new ArrayList<String>(); 
        ArrayList<String> statusData = new ArrayList<String>(); 
        Random rand = new Random();
        String hostID = "";
        String queueID = "";
        String gatewayID = "";
        int submitCtr = 0;
        int holdCtr = 0;
        int errorCtr = 0;

    	String sarg0 = args[0];
    	if (sarg0.equals("clear")) {
            System.out.println("Clearing Active Jobs");
            MetaScheduler.clearActiveJobs();	
    	}
    
    	if ((sarg0.equals("run")) || (sarg0.equals("end"))) {
        	int runCount = Integer.parseInt(args[1]);     
            int  n = rand.nextInt(100);
        	hostID = "quarry.uits.iu.edu";
        	if (n < 50) {
            	hostID = "bigred2.uits.iu.edu";            	
            }
            queueID = "debug";
            n = rand.nextInt(100);
            if (n < 25) {
        		queueID = "serial";            	
            }            
            n = rand.nextInt(100);
            if (n < 25) {
        		queueID = "normal";            	
            }            
            n = rand.nextInt(100);
            if (n < 25) {
        		queueID = "long";            	
            }   
            n = rand.nextInt(100);
            gatewayID = "Gateway1";
            n = rand.nextInt(100);
            if (n < 50) {
        		gatewayID = "Gateway2";            	
            }          
            long currentTime = ((long) System.currentTimeMillis()) / 99;
            for (int i=0;i<runCount;i++) {
                experimentData.add(hostID); 
                experimentData.add(queueID);
                experimentData.add(gatewayID);
                experimentData.add(Long.toString(currentTime+i));
        	}
        	if (sarg0.equals("run")) {
                System.out.println("Scheduling " + runCount + " jobs on "
                		+ hostID + " : " + queueID + " for " + gatewayID);
                statusData = MetaScheduler.submitThrottleJob(experimentData);  
                // display results summarized
                int dataNDX = 0;
                while (dataNDX < statusData.size()) {
                	if (statusData.get(dataNDX).equals("SUBMIT")) {
                		submitCtr++;
                	}
                	if (statusData.get(dataNDX).equals("HOLD")) {
                		holdCtr++;
                	}
                	if (statusData.get(dataNDX).equals("ERROR")) {
                		errorCtr++;
                	}	
                	dataNDX++;
                }
                System.out.println("Job States: submit=" + submitCtr + "  hold="
                		+ holdCtr + "  error=" + errorCtr);
        	}
        	if (sarg0.equals("end")) {
                System.out.println("Changing Status of " + runCount + " jobs on "
                		+ hostID + " : " + queueID + " for " + gatewayID);
                MetaScheduler.changeJobStatus(experimentData);        	
        	}
    	}
    }
}