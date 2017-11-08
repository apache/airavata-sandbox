package org.apache.airavata.k8s.api.server.repository.workflow;

import org.apache.airavata.k8s.api.server.model.workflow.Workflow;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public interface WorkflowRepository extends CrudRepository<Workflow, Long> {
    public Optional<Workflow> findById(long id);
}
