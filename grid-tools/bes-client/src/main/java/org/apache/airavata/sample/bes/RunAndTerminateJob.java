package org.apache.airavata.sample.bes;

import java.io.File;

import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;

public class RunAndTerminateJob extends AbstractJobCommand {

	public RunAndTerminateJob() {
		super();
	}

	public void runAndTerminate(){
		JobDefinitionDocument jobDoc = null;
		try {
			jobDoc = JobDefinitionDocument.Factory.parse(new File(dateJsdlPath));
		} catch (Exception e) {
			System.err.println("Error parsing JSDL instance. " + e);
		}

		BESJob job = new BESJob();
		job.setFactory(factoryUrl);
		job.setJobDoc(jobDoc);

		CreateAndTerminateActivityTask th = new CreateAndTerminateActivityTask(job, securityProperties);
		try {
			th.startJob();
		} catch (Exception e) {

			System.err.println("Couldn't run job: " + e);
		}
	}
	

}
