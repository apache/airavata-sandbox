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
package org.apache.airavata.gsi.ssh.util;

import com.jcraft.jsch.*;
import com.sun.tools.javac.util.Paths;
import org.apache.airavata.gsi.ssh.api.CommandOutput;
import org.apache.airavata.gsi.ssh.api.authentication.GSIAuthenticationInfo;
import org.apache.airavata.gsi.ssh.api.SSHApiException;
import org.apache.airavata.gsi.ssh.api.ServerInfo;
import org.apache.airavata.gsi.ssh.config.ConfigReader;
import org.apache.airavata.gsi.ssh.impl.SystemCommandOutput;
import org.apache.airavata.gsi.ssh.jsch.ExtendedJSch;
import org.slf4j.*;

import java.io.*;

/**
 * This class is going to be useful to SCP a file to a remote grid machine using my proxy credentials
 */
public class SSHUtils {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SSHUtils.class);

    static {
        JSch.setConfig("gssapi-with-mic.x509", "org.apache.airavata.gsi.ssh.GSSContextX509");
        JSch.setConfig("userauth.gssapi-with-mic", "com.jcraft.jsch.UserAuthGSSAPIWithMICGSSCredentials");

    }

    private ServerInfo serverInfo;

    private GSIAuthenticationInfo authenticationInfo;

    private ConfigReader configReader;

    /**
     * We need to pass certificateLocation when we use SCPTo method standalone
     *
     * @param serverInfo
     * @param authenticationInfo
     * @param certificateLocation
     * @param configReader
     */
    public SSHUtils(ServerInfo serverInfo, GSIAuthenticationInfo authenticationInfo, String certificateLocation, ConfigReader configReader) {
        System.setProperty("X509_CERT_DIR", certificateLocation);
        this.serverInfo = serverInfo;
        this.authenticationInfo = authenticationInfo;
        this.configReader = configReader;
    }

    /**
     * This can be used when use SCPTo method within SSHAPi because SSHApiFactory already set the system property certificateLocation
     *
     * @param serverInfo
     * @param authenticationInfo
     * @param configReader
     */
    public SSHUtils(ServerInfo serverInfo, GSIAuthenticationInfo authenticationInfo
            , ConfigReader configReader) {
        this.serverInfo = serverInfo;
        this.authenticationInfo = authenticationInfo;
        this.configReader = configReader;
    }

    /**
     * This  method will scp the lFile to the rFile location
     *
     * @param rFile remote file Path to use in scp
     * @param lFile local file path to use in scp
     * @throws IOException
     * @throws JSchException
     * @throws org.apache.airavata.gsi.ssh.api.SSHApiException
     *
     */
    public String scpTo(String rFile, String lFile) throws IOException, JSchException, SSHApiException {
        FileInputStream fis = null;
        String prefix = null;
        if (new File(lFile).isDirectory()) {
            prefix = lFile + File.separator;
        }
        JSch jsch = new ExtendedJSch();

        log.debug("Connecting to server - " + serverInfo.getHost() + ":" + serverInfo.getPort() + " with user name - "
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

        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rFile;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(lFile);

        if (ptimestamp) {
            command = "T " + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (lFile.lastIndexOf('/') > 0) {
            command += lFile.substring(lFile.lastIndexOf('/') + 1);
        } else {
            command += lFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lFile
        fis = new FileInputStream(lFile);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }
        fis.close();
        fis = null;
        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        channel.disconnect();
        session.disconnect();
        int i = lFile.lastIndexOf("\\");
        String substring = lFile.substring(i + 1);
        return rFile + File.separator + substring;
    }

    public static String scpTo(String rFile, String lFile, Session session) throws IOException, JSchException, SSHApiException {
        FileInputStream fis = null;
        String prefix = null;
        if (new File(lFile).isDirectory()) {
            prefix = lFile + File.separator;
        }
        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + rFile;
        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            System.exit(0);
        }

        File _lfile = new File(lFile);

        if (ptimestamp) {
            command = "T " + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (lFile.lastIndexOf('/') > 0) {
            command += lFile.substring(lFile.lastIndexOf('/') + 1);
        } else {
            command += lFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in) != 0) {
            System.exit(0);
        }

        // send a content of lFile
        fis = new FileInputStream(lFile);
        byte[] buf = new byte[1024];
        while (true) {
            int len = fis.read(buf, 0, buf.length);
            if (len <= 0) break;
            out.write(buf, 0, len); //out.flush();
        }
        fis.close();
        fis = null;
        // send '\0'
        buf[0] = 0;
        out.write(buf, 0, 1);
        out.flush();
        if (checkAck(in) != 0) {
            System.exit(0);
        }
        out.close();

        channel.disconnect();
        int i = lFile.lastIndexOf("\\");
        String substring = lFile.substring(i + 1);
        return rFile + File.separator + substring;
    }

    public static Session makeDirectory(String path, Session session) throws IOException, JSchException, SSHApiException {

        // exec 'scp -t rfile' remotely
        String command = "mkdir " + path;
        Channel channel = session.openChannel("exec");
        CommandOutput commandOutput = new SystemCommandOutput();
        ((ChannelExec) channel).setCommand(command);

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(commandOutput.getStandardError());
        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();
            session.disconnect();

            throw new SSHApiException("Unable to retrieve command output. Command - " + command +
                    " on server - " + session.getHost() + ":" + session.getPort() +
                    " connecting user name - "
                    + session.getUserName(), e);
        }

        commandOutput.onOutput(channel);

        channel.disconnect();
        session.disconnect();

        channel.disconnect();
        return session;
    }

    static int checkAck(InputStream in) throws IOException {
        int b = in.read();
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
