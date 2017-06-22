package edu.iu.helix.play;

import org.apache.helix.task.Task;
import org.apache.helix.task.TaskCallbackContext;
import org.apache.helix.task.TaskResult;
import org.apache.helix.task.UserContentStore;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Created by goshenoy on 6/20/17.
 */
public class HelixTaskB extends UserContentStore implements Task {

    private static Logger logger = LogManager.getLogger(HelixTaskA.class);
    public static final String TASK_COMMAND = "HelixTask-B";

    HelixTaskB(TaskCallbackContext callbackContext) {
        logger.info("edu.iu.helix.play.HelixTaskB | callbackContext: " + callbackContext);
    }

    @Override
    public TaskResult run() {
        logger.info("edu.iu.helix.play.HelixTaskB | Inside run(), sleeping for 5 secs");
        long expiry = System.currentTimeMillis() + 3000L;
        while (System.currentTimeMillis() < expiry) {
            logger.info("edu.iu.helix.play.HelixTaskB | Inside run(), *** Waiting ***");
//            sleep(50);
        }
        logger.info("edu.iu.helix.play.HelixTaskB | Retrieved from UserStore : " + getFromUserStore("fullName"));
        logger.info("edu.iu.helix.play.HelixTaskB | Returning status : COMPLETED.");
        return new TaskResult(TaskResult.Status.COMPLETED, "edu.iu.helix.play.HelixTaskB completed!");
    }

    @Override
    public void cancel() {
        logger.info("edu.iu.helix.play.HelixTaskB | Inside cancel()");
    }

    private String getFromUserStore(String key) {
        logger.info("edu.iu.helix.play.HelixTaskB | Inside getFromUserStore()");
        return getUserContent(key, Scope.WORKFLOW);
    }

    private static void sleep(long d) {
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
