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

import com.jcraft.jsch.*;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
import org.slf4j.*;

import java.io.*;

/**
 * This class is going to be useful to SCP a file to a remote grid machine using my proxy credentials
 */
public class SCPFrom {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SCPFrom.class);

    private ServerInfo serverInfo;

    private AuthenticationInfo authenticationInfo;

    private ConfigReader configReader;


    public SCPFrom(ServerInfo serverInfo, AuthenticationInfo authenticationInfo, String certificateLocation, ConfigReader configReader) {
        System.setProperty("X509_CERT_DIR", certificateLocation);
        this.serverInfo = serverInfo;
        this.authenticationInfo = authenticationInfo;
        this.configReader = configReader;
    }

    /**
     * This  method will scp the lFile to the rFile location
     * @param rFile   remote file Path to use in scp
     * @param lFile   local file path to use in scp
     * @throws IOException
     * @throws JSchException
     * @throws SSHApiException
     */
    public void scpFrom(String rFile, String lFile) throws IOException, JSchException, SSHApiException {
        FileOutputStream fos = null;
        String prefix = null;
        if (new File(lFile).isDirectory()) {
            prefix = lFile + File.separator;
        }
        JSch jsch = new ExtendedJSch();

        log.info("Connecting to server - " + serverInfo.getHost() + ":" + serverInfo.getPort() + " with user name - "
                + serverInfo.getUserName());

        Session session = null;

        try {
            session = jsch.getSession(serverInfo.getUserName(), serverInfo.getHost(), serverInfo.getPort());
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while creating SSH session." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        java.util.Properties config = this.configReader.getProperties();
        session.setConfig(config);

        // Not a good way, but we dont have any choice
        if (session instanceof ExtendedSession) {
            ((ExtendedSession) session).setAuthenticationInfo(authenticationInfo);
        }

        try {
            session.connect();
        } catch (JSchException e) {
            throw new SSHApiException("An exception occurred while connecting to server." +
                    "Connecting server - " + serverInfo.getHost() + ":" + serverInfo.getPort() +
                    " connecting user name - "
                    + serverInfo.getUserName(), e);
        }

        // exec 'scp -f rFile' remotely
        String command = "scp -f " + rFile;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] buf = new byte[1024];

        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();

        while (true) {
            int c = checkAck(in);
            if (c != 'C') {
                break;
            }

            // read '0644 '
            in.read(buf, 0, 5);

            long filesize = 0L;
            while (true) {
                if (in.read(buf, 0, 1) < 0) {
                    // error
                    break;
                }
                if (buf[0] == ' ') break;
                filesize = filesize * 10L + (long) (buf[0] - '0');
            }

            String file = null;
            for (int i = 0; ; i++) {
                in.read(buf, i, 1);
                if (buf[i] == (byte) 0x0a) {
                    file = new String(buf, 0, i);
                    break;
                }
            }

            //System.out.println("filesize="+filesize+", file="+file);

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            // read a content of lFile
            fos = new FileOutputStream(prefix == null ? lFile : prefix + file);
            int foo;
            while (true) {
                if (buf.length < filesize) foo = buf.length;
                else foo = (int) filesize;
                foo = in.read(buf, 0, foo);
                if (foo < 0) {
                    // error
                    break;
                }
                fos.write(buf, 0, foo);
                filesize -= foo;
                if (filesize == 0L) break;
            }
            fos.close();
            fos = null;

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
        }

        session.disconnect();

        System.exit(0);
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //          -1
        if (b == 0) return b;
        if (b == -1) return b;

        if (b == 1 || b == 2) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            }
            while (c != '\n');
            if (b == 1) { // error
                System.out.print(sb.toString());
            }
            if (b == 2) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

}
