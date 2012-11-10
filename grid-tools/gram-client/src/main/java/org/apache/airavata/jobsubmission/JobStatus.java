package org.apache.airavata.jobsubmission;

import org.apache.airavata.jobsubmission.context.ApplicationContext;
import org.globus.gram.GramJob;

public class JobStatus {

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ApplicationContext context = new ApplicationContext();
			context.login();
			
			String rsl = "";
			GramJob job = new GramJob(context.getGssCredential(),rsl);
			job.setID("https://gatekeeper.ranger.tacc.teragrid.org:50388/16073791261988702796/9536223017013632385/");
			JobListener listener = new JobListener(job, context.getGssCredential());
			job.addListener(listener);
			System.out.println("Status 1111111" + job.getStatusAsString());
		    listener.waitFor();
			System.out.println("Now Status" + job.getStatusAsString());
			job.removeListener(listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
