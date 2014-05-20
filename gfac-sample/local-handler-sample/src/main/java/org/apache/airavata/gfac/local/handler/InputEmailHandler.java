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
package org.apache.airavata.gfac.local.handler;

import org.apache.airavata.gfac.core.context.JobExecutionContext;
import org.apache.airavata.gfac.core.handler.GFacHandler;
import org.apache.airavata.gfac.core.handler.GFacHandlerException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.String;
import java.util.Map;
import java.util.Properties;

public class InputEmailHandler implements GFacHandler {
    private Properties props;

    public void initProperties(Properties properties) throws GFacHandlerException {
        props = properties;
    }

    public void invoke(JobExecutionContext jobExecutionContext) throws GFacHandlerException {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication((String) props.get("username"), (String) props.get("password"));
                    }
                });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress((String) props.get("username")));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse((String) props.get("username")));
            message.setSubject("GFAC Input Email");

            Map<String, Object> parameters = jobExecutionContext.getInMessageContext().getParameters();
            StringBuffer buffer = new StringBuffer();
            for (String input : parameters.keySet()) {
                buffer.append("Input Name: input: Input Value: " + parameters.get(input) + "\n");
            }
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
