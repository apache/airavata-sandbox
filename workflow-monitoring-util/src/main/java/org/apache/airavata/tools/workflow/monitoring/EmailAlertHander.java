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

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlElement;

import xsul.MLogger;

public class EmailAlertHander implements AlertHandler {

    private static MLogger log = MLogger.getLogger();

    private static final String drlead = "drlead@extreme.indiana.edu";

    private static final String messageBoundary = "\n\n------------------\n\n";

    private static final String subject = "Workflow Monitoring service: Error in workflow with id=";

    private static final String from = "Do not Reply<workflow@extreme.indiana.edu>";

    private Vector<String> emails = new Vector<String>();

    public EmailAlertHander(Iterable iterable) {
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            XmlElement element = (XmlElement) iterator.next();
            emails.add(element.requiredTextContent());
        }
    }

    public EmailAlertHander() {
        this.emails.add(this.drlead);
    }

    public void alert(Vector<String> payload, String workflowID) {
        String mailMessage = "";
        for (String message : payload) {
            mailMessage += message;
            mailMessage += messageBoundary;
        }
        this.postMail(this.getStingArray(this.emails), subject + workflowID, mailMessage, from);
    }

    public void postMail(String recipients[], String subject, String message, String from) {
        boolean debug = false;
        log.finest("Intiating emai");
        // Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", "rainier.extreme.indiana.edu");

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        try {
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);

            // Optional : You can also set your custom headers in the Email if
            // you Want
            // msg.addHeader("MyHeaderName", "myHeaderValue");

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            msg.setContent(message, "text/plain");
            Transport.send(msg);
        } catch (AddressException e) {
            throw new WorkflowMonitoringException(e);
        } catch (MessagingException e) {
            throw new WorkflowMonitoringException(e);
        }
        log.finest("Sent mail " + subject);

    }

    private String[] getStingArray(Vector<String> vector) {
        String[] array = new String[vector.size()];
        for (int i = 0; i < vector.size(); ++i) {
            array[i] = vector.get(i);
        }
        return array;
    }

}