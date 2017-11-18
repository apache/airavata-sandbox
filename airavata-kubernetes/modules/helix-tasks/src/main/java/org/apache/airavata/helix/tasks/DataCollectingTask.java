package org.apache.airavata.helix.tasks;

import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.helix.task.UserContentStore;

/**
 * TODO: Class level comments please
 *
 * @author dimuthu
 * @since 1.0.0-SNAPSHOT
 */
public class DataCollectingTask extends UserContentStore implements Task {

    public static final String NAME = "DATA_COLLECTING";

    public DataCollectingTask(TaskCallbackContext callbackContext) {
    }

    public TaskResult run() {
        System.out.println("Executing data collecting");
        putUserContent("Key", "Hooo", Scope.WORKFLOW);

        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTaskB completed!");

    }

    public void cancel() {

    }
}
