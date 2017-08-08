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
package org.apache.airavata.models.runners.ssh;

import org.apache.airavata.models.resources.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHServerInfo extends ServerInfo {
    private final static Logger logger = LoggerFactory.getLogger(SSHServerInfo.class);

    SSHKeyAuthentication authentication;
    int sshPort;

    public SSHServerInfo(String userName, String host, SSHKeyAuthentication authentication, int port){
            super(userName, host, ComProtocol.SSH, port);
            this.authentication = authentication;
            this.sshPort = port;
    }

    public SSHServerInfo(String userName, String host, SSHKeyAuthentication authentication){
        super(userName, host, ComProtocol.SSH, 22);
        this.authentication = authentication;
        this.sshPort = 22;
    }

    public SSHKeyAuthentication getAuthentication() {
        return authentication;
    }

    public int getSshPort() {
        return sshPort;
    }
}