package org.apache.airavata.work.orchestrator.model;

import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "work_item")
public class WorkItem {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false, length = 48)
    private String id;

    @Column(name = "status")
    private WorkItemStatus status;

    @Column(name = "last_updated")
    private long lastUpdated;

    @Column(name = "work_config")
    private String workConfig;

    @Column(name = "airavata_experiment_id")
    private String airavataExperimentId;

    @Column(name = "airavata_experiment_status")
    private String airavataExperimentStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "workload_id")
    private Workload workload;

    @Column(name = "workload_id", insertable = false, updatable = false)
    private String workloadId;

    public String getId() {
        return id;
    }

    public WorkItemStatus getStatus() {
        return status;
    }

    public void setStatus(WorkItemStatus status) {
        this.status = status;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getWorkConfig() {
        return workConfig;
    }

    public void setWorkConfig(String workConfig) {
        this.workConfig = workConfig;
    }

    public String getAiravataExperimentId() {
        return airavataExperimentId;
    }

    public void setAiravataExperimentId(String airavataExperimentId) {
        this.airavataExperimentId = airavataExperimentId;
    }

    public String getAiravataExperimentStatus() {
        return airavataExperimentStatus;
    }

    public void setAiravataExperimentStatus(String airavataExperimentStatus) {
        this.airavataExperimentStatus = airavataExperimentStatus;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    public String getWorkloadId() {
        return workloadId;
    }

    public void setWorkloadId(String workloadId) {
        this.workloadId = workloadId;
    }

}
