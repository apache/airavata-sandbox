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

package org.apache.airavata.gsi.ssh.api;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 8/15/13
 * Time: 10:48 AM
 */

import com.jcraft.jsch.JSch;
import org.apache.airavata.gsi.ssh.impl.DefaultSSHApi;

import java.io.IOException;

/**
 * Factory class to create SSH API implementations.
 */
public class SSHApiFactory {

    static {
        JSch.setConfig("gssapi-with-mic.x509", "org.apache.airavata.gsi.ssh.GSSContextX509");
        JSch.setConfig("userauth.gssapi-with-mic", "com.jcraft.jsch.UserAuthGSSAPIWithMICGSSCredentials");

    }

    /**
     * Creates a new instance of SSHAPI. For the moment we only have default implementation.
     * i.e. org.apache.airavata.gsi.ssh.impl.DefaultSSHApi
     * @param certificateLocation The file location where certificates are stored.
     * @return An SSHAPI implementation.
     * @throws SSHApiException If an error occurred while initialization.
     */
    public static SSHApi createSSHApi(String certificateLocation) throws SSHApiException {

        System.setProperty("X509_CERT_DIR", certificateLocation);
        return new DefaultSSHApi();

    }
}
