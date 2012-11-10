package org.apache.airavata.jobsubmission;

import org.globus.gram.GramException;
import org.globus.gram.GramJob;
import org.globus.gram.GramJobListener;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;

public class JobListener implements GramJobListener {

	private boolean finished;
	private int error;
	private int status;
	private GSSCredential credential;
	private GramJob job;
	
	public JobListener(GramJob job, GSSCredential credential) {
		this.job = job;
		this.credential = credential;
	}
	public void waitFor() throws InterruptedException, GSSException, GramException {
		while (!finished) {
			
		// job status is changed but method isn't invoked
		if (status != 0) {
			if (job.getStatus() != status) {
				System.out.println("invoke method manually");
				statusChanged(job);
				}
			}
		 else {
			statusChanged(job);
			 System.out.println("Status is zero");
		}
		synchronized (this) {
			wait(1000l);
		}		
		}
	}

	public void statusChanged(GramJob job) {
		int jobStatus = job.getStatus();
		String jobId = job.getIDAsString();
		String statusString = job.getStatusAsString();
		System.out.println("Status " + statusString);
		if (jobStatus == GramJob.STATUS_DONE) {
			finished = true;
		} else if (jobStatus == GramJob.STATUS_FAILED) {
			finished = true;
			error = job.getError();
			System.out.println("Job Error Code: " + error);
		}
	}

}