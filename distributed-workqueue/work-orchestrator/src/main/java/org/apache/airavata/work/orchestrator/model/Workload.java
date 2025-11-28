package org.apache.airavata.work.orchestrator.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "workload")
public class Workload {
    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false, length = 48)
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private WorkloadStatus status;

    @Column(name = "last_updated")
    private long lastUpdated;

    @OneToMany(mappedBy = "workload", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkItem> workItems;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<WorkItem> getWorkItems() {
        return workItems;
    }

    public void setWorkItems(List<WorkItem> workItems) {
        this.workItems = workItems;
    }

    public WorkloadStatus getStatus() {
        return status;
    }

    public void setStatus(WorkloadStatus status) {
        this.status = status;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
