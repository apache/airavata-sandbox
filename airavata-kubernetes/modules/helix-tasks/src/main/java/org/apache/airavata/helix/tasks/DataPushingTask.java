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
public class DataPushingTask extends UserContentStore implements Task {

    public static final String NAME = "DATA_PUSHING";

    public DataPushingTask(TaskCallbackContext callbackContext) {
    }

    public TaskResult run() {
        System.out.println("Executing data pushing");
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Continuing");
        String key2 = getUserContent("Key2", Scope.WORKFLOW);

        System.out.println(key2);
        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTaskB completed!");

    }

    public void cancel() {

    }
}
