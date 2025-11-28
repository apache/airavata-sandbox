package org.apache.airavata.work.orchestrator.repository;

import java.util.List;

import org.apache.airavata.work.orchestrator.model.WorkItem;
import org.apache.airavata.work.orchestrator.model.WorkItemStatus;
import org.springframework.data.repository.CrudRepository;

public interface WorkItemRepository extends CrudRepository<WorkItem, String> {
    public List<WorkItem> findByWorkloadIdAndStatus(String workloadId, WorkItemStatus status);
}
