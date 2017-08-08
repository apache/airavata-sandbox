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
package org.apache.airavata.runners.ssh;

import com.jcraft.jsch.*;
import org.apache.airavata.models.runners.ssh.SSHApiException;
import org.apache.airavata.models.runners.ssh.SSHKeyAuthentication;
import org.apache.airavata.models.runners.ssh.SSHServerInfo;
import org.apache.airavata.models.runners.ssh.SSHUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class SSHRunner {
    private final static Logger log = LoggerFactory.getLogger(SSHRunner.class);

    public Session createSSHSession(SSHServerInfo serverInfo, SSHKeyAuthentication authentication) throws JSchException {
            JSch jSch = new JSch();
            jSch.addIdentity(UUID.randomUUID().toString(), authentication.getPrivateKey(), authentication.getPublicKey(),
                    authentication.getPassphrase().getBytes());
            Session session = jSch.getSession(serverInfo.getUserName(), serverInfo.getHost(),
                    serverInfo.getSshPort());
            session.setUserInfo(new SSHUserInfo(serverInfo.getUserName(), null, authentication.getPassphrase()));
            if (authentication.getStrictHostKeyChecking().equals("yes")) {
                jSch.setKnownHosts(authentication.getKnownHostsFilePath());
            } else {
                session.setConfig("StrictHostKeyChecking", "no");
            }
            session.connect();

        return session;
    }

    public String scpTo(String routingKey, String localFile, String remoteFile, SSHServerInfo serverInfo,
                        SSHKeyAuthentication authentication) throws IOException, JSchException, SSHApiException {

        Session session =  createSSHSession(serverInfo, authentication);

        FileInputStream fis = null;
        String prefix = null;
        if (new File(localFile).isDirectory()) {
            prefix = localFile + File.separator;
        }
        boolean ptimestamp = true;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + remoteFile;
        Channel channel = session.openChannel("exec");

        SSHCommandOutputReader stdOutReader = new SSHCommandOutputReader();
        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(stdOutReader.getErrorStream());

        // get I/O streams for remote scp
        OutputStream out = channel.getOutputStream();
        InputStream in = channel.getInputStream();

        channel.connect();

        if (checkAck(in) != 0) {
            String error = "Error Reading input Stream";
            log.error(error);
            throw new SSHApiException(error);
        }

        File _lfile = new File(localFile);

        if (ptimestamp) {
            command = "T" + (_lfile.lastModified() / 1000) + " 0";
            // The access time should be sent here,
            // but it is not accessible with JavaAPI ;-<
            command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                String error = "Error Reading input Stream";
                log.error(error);
                throw new SSHApiException(error);
            }
        }

        // send "C0644 filesize filename", where filename should not include '/'
        long filesize = _lfile.length();
        command = "C0644 " + filesize + " ";
        if (localFile.lastIndexOf('/') > 0) {
            command += localFile.substring(localFile.lastIndexOf('/') + 1);
        } else {
            command += localFile;
        }
        command += "\n";
        out.write(command.getBytes());
        out.flush();
        if (checkAck(in) != 0) {
            String error = "Error Reading input Stream";
            log.error(error);
            throw new SSHApiException(error);
        }

        // send a content of localFile
        fis = new FileInputStream(localFile);
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
            String error = "Error Reading input Stream";
            log.error(error);
            throw new SSHApiException(error);
        }
        out.close();
        stdOutReader.onOutput(channel);


        channel.disconnect();
        if (stdOutReader.getStdErrorString().contains("scp:")) {
            throw new SSHApiException(stdOutReader.getStdErrorString());
        }

        session.disconnect();

        //since remote file is always a file  we just return the file
        return remoteFile;
    }

    /**
     * This method will copy a remote file to a local directory
     *
     * @param remoteFile remote file path, this has to be a full qualified path
     * @param localFile  This is the local file to copy, this can be a directory too
     * @return returns the final local file path of the new file came from the remote resource
     */
    public void scpFrom(String routingKey, String remoteFile, String localFile, SSHServerInfo serverInfo,
                        SSHKeyAuthentication authentication) throws IOException,
            JSchException, SSHApiException {
        Session session = createSSHSession(serverInfo, authentication);
        FileOutputStream fos = null;
        try {
            String prefix = null;
            if (new File(localFile).isDirectory()) {
                prefix = localFile + File.separator;
            }

            SSHCommandOutputReader stdOutReader = new SSHCommandOutputReader();

            // exec 'scp -f remotefile' remotely
            String command = "scp -f " + remoteFile;
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(stdOutReader.getErrorStream());

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            if (!channel.isClosed()){
                channel.connect();
            }

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

                // read a content of lfile
                fos = new FileOutputStream(prefix == null ? localFile : prefix + file);
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
                    String error = "Error transfering the file content";
                    log.error(error);
                    throw new SSHApiException(error);
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }
            stdOutReader.onOutput(channel);
            if (stdOutReader.getStdErrorString().contains("scp:")) {
                throw new SSHApiException(stdOutReader.getStdErrorString());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (fos != null) fos.close();
                session.disconnect();
            } catch (Exception ee) {
            }
        }
    }

    public void makeDirectory(String routingKey, String path, SSHServerInfo serverInfo, SSHKeyAuthentication authentication)
            throws IOException, JSchException, Exception {

        Session session = createSSHSession(serverInfo, authentication);

        // exec 'scp -t rfile' remotely
        String command = "mkdir -p " + path;
        Channel channel = session.openChannel("exec");
        SSHCommandOutputReader stdOutReader = new SSHCommandOutputReader();

        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(stdOutReader.getErrorStream());

        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();
            log.error("Unable to retrieve command output. Command - " + command +
                    " on server - " + session.getHost() + ":" + session.getPort() +
                    " connecting user name - "
                    + session.getUserName());
            throw e;
        }
        stdOutReader.onOutput(channel);
        if (stdOutReader.getStdErrorString().contains("mkdir:")) {
            throw new Exception(stdOutReader.getStdErrorString());
        }

        channel.disconnect();
        session.disconnect();
    }

    public List<String> listDirectory(String routingKey, String path, SSHServerInfo serverInfo, SSHKeyAuthentication authentication)
            throws IOException, JSchException, Exception {

        Session session = createSSHSession(serverInfo, authentication);

        // exec 'scp -t rfile' remotely
        String command = "ls " + path;
        Channel channel = session.openChannel("exec");
        SSHCommandOutputReader stdOutReader = new SSHCommandOutputReader();

        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(stdOutReader.getErrorStream());

        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();

            throw new Exception("Unable to retrieve command output. Command - " + command +
                    " on server - " + session.getHost() + ":" + session.getPort() +
                    " connecting user name - "
                    + session.getUserName(), e);
        }
        if (stdOutReader.getStdErrorString().contains("ls:")) {
            throw new Exception(stdOutReader.getStdErrorString());
        }
        channel.disconnect();
        session.disconnect();
        return Arrays.asList(stdOutReader.getStdOutputString().split("\n"));
    }

    public SSHCommandOutputReader executeCommand(String routingKey, String command, SSHServerInfo serverInfo,
                                              SSHKeyAuthentication authentication) throws Exception {

        Session session = createSSHSession(serverInfo, authentication);

        Map<String, String> results = new HashMap<>();

        Channel channel = session.openChannel("exec");
        SSHCommandOutputReader stdOutReader = new SSHCommandOutputReader();

        ((ChannelExec) channel).setCommand(command);
        ((ChannelExec) channel).setErrStream(stdOutReader.getErrorStream());

        try {
            channel.connect();
        } catch (JSchException e) {

            channel.disconnect();

            throw new Exception("Unable to retrieve command output. Command - " + command +
                    " on server - " + session.getHost() + ":" + session.getPort() +
                    " connecting user name - "
                    + session.getUserName(), e);
        }
        stdOutReader.onOutput(channel);
        session.disconnect();

        return stdOutReader;
    }

    public SSHCommandOutputReader executeCommand(String routingKey, String[] commands, SSHServerInfo serverInfo,
                                              SSHKeyAuthentication authentication) throws Exception {
        return executeCommand(routingKey, String.join(" && ", commands), serverInfo, authentication);
    }

    private int checkAck(InputStream in) throws IOException {
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
            log.warn(sb.toString());
        }
        return b;
    }

}