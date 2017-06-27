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

import com.jcraft.jsch.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SSHCommandOutputReader{

    private static final Logger logger = LoggerFactory.getLogger(SSHCommandOutputReader.class);
    String stdOutputString = null;
    String stdErrorString = null;
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    private int exitCode;

    public void onOutput(Channel channel) {
        try {
            this.setStdOutputString(getOutputStream(channel, channel.getInputStream()));
            this.setStdErrorString(new String(errorStream.toByteArray(), "UTF-8"));
            this.exitCode = channel.getExitStatus();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    private String getOutputStream(Channel channel, InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder("");
        byte[] tmp = new byte[1024];
        do {
            while (inputStream.available() > 0) {
                int i = inputStream.read(tmp, 0, 1024);
                if (i < 0) break;
                output.append(new String(tmp, 0, i));
            }
        } while (!channel.isClosed()) ;
        return  output.toString();
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getStdOutputString() {
        return stdOutputString;
    }

    public void setStdOutputString(String stdOutputString) {
        this.stdOutputString = stdOutputString;
    }

    public String getStdErrorString() {
        return stdErrorString;
    }

    public void setStdErrorString(String stdErrorString) {
        this.stdErrorString = stdErrorString;
    }

    public ByteArrayOutputStream getErrorStream() {
        return errorStream;
    }
}