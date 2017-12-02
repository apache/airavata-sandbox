/**
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
 */
package org.apache.airavata.k8s.api.server.model.process;

import org.apache.airavata.k8s.api.server.model.commons.ErrorModel;
import org.apache.airavata.k8s.api.server.model.experiment.Experiment;
import org.apache.airavata.k8s.api.server.model.task.TaskModel;
import org.apache.airavata.k8s.api.server.model.workflow.Workflow;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */

@Entity
@Table(name = "PROCESS_MODEL")
public class ProcessModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Experiment experiment;

    @ManyToOne
    private Workflow workflow;

    private String name;

    private long creationTime;
    private long lastUpdateTime;

    @OneToMany(mappedBy = "processModel", cascade = CascadeType.ALL)
    private List<ProcessStatus> processStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "parentProcess", cascade = CascadeType.ALL)
    private List<TaskModel> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "processModel", cascade = CascadeType.ALL)
    private List<ProcessBootstrapData> processBootstrapData = new ArrayList<>();

    private String taskDag;

    @OneToMany
    private List<ErrorModel> processErrors = new ArrayList<>();

    private String experimentDataDir;

    private ProcessType processType = ProcessType.EXPERIMENT;

    public long getId() {
        return id;
    }

    public ProcessModel setId(long id) {
        this.id = id;
        return this;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public ProcessModel setExperiment(Experiment experiment) {
        this.experiment = experiment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProcessModel setName(String name) {
        this.name = name;
        return this;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public ProcessModel setCreationTime(long creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public ProcessModel setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    public List<ProcessStatus> getProcessStatuses() {
        return processStatuses;
    }

    public ProcessModel setProcessStatuses(List<ProcessStatus> processStatuses) {
        this.processStatuses = processStatuses;
        return this;
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

    public ProcessModel setTasks(List<TaskModel> tasks) {
        this.tasks = tasks;
        return this;
    }

    public String getTaskDag() {
        return taskDag;
    }

    public ProcessModel setTaskDag(String taskDag) {
        this.taskDag = taskDag;
        return this;
    }

    public List<ErrorModel> getProcessErrors() {
        return processErrors;
    }

    public ProcessModel setProcessErrors(List<ErrorModel> processErrors) {
        this.processErrors = processErrors;
        return this;
    }

    public String getExperimentDataDir() {
        return experimentDataDir;
    }

    public ProcessModel setExperimentDataDir(String experimentDataDir) {
        this.experimentDataDir = experimentDataDir;
        return this;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public ProcessModel setProcessType(ProcessType processType) {
        this.processType = processType;
        return this;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public ProcessModel setWorkflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public List<ProcessBootstrapData> getProcessBootstrapData() {
        return processBootstrapData;
    }

    public ProcessModel setProcessBootstrapData(List<ProcessBootstrapData> processBootstrapData) {
        this.processBootstrapData = processBootstrapData;
        return this;
    }

    public enum ProcessType {
        WORKFLOW, EXPERIMENT;
    }
}
