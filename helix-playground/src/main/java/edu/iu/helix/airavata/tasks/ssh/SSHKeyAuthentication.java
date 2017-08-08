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
package edu.iu.helix.airavata.tasks.ssh;


public class SSHKeyAuthentication extends Authentication {
    private byte[] privateKey;
    private byte[] publicKey;
    private String passphrase;
    private String knownHostsFilePath;
    private String strictHostKeyChecking; // yes or no

    public SSHKeyAuthentication(String userName, byte[] privateKey, byte[] publicKey, String passphrase, String knownHostsFilePath, boolean strictHostKeyChecking) {
        this.userName = userName;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.passphrase = passphrase;
        this.knownHostsFilePath = knownHostsFilePath;
        if(strictHostKeyChecking){
            this.strictHostKeyChecking = "yes";
        }else{
            this.strictHostKeyChecking = "no";
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getKnownHostsFilePath() {
        return knownHostsFilePath;
    }

    public void setKnownHostsFilePath(String knownHostsFilePath) {
        this.knownHostsFilePath = knownHostsFilePath;
    }

    public String getStrictHostKeyChecking() {
        return strictHostKeyChecking;
    }

    public void setStrictHostKeyChecking(String strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
    }
}
