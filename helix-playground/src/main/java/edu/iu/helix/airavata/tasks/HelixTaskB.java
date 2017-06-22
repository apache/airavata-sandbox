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
public class HelixTaskB  extends UserContentStore implements Task {

    private static Logger logger = LogManager.getLogger(HelixTaskB.class);
    public static final String TASK_COMMAND = "HelixTask_B";

    private static int retryCount = 0;
    public HelixTaskB(TaskCallbackContext callbackContext) {
        System.out.println("HelixTaskB | callbackContext: " + callbackContext);
    }

    @Override
    public TaskResult run() {
        if (retryCount < 2) {
            retryCount++;
            System.out.println("HelixTaskB | Returning status FAILED, Helix will retry this task. Retry count: " + retryCount);
            return new TaskResult(TaskResult.Status.FAILED, "HelixTaskB should be retried!");
        }

        System.out.println("HelixTaskB | After 2 retries, Inside run(), sleeping for 2 secs");
//        sleep(2000);
        System.out.println("HelixTaskB | Retrieved from UserStore : " + getFromUserStore("fullName"));

//        System.out.println("HelixTaskB | Returning status : FATAL_FAILED.");
//        return new TaskResult(TaskResult.Status.FATAL_FAILED, "edu.iu.helix.play.HelixTaskB completed!");

        System.out.println("HelixTaskB | Returning status : COMPLETED.");
        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTaskB completed!");
    }

    @Override
    public void cancel() {
        System.out.println("HelixTaskB | Inside cancel()");
    }

    private String getFromUserStore(String key) {
        System.out.println("HelixTaskB | Inside getFromUserStore()");
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
