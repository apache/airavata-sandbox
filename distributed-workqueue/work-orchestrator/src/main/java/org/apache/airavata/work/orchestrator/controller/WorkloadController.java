package org.apache.airavata.work.orchestrator.controller;

import java.util.Optional;

import org.apache.airavata.work.orchestrator.model.WorkItem;
import org.apache.airavata.work.orchestrator.model.WorkItemStatus;
import org.apache.airavata.work.orchestrator.model.Workload;
import org.apache.airavata.work.orchestrator.service.WorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workload")
public class WorkloadController {

    @Autowired
    private WorkloadService workloadService;

    @PostMapping("/workitem")
    public ResponseEntity<WorkItem> createWorkItem(@RequestBody WorkItem workItem) {
        WorkItem createdItem = workloadService.createWorkItem(workItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/workitem/{id}")
    public ResponseEntity<WorkItem> getWorkItem(@PathVariable String id) {
        Optional<WorkItem> item = workloadService.getWorkItem(id);
        return ResponseEntity.ok(item.orElse(null));
    }

    @GetMapping("/workitem")
    public ResponseEntity<?> getAllWorkItems() {
        return ResponseEntity.ok(workloadService.getAllWorkItems());
    }

    @DeleteMapping("/workitem/{id}")
    public ResponseEntity<Void> deleteWorkItem(@PathVariable String id) {
        workloadService.deleteWorkItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Workload> createWorkload(@RequestBody Workload workload) {
        Workload createdWorkload = workloadService.createWorkload(workload);
        return new ResponseEntity<>(createdWorkload, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workload> getWorkload(@PathVariable String id) {
        Optional<Workload> workload = workloadService.getWorkload(id);
        return ResponseEntity.ok(workload.orElse(null));
    }

    @GetMapping
    public ResponseEntity<Iterable<Workload>> getAllWorkloads() {
        return ResponseEntity.ok(workloadService.getAllWorkloads());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkload(@PathVariable String id) {
        workloadService.deleteWorkload(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/lease/{workloadId}/{airavataExperimentId}")
    public ResponseEntity<WorkItem> leaseWorkItem(@PathVariable String workloadId, @PathVariable String airavataExperimentId) {
        Optional<WorkItem> leasedItem = workloadService.leaseWorkItem(workloadId, airavataExperimentId);
        return leasedItem.map(item -> ResponseEntity.ok(item))
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping("/workitem/{workItemId}/status")
    public ResponseEntity<WorkItem> updateWorkItemStatus(@PathVariable String workItemId, @RequestParam("status") String status) {
        Optional<WorkItem> updatedItem = workloadService.updateWorkItemStatus(workItemId, Enum.valueOf(WorkItemStatus.class, status));
        return updatedItem.map(item -> ResponseEntity.ok(item))
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
