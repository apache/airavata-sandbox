package org.apache.airavata.jobsubmission.gram;

import org.apache.log4j.Logger;
import org.globus.gram.GramJob;
import org.globus.gram.GramJobListener;

public class JobSubmissionListener implements GramJobListener {

	private static final String DELIMITER = "#";
	private boolean finished;
	private int error;
	private int status;
	private StringBuffer buffer;

	private GramJob job;

	private static final Logger log = Logger.getLogger(JobSubmissionListener.class);

	public JobSubmissionListener(GramJob job, StringBuffer buffer) {
		this.buffer = buffer;
		this.job = job;
	}

	// waits for DONE or FAILED status
	public void waitFor() throws InterruptedException {
		while (!finished) {
			
			// job status is changed but method isn't invoked
			if (status != 0) {
				if (job.getStatus() != status) {
					log.info("invoke method manually");
					statusChanged(job);
				} else {
					log.info("job " + job.getIDAsString() + " have same status: " + GramJob.getStatusAsString(status));
				}
			} else {
				log.info("Status is zero");
			}
			
			synchronized (this) {
				wait(60 * 1000l);
			}			
		}
	}

	public synchronized void statusChanged(GramJob job) {
		log.debug("Listener: statusChanged triggered");
		int jobStatus = job.getStatus();
		String jobId = job.getIDAsString();
		String statusString = job.getStatusAsString();
		log.info("Job Status: " + statusString + "(" + jobStatus + ")");
		buffer.append(formatJobStatus(jobId, statusString));
		log.debug(formatJobStatus(jobId, statusString));

		status = jobStatus;
		if (jobStatus == GramJob.STATUS_DONE) {
			finished = true;
		} else if (jobStatus == GramJob.STATUS_FAILED) {
			finished = true;
			error = job.getError();
			log.info("Job Error Code: " + error);
			buffer.append(DELIMITER + error);
		}
		buffer.append("\n");

		// notify wait thread to wake up if done		
		if (finished) {
			notify();
		}
	}

	public static String formatJobStatus(String jobid, String jobstatus) {
		return System.currentTimeMillis() + DELIMITER + jobid + DELIMITER + jobstatus;
	}

	public int getError() {
		return error;
	}

	public int getStatus() {
		return status;
	}

	public void wakeup() {
		try {
			notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
