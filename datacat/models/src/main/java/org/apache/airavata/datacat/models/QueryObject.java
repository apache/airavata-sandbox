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

import java.util.List;

public class QueryObject {

    private String username;

    private boolean queryStringSet =false;

    private String queryString;

    private String[] userGroups;

    private List<PrimaryQueryParameter> primaryQueryParameterList;

    private int startRow=0;

    private int numberOfRows =0;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public boolean isQueryStringSet() {
        return queryStringSet;
    }

    public void setQueryStringSet(boolean stringQuery) {
        this.queryStringSet = stringQuery;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(String[] userGroups) {
        this.userGroups = userGroups;
    }

    public List<PrimaryQueryParameter> getPrimaryQueryParameterList() {
        return primaryQueryParameterList;
    }

    public void setPrimaryQueryParameterList(List<PrimaryQueryParameter> primaryQueryParameterList) {
        this.primaryQueryParameterList = primaryQueryParameterList;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
