package org.apache.airavata.k8s.api.server.repository.task;

import org.apache.airavata.k8s.api.server.model.task.TaskOutPort;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public interface TaskOutPortRepository extends CrudRepository<TaskOutPort, Long> {
    Optional<TaskOutPort> findByName(String name);
    Optional<TaskOutPort> findByReferenceIdAndTaskModel_Id(int referenceId, long taskId);
}
