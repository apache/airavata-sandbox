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
package org.apache.airavata.tools.workflow.monitoring.bean;

import java.util.ArrayList;
import java.util.List;

public class TemplateInfoBean {

    private String templateID;

    // I will store the status and the status count in two different array lists. The count for status[i] will be in
    // statusCount[i].
    // To optimize the number of array list usages, I defined a an initial capacity as 3 assuming most of the time
    // each template may exist in one of the three status.

    private int AVERAGE_STATUS_SIZE = 3;

    private long totalCount = 0;

    private List<String> status = new ArrayList<String>(3);
    private List<Long> statusCount = new ArrayList<Long>(AVERAGE_STATUS_SIZE);

    public TemplateInfoBean(String templateID) {
        this.templateID = templateID;
    }

    public void addStatusInfo(String status, long statusCount) {
        this.status.add(status);
        this.statusCount.add(new Long(statusCount));
        totalCount += statusCount;
    }

    public String getTemplateID() {
        return templateID;
    }

    public List<String> getAllStatusNames() {
        return status;
    }

    public List<Long> getAllStatusCounts() {
        return statusCount;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
