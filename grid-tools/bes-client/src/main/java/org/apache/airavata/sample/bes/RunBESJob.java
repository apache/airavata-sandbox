package org.apache.airavata.sample.bes;

import java.io.File;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;

public class RunBESJob extends AbstractJobCommand{


	public RunBESJob() {
		super();
	}

	public void runJob() {

		JobDefinitionDocument jobDoc = null;
		try {
			jobDoc = JobDefinitionDocument.Factory.parse(new File(dateJsdlPath));
		} catch (Exception e) {
			System.err.println("Error parsing JSDL instance. " + e);
		}

		BESJob job = new BESJob();
		job.setFactory(factoryUrl);
		job.setJobDoc(jobDoc);

		CreateActivityTask th = new CreateActivityTask(job, securityProperties);
		try {
			th.startJob();
		} catch (Exception e) {

			System.err.println("Couldn't run job: " + e);
		}

	}
	
}
