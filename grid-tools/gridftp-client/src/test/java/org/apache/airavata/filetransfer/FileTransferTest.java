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

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.airavata.filetransfer.utils.GridFtp;
import org.apache.airavata.security.myproxy.SecurityContext;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ietf.jgss.GSSCredential;

import java.io.*;
import java.net.URI;

/**
 * User: AmilaJ (amilaj@apache.org)
 * Date: 5/31/13
 * Time: 12:39 PM
 */

public class FileTransferTest extends TestCase {

    private GSSCredential gssCredential;

    private ExectionContext contextExectionContext;


    public void setUp() throws Exception {
        super.setUp();

        BasicConfigurator.configure();
        Logger logger = Logger.getLogger("GridFTPClient");
        Level lev = Level.toLevel("DEBUG");
        logger.setLevel(lev);
        SecurityContext context = new SecurityContext();
        context.login();
        contextExectionContext = new ExectionContext();


        String targeterp = contextExectionContext.getGridFTPServerDest();
        String remoteDestFile = contextExectionContext.getDestdataLocation();

        URI dirLocation = GridFtp.createGsiftpURI(targeterp,
                remoteDestFile.substring(0, remoteDestFile.lastIndexOf("/")));
        gssCredential = context.getGssCredential();
        System.out.println(dirLocation);

    }

    public void testMakeDir() throws Exception {

        String targetErp = contextExectionContext.getGridFTPServerDest();
        String remoteDestinationFile = contextExectionContext.getDestdataLocation();

        URI dirLocation = GridFtp.createGsiftpURI(targetErp,
                remoteDestinationFile.substring(0, remoteDestinationFile.lastIndexOf("/")));

        GridFtp ftp = new GridFtp();
        ftp.makeDir(dirLocation, gssCredential);
    }

    public void testTransferData() throws Exception {

        String sourceERP = contextExectionContext.getGridFTPServerSource();
        String remoteSrcFile = contextExectionContext.getSourcedataLocation();

        String targetErp = contextExectionContext.getGridFTPServerDest();
        String remoteDestinationFile = contextExectionContext.getDestdataLocation();

        URI srcURI = GridFtp.createGsiftpURI(sourceERP, remoteSrcFile);
        URI destURI = GridFtp.createGsiftpURI(targetErp, remoteDestinationFile);

        GridFtp ftp = new GridFtp();
        ftp.transfer(srcURI, destURI, gssCredential, true);

    }

    public void testDownloadFile() throws Exception {

        String fileName = "./downloaded";

        File deleteFile = new File(fileName);

        if (deleteFile.exists()) {
            if (!deleteFile.delete())
                throw new RuntimeException("Unable to delete file " + fileName);
        }

        File f = new File(fileName);

        GridFtp ftp = new GridFtp();
        ftp.downloadFile(contextExectionContext.getSourceDataFileUri(),
                gssCredential, f);

        Assert.assertTrue(f.exists());

    }

    public void testFileExists() throws Exception {

        GridFtp ftp = new GridFtp();
        Assert.assertTrue(ftp.exists(contextExectionContext.getSourceDataFileUri(), gssCredential));
    }

    public void testUpdateFile() throws Exception {

        String currentDir = System.getProperty("projectDirectory");

        if (currentDir == null)
            currentDir = "src/test/resources";
        else
            currentDir = currentDir + "/src/test/resources";

        String file = currentDir + "/dummy";

        System.out.println("File to upload is " + file);

        File fileToUpload = new File(file);

        Assert.assertTrue(fileToUpload.canRead());

        GridFtp ftp = new GridFtp();
        ftp.updateFile(contextExectionContext.getUploadingFilePathUri(), gssCredential, fileToUpload);

        Assert.assertTrue(ftp.exists(contextExectionContext.getUploadingFilePathUri(), gssCredential));

    }



}
