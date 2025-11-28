package org.apache.airavata.work.orchestrator.service;

import java.util.List;
import java.util.Optional;

import org.apache.airavata.work.orchestrator.model.WorkItem;
import org.apache.airavata.work.orchestrator.model.WorkItemStatus;
import org.apache.airavata.work.orchestrator.model.Workload;
import org.apache.airavata.work.orchestrator.repository.WorkItemRepository;
import org.apache.airavata.work.orchestrator.repository.WorkloadRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkloadService {

    private WorkItemRepository workItemRepository;
    private WorkloadRepository workloadRepository;

    public WorkloadService(WorkItemRepository workItemRepository, WorkloadRepository workloadRepository) {
        this.workItemRepository = workItemRepository;
        this.workloadRepository = workloadRepository;
    }


    public Workload createWorkload(Workload workload) {
        return workloadRepository.save(workload);
    }

    public Optional<Workload> getWorkload(String id) {
        return workloadRepository.findById(id);
    }

    public Iterable<Workload> getAllWorkloads() {
        return workloadRepository.findAll();
    }

    public void deleteWorkload(String id) {
        workloadRepository.deleteById(id);
    }

    public WorkItem createWorkItem(WorkItem workItem) {
        // If workloadId is set but workload relationship isn't, fetch and set it
        if (workItem.getWorkloadId() != null && workItem.getWorkload() == null) {
            Optional<Workload> workloadOpt = workloadRepository.findById(workItem.getWorkloadId());
            if (workloadOpt.isPresent()) {
                workItem.setWorkload(workloadOpt.get());
            } else {
                throw new RuntimeException("Workload not found with id: " + workItem.getWorkloadId());
            }
        }
        return workItemRepository.save(workItem);
    }


    public Optional<WorkItem> getWorkItem(String id) {
        return workItemRepository.findById(id);
    }

    public Iterable<WorkItem> getAllWorkItems() {
        return workItemRepository.findAll();
    }

    public void deleteWorkItem(String id) {
        workItemRepository.deleteById(id);
    }

    public Optional<WorkItem> leaseWorkItem(String workloadId, String airavataExperimentId) {

        synchronized(this) { // Make sure only one work item is leased at a time
            List<WorkItem> workItemOpt = workItemRepository.findByWorkloadIdAndStatus(workloadId, WorkItemStatus.NOT_ASSIGNED);
            if (!workItemOpt.isEmpty()) {
                WorkItem workItem = workItemOpt.get(0);
                workItem.setStatus(WorkItemStatus.IN_PROGRESS);
                workItem.setAiravataExperimentId(airavataExperimentId);
                workItemRepository.save(workItem);
                return Optional.of(workItem);
            }
            return Optional.empty();
        }
    }

    public Optional<WorkItem> updateWorkItemStatus(String workItemId, WorkItemStatus status) {
        Optional<WorkItem> workItemOpt = workItemRepository.findById(workItemId);
        if (workItemOpt.isPresent()) {
            WorkItem workItem = workItemOpt.get();
            workItem.setStatus(status);
            workItemRepository.save(workItem);
            return Optional.of(workItem);
        }
        return Optional.empty();
    }

}
