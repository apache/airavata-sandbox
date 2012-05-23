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

package org.apache.airavata.tools.workflow.monitoring;

import java.io.StringReader;
import java.util.Date;
import java.util.Vector;

import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlInfosetBuilder;
import org.xmlpull.v1.builder.XmlNamespace;

import xsul.XmlConstants;

public class Workflow {
    private final static XmlInfosetBuilder builder = XmlConstants.BUILDER;

    private static final int DEFAULT_EXPIRATION_HOURS = 10;

    private String workflowID;

    private Vector<String> faultMessages = new Vector<String>();

    private Date expirationTime;

    private long firstFaultArrivalTimeForCurrentBatch;

    // TODA abstract this to capture generic "Alert" mechanism
    private Vector<String> emails = new Vector<String>();

    private AlertHandler alertHandler;

    public Workflow() {

    }

    public boolean parse(String message) {
        XmlElement messageEl = builder.parseFragmentFromReader(new StringReader(message));
        XmlElement body = messageEl.element(null, "Body");
        if (body == null) {
            return false;
        }
        XmlElement mon = body.element(null, "monitorWorkflow");
        if (mon == null) {
            return false;
        }

        XmlElement source = mon.element(null, "notificationSource");
        if (source == null) {
            return false;
        }
        XmlNamespace ns = source.getNamespace();

        this.workflowID = source.getAttributeValue(ns.getNamespaceName(), "workflowID");
        if (this.workflowID == null) {
            return false;
        }

        XmlElement duration = mon.element(null, "monitoringDuration");
        if (duration == null) {
            this.expirationTime = this.getDateAfter(DEFAULT_EXPIRATION_HOURS);
        } else {
            this.expirationTime = this.getDateAfter(this.getHours(duration.requiredTextContent()));
        }

        this.alertHandler = AlertFactory.createAlertHandler(mon);

        return true;

    }

    // No need for mutex synchronisation the code is written with
    // global event ordering
    public void addFaultMessage(String message) {

        if (this.faultMessages.size() == 0) {
            this.firstFaultArrivalTimeForCurrentBatch = System.currentTimeMillis();
        }
        this.faultMessages.add(message);

    }

    // do not require synchromisation since if a message is missed it will be
    // caught in the next lot;
    public Vector getAllMessagesAndReset() {
        Vector oldmessages = this.faultMessages;
        this.faultMessages = new Vector<String>();
        return oldmessages;
    }

    public Vector<String> getFaultMessages() {
        return faultMessages;
    }

    public String getWorkflowID() {
        return workflowID;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public long getFirstFaultArrivalTimeForCurrentBatch() {
        return this.firstFaultArrivalTimeForCurrentBatch;
    }

    public Date getDateAfter(int hours) {
        long now = System.currentTimeMillis();
        long future = now + hours * 60 * 60 * 1000;

        return new Date(future);
    }

    public int getHours(String str) {
        return Integer.parseInt(str);
    }

    public AlertHandler getAlertHandler() {
        return alertHandler;
    }

}
