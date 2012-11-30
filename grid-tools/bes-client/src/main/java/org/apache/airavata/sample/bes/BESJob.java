package org.apache.airavata.sample.bes;


import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;

public class BESJob {
	
	private String factoryUrl;
	
	private JobDefinitionDocument jobDoc;
		
	
	public BESJob() {
		
	}


	public String getFactoryUrl() {
		return factoryUrl;
	}

	public void setFactory(String factoryUrl) {
		this.factoryUrl = factoryUrl;
	}

	public JobDefinitionDocument getJobDoc() {
		return jobDoc;
	}

	public void setJobDoc(JobDefinitionDocument jobDoc) {
		this.jobDoc = jobDoc;
	}
	
}
