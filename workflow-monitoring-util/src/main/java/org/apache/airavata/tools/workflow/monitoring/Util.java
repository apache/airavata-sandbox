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
import java.util.Iterator;

import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlInfosetBuilder;
import org.xmlpull.v1.builder.XmlNamespace;

import xsul.XmlConstants;

public class Util {

    private final static XmlInfosetBuilder builder = XmlConstants.BUILDER;

    public static String getWorkflowID(String message, String firstElement, String attrName) {
        XmlElement messageEl = builder.parseFragmentFromReader(new StringReader(message));
        XmlElement body = messageEl.element(null, "Body");
        if (body == null)
            return null;
        XmlElement firstElmt = body.element(null, firstElement);
        if (firstElmt == null)
            return null;
        XmlElement source = firstElmt.element(null, "notificationSource");
        if (source == null)
            return null;
        XmlNamespace ns = source.getNamespace();

        String workflowid = source.getAttributeValue(ns.getNamespaceName(), attrName);
        return workflowid;
    }

    public static String getDN(String message) {
        XmlElement messageEl = builder.parseFragmentFromReader(new StringReader(message));
        XmlElement body = messageEl.element(null, "Body");
        if (body == null)
            return null;
        XmlElement firstelemt = body.element(null, "invokingService");
        if (firstelemt == null)
            return null;
        XmlElement annotation = firstelemt.element(null, "annotation");
        if (null == annotation)
            return null;

        XmlElement dn = annotation.element(null, "userDN");
        if (null == dn)
            return null;

        Iterator children = dn.children();
        if (children.hasNext()) {
            return (String) children.next();
        }
        return null;
    }

    public static String getInitialID(String message) {
        XmlElement messageEl = builder.parseFragmentFromReader(new StringReader(message));
        XmlElement body = messageEl.element(null, "Body");
        if (body == null)
            return null;
        XmlElement firstelemt = body.element(null, "invokingService");
        if (firstelemt == null)
            return null;
        XmlElement request = firstelemt.element(null, "request");
        if (null == request)
            return null;
        XmlElement header = request.element(null, "header");
        if (null == header) {
            return null;
        }
        XmlElement secHeader = header.element(null, "Header");
        if (secHeader == null)
            return null;
        XmlElement context = secHeader.element(null, "context");
        XmlElement id = context.element(null, "workflow-instance-id");
        if (null == context)
            return null;
        Iterator itr = id.children();
        if (itr.hasNext()) {
            return (String) itr.next();
        }
        return null;

    }

}