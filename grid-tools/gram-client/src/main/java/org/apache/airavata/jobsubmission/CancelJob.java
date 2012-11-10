package org.apache.airavata.jobsubmission;

import org.apache.airavata.jobsubmission.context.ApplicationContext;
import org.apache.airavata.jobsubmission.gram.ExectionContext;
import org.apache.log4j.Logger;
import org.globus.gram.GramJob;
import org.ietf.jgss.GSSCredential;

public class CancelJob {
	private static final Logger log = Logger.getLogger(CancelJob.class);
	public void cancelJob(GSSCredential gssCred, ExectionContext appExecContext, String jobUrl) throws Exception{
		log.setLevel(org.apache.log4j.Level.INFO);
		String rsl = "";
		GramJob job = new GramJob(rsl);
		job.setID(jobUrl);
		job.setCredentials(gssCred);
		job.cancel();
		
	}
public static void main(String[] args) {
	try {
		ApplicationContext context = new ApplicationContext();
		context.login();
		CancelJob job = new CancelJob();
		String jobURL = "https://lslogin2.lonestar.tacc.utexas.edu:50385/16073824805448638521/14062883951572193460/";
		ExectionContext contextExectionContext = new ExectionContext();
		job.cancelJob(context.getGssCredential(), contextExectionContext, jobURL);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
