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
package org.apache.airavata.datacat.models;

import java.util.Map;

public class OutputMetadataDTO {

    private String id = "";

    private String experimentName = "";

    private String outputPath = "";

    private String ownerId = "";

    public String gatewayId = "";

    private String applicationName = "";

    private String experimentId = "";

    private String createdDate = "";

    private String host = "";

    //This field is added for the requirements of Solr (it is easy to encode json to object when this field
    // is present).
    private String _version_ = "";

    //Custom Metadata fields
    //The content of this field depends on the parsers
    //The relevant parsers populate this data entity
    private Map<String, String> customMetaData;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExperimentName() {
        return experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String get_version_() {
        return _version_;
    }

    public void set_version_(String _version_) {
        this._version_ = _version_;
    }

    public Map<String, String> getCustomMetaData() {
        return customMetaData;
    }

    public void setCustomMetaData(Map<String, String> customMetaData) {
        this.customMetaData = customMetaData;
    }

}
