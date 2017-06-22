package edu.iu.helix.airavata.tasks;

import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.helix.task.UserContentStore;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by goshenoy on 6/21/17.
 */
public class HelixTaskA  extends UserContentStore implements Task {

    private static Logger logger = LogManager.getLogger(HelixTaskA.class);
    public static final String TASK_COMMAND = "HelixTask_A";
    private static int count = 0;

    public HelixTaskA(TaskCallbackContext callbackContext) {
        System.out.println("HelixTaskA | callbackContext: " + callbackContext);
    }

    @Override
    public TaskResult run() {
        System.out.println("HelixTaskA | Inside run(), sleeping for 2 secs");
        addToUserStore();
//        sleep(2000);
        System.out.println("HelixTaskA | Returning status : COMPLETED.");
        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTask completed!");
    }

    @Override
    public void cancel() {
        System.out.println("HelixTaskA | Inside cancel()");
    }

    private void addToUserStore() {
        System.out.println("HelixTaskA | Inside addToUserStore()");
        putUserContent("fullName", "Gourav Shenoy", Scope.WORKFLOW);
    }

    private static void sleep(long d) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
