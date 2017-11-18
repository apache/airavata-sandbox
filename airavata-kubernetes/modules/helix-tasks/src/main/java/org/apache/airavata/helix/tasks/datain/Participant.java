package org.apache.airavata.helix.tasks.datain;

import org.apache.airavata.helix.HelixParticipant;
import org.apache.airavata.helix.tasks.command.CommandTask;
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
                       String taskTypeName,
                       String apiServerUrl) {
        super(zkAddress, clusterName, participantName, taskTypeName, apiServerUrl);
    }

    @Override
    public Map<String, TaskFactory> getTaskFactory() {
        Map<String, TaskFactory> taskRegistry = new HashMap<String, TaskFactory>();

        TaskFactory dataInTask = new TaskFactory() {
            @Override
            public Task createNewTask(TaskCallbackContext context) {
                return new DataInputTask(context);
            }
        };

        taskRegistry.put(DataInputTask.NAME, dataInTask);

        return taskRegistry;
    }

    @Override
    public TaskTypeResource getTaskType() {
        return DataInputTask.getTaskType();
    }

    public static void main(String args[]) {
        HelixParticipant participant = new Participant(
                "localhost:2199",
                "AiravataDemoCluster",
                "data-in-p1", DataInputTask.NAME,
                "localhost:8080");
        new Thread(participant).start();
    }
}
