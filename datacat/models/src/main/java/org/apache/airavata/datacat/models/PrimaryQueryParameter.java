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

import java.io.Serializable;

public class PrimaryQueryParameter implements Serializable {

    private String field;
    private String firstParameter;
    //In some cases we need a second parameter e.g. range query
    private String secondParameter;
    private PrimaryQueryType primaryQueryType;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFirstParameter() {
        return firstParameter;
    }

    public void setFirstParameter(String firstParameter) {
        this.firstParameter = firstParameter;
    }

    public String getSecondParameter() {
        return secondParameter;
    }

    public void setSecondParameter(String secondParameter) {
        this.secondParameter = secondParameter;
    }

    public PrimaryQueryType getPrimaryQueryType() {
        return primaryQueryType;
    }

    public void setPrimaryQueryType(PrimaryQueryType primaryQueryType) {
        this.primaryQueryType = primaryQueryType;
    }
}
