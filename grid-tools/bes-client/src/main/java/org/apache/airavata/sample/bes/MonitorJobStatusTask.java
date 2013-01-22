package org.apache.airavata.sample.bes;

import org.w3.x2005.x08.addressing.EndpointReferenceType;

import de.fzj.unicore.wsrflite.xmlbeans.WSUtilities;

import eu.unicore.security.util.client.IClientProperties;

public class MonitorJobStatusTask {
	
	private String factoryUrl;
	private EndpointReferenceType jobEPR;
	private IClientProperties sec;
	
	public MonitorJobStatusTask(String factoryUrl, IClientProperties sec, EndpointReferenceType jobEPR) {
		this.factoryUrl = factoryUrl;
		this.jobEPR = jobEPR;
		this.sec = sec; 
	}
}