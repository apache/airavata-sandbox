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
package org.apache.airavata.gfac.handler;

import org.apache.airavata.commons.gfac.type.ActualParameter;
import org.apache.airavata.commons.gfac.type.MappingFactory;
import org.apache.airavata.gfac.GFacException;
import org.apache.airavata.gfac.context.JobExecutionContext;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OutputEmailHandler implements GFacHandler {
    public void invoke(JobExecutionContext jobExecutionContext) throws GFacHandlerException, GFacException {
        String emailContent = "";
        Map<String, Object> parameters = jobExecutionContext.getOutMessageContext().getParameters();
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            ActualParameter output = (ActualParameter) parameters.get(key);
            String s = MappingFactory.toString(output);
            emailContent = emailContent + "\n" + key + ":" + s;
        }
        try {
            sendEmail(emailContent);
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void sendEmail(String mailContent) throws MessagingException {
        String host = "smtp.gmail.com";
    String from = "airavatamail";
    String pass = "apachetlp";
    Properties props = System.getProperties();
    props.put("mail.smtp.starttls.enable", "true"); // added this line
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.user", from);
    props.put("mail.smtp.password", pass);
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");

    String[] to = {"glahiru@gmail.com"}; // added this line

    Session session = Session.getDefaultInstance(props, null);
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));

    InternetAddress[] toAddress = new InternetAddress[to.length];

    // To get the array of addresses
    for( int i=0; i < to.length; i++ ) { // changed from a while loop
        toAddress[i] = new InternetAddress(to[i]);
    }

    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
    }
    message.setSubject("WorkflowOutputs");
    message.setText(mailContent);
    Transport transport = session.getTransport("smtp");
    transport.connect(host, from, pass);
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
    }
}

