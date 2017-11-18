package org.apache.airavata.helix.tasks.dataout;

import org.apache.airavata.helix.HelixParticipant;
import org.apache.airavata.k8s.api.resources.task.type.TaskTypeResource;
import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class Participant extends HelixParticipant {

    public Participant(String zkAddress, String clusterName, String participantName,
                       String taskTypeName, String apiServerUrl) {
        super(zkAddress, clusterName, participantName, taskTypeName, apiServerUrl);
    }

    @Override
    public Map<String, TaskFactory> getTaskFactory() {
        Map<String, TaskFactory> taskRegistry = new HashMap<String, TaskFactory>();

        TaskFactory dataInTask = new TaskFactory() {
            @Override
            public Task createNewTask(TaskCallbackContext context) {
                return new DataOutputTask(context);
            }
        };

        taskRegistry.put(DataOutputTask.NAME, dataInTask);

        return taskRegistry;
    }

    @Override
    public TaskTypeResource getTaskType() {
        return DataOutputTask.getTaskType();
    }

    public static void main(String args[]) {
        HelixParticipant participant = new Participant(
                "localhost:2199",
                "AiravataDemoCluster",
                "data-out-p1", DataOutputTask.NAME,
                "localhost:8080");
        new Thread(participant).start();
    }
}
