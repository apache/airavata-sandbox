package edu.iu.helix.play;

import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.helix.task.UserContentStore;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by goshenoy on 6/19/17.
 */
public class HelixTaskA extends UserContentStore implements Task {

    private static Logger logger = LogManager.getLogger(HelixTaskA.class);
    public static final String TASK_COMMAND = "HelixTask-A";

    HelixTaskA(TaskCallbackContext callbackContext) {
        logger.info("edu.iu.helix.play.HelixTaskA | callbackContext: " + callbackContext);
    }

    @Override
    public TaskResult run() {
        logger.info("edu.iu.helix.play.HelixTaskA | Inside run(), sleeping for 5 secs");
        addToUserStore();
        sleep(5000);
        logger.info("edu.iu.helix.play.HelixTaskA | Returning status : COMPLETED.");
        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTask completed!");
    }

    @Override
    public void cancel() {
        logger.info("edu.iu.helix.play.HelixTaskA | Inside cancel()");
    }

    private void addToUserStore() {
        logger.info("edu.iu.helix.play.HelixTaskA | Inside addToUserStore()");
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
