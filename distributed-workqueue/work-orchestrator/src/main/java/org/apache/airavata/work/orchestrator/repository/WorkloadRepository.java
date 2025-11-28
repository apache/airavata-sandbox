package org.apache.airavata.work.orchestrator.repository;

import java.util.List;
import java.util.Optional;

import org.apache.airavata.work.orchestrator.model.Workload;
import org.apache.airavata.work.orchestrator.model.WorkloadStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkloadRepository extends CrudRepository<Workload, String> {

    public Optional<Workload> findById(String id);

    public List<Workload> findByStatus(WorkloadStatus status);

    public List<Workload> findAll();

}
