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

package org.apache.airavata.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.airavata.filetransfer.utils.ServiceConstants;

public class ExectionContext {

    private String testingHost;
    
    private String lonestarGridFTP;
    private String rangerGridFTP;
    private String trestlesGridFTP;
    
    private String gridFTPServerSource;
    private String sourcedataLocation;
    private String gridFTPServerDest;
    private String destdataLocation;

    public static final String PROPERTY_FILE = "airavata-gridftp-client.properties";

    public ExectionContext() throws IOException {
        loadConfigration();
    }

    private void loadConfigration() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertyStream = classLoader.getResourceAsStream(PROPERTY_FILE);

        Properties properties = new Properties();
        if (propertyStream != null) {
            properties.load(propertyStream);
            
            String testinghost = properties.getProperty(ServiceConstants.TESTINGHOST);
            
            String lonestargridftp = properties.getProperty(ServiceConstants.LONESTARGRIDFTPEPR);
            String rangergridftp = properties.getProperty(ServiceConstants.RANGERGRIDFTPEPR);
            String trestlesgridftp = properties.getProperty(ServiceConstants.TRESTLESGRIDFTPEPR);

            String gridFTPServerSource = properties.getProperty(ServiceConstants.GRIDFTPSERVERSOURCE);
            String gridFTPSourcePath = properties.getProperty(ServiceConstants.GRIDFTPSOURCEPATH);
            String gridFTPServerDest = properties.getProperty(ServiceConstants.GRIDFTPSERVERDEST);
            String gridFTPDestPath = properties.getProperty(ServiceConstants.GRIDFTPDESTPATH);

            if (testinghost != null) {
                this.testingHost = testinghost;
            }
            
            if (lonestargridftp != null) {
                this.lonestarGridFTP = lonestargridftp;
            }
            if (rangergridftp != null) {
                this.rangerGridFTP= rangergridftp;
            }
            if (trestlesgridftp != null) {
                this.trestlesGridFTP = trestlesgridftp;
            }
            
            if (gridFTPServerSource != null && !gridFTPServerSource.isEmpty()) {
                this.gridFTPServerSource = gridFTPServerSource;
            }
            if (gridFTPSourcePath != null && !gridFTPSourcePath.isEmpty()) {
                this.sourcedataLocation = gridFTPSourcePath;
            }
            if (gridFTPServerDest != null && !gridFTPServerDest.isEmpty()) {
                this.gridFTPServerDest = gridFTPServerDest;
            }
            if (gridFTPDestPath != null && !gridFTPDestPath.isEmpty()) {
                this.destdataLocation = gridFTPDestPath;
            }

        }
    }

    public String getTestingHost() {
        return testingHost;
    }

    public void setTestingHost(String testingHost) {
        this.testingHost = testingHost;
    }

    public String getLonestarGridFTP() {
        return lonestarGridFTP;
    }

    public void setLonestarGridFTP(String lonestarGridFTP) {
        this.lonestarGridFTP = lonestarGridFTP;
    }

    public String getRangerGridFTP() {
        return rangerGridFTP;
    }

    public void setRangerGridFTP(String rangerGridFTP) {
        this.rangerGridFTP = rangerGridFTP;
    }

    public String getTrestlesGridFTP() {
        return trestlesGridFTP;
    }

    public void setTrestlesGridFTP(String trestlesGridFTP) {
        this.trestlesGridFTP = trestlesGridFTP;
    }

    public String getGridFTPServerSource() {
        return gridFTPServerSource;
    }

    public void setGridFTPServerSource(String gridFTPServerSource) {
        this.gridFTPServerSource = gridFTPServerSource;
    }

    public String getSourcedataLocation() {
        return sourcedataLocation;
    }

    public void setSourcedataLocation(String sourcedataLocation) {
        this.sourcedataLocation = sourcedataLocation;
    }

    public String getGridFTPServerDest() {
        return gridFTPServerDest;
    }

    public void setGridFTPServerDest(String gridFTPServerDest) {
        this.gridFTPServerDest = gridFTPServerDest;
    }

    public String getDestdataLocation() {
        return destdataLocation;
    }

    public void setDestdataLocation(String destdataLocation) {
        this.destdataLocation = destdataLocation;
    }
}
