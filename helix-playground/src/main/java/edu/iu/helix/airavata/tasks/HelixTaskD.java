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
public class HelixTaskD  extends UserContentStore implements Task {

    private static Logger logger = LogManager.getLogger(HelixTaskD.class);
    public static final String TASK_COMMAND = "HelixTask_D";

    public HelixTaskD(TaskCallbackContext callbackContext) {
        System.out.println("HelixTaskD | callbackContext: " + callbackContext);
    }

    @Override
    public TaskResult run() {
        System.out.println("HelixTaskD | Inside run(), sleeping for 2 secs");
//        sleep(2000);
        System.out.println("HelixTaskD | Retrieved from UserStore : " + getFromUserStore("fullName"));
        System.out.println("HelixTaskD | Returning status : COMPLETED.");
        return new TaskResult(TaskResult.Status.COMPLETED, "HelixTaskD completed!");
    }

    @Override
    public void cancel() {
        System.out.println("HelixTaskD | Inside cancel()");
    }

    private String getFromUserStore(String key) {
        System.out.println("HelixTaskD | Inside getFromUserStore()");
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
