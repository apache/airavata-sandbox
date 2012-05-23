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

package org.apache.airavata.tools.workflow.monitoring.test;

import org.apache.airavata.tools.workflow.monitoring.Server;
import org.apache.airavata.tools.workflow.monitoring.Util;
import junit.framework.TestCase;

public class WorkflowIDParsingTest extends TestCase {

    public static String TEST_ENVELOP = "<S:Envelope " + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<S:Body>" + PublishSendingFaultTest.message + "</S:Body>" + "</S:Envelope>";

    public void testPasringWOrkflowID() {
        assertEquals(PublishSendingFaultTest.workflowID,
                Util.getWorkflowID(TEST_ENVELOP, Server.SENDING_FAULT, "workflowID"));
    }
}