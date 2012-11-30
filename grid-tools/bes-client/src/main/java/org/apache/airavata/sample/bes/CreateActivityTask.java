package org.apache.airavata.sample.bes;

import java.util.Calendar;

import org.ggf.schemas.bes.x2006.x08.besFactory.ActivityStateEnumeration;
import org.ggf.schemas.bes.x2006.x08.besFactory.CreateActivityDocument;
import org.ggf.schemas.bes.x2006.x08.besFactory.CreateActivityResponseDocument;
import org.w3.x2005.x08.addressing.EndpointReferenceType;

import de.fzj.unicore.bes.client.FactoryClient;
import de.fzj.unicore.wsrflite.xmlbeans.WSUtilities;
import eu.unicore.security.util.client.IClientProperties;

public class CreateActivityTask {

	private BESJob job;

	private IClientProperties sec;
	private String jobId;
	
	
	public CreateActivityTask(BESJob job, IClientProperties sec) {
		this.job = job;
		this.sec = sec;
		
	}

	public void startJob() throws Exception {
		String factoryUrl = job.getFactoryUrl();
		EndpointReferenceType eprt = EndpointReferenceType.Factory
				.newInstance();
		eprt.addNewAddress().setStringValue(factoryUrl);
		System.out.println("========================================");
		System.out.println(String.format("Job Submitted to %s.\n", factoryUrl));
		FactoryClient factory = new FactoryClient(eprt, sec);
		CreateActivityDocument cad = CreateActivityDocument.Factory
				.newInstance();
		cad.addNewCreateActivity().addNewActivityDocument()
				.setJobDefinition(job.getJobDoc().getJobDefinition());
		CreateActivityResponseDocument response = factory.createActivity(cad);
		EndpointReferenceType activityEpr = response
				.getCreateActivityResponse().getActivityIdentifier();
//		factory.waitWhileActivityIsDone(activityEpr, 1000);
		jobId = WSUtilities.extractResourceID(activityEpr);
		if (jobId == null) {
			jobId = new Long(Calendar.getInstance().getTimeInMillis())
					.toString();
		}
		ActivityStateEnumeration.Enum state = factory.getActivityStatus(activityEpr);
		
		String status;
		
		status = String.format("Job %s is %s.\n", activityEpr.getAddress()
				.getStringValue(), factory.getActivityStatus(activityEpr)
				.toString()).toString();
		
		
		while ((factory.getActivityStatus(activityEpr) != ActivityStateEnumeration.FINISHED) && 
				(factory.getActivityStatus(activityEpr) != ActivityStateEnumeration.FAILED)){
			status = String.format("Job %s is %s.\n", activityEpr.getAddress()
					.getStringValue(), factory.getActivityStatus(activityEpr)
					.toString()).toString();
			Thread.sleep(1500);
			continue;
		}
		
		status = String.format("Job %s is %s.\n", activityEpr.getAddress()
				.getStringValue(), factory.getActivityStatus(activityEpr)
				.toString()).toString();
		
		System.out.println(status);
		
		if (!(factory.getActivityStatus(activityEpr) == ActivityStateEnumeration.FINISHED)) {
			throw new Exception("Job "+activityEpr.getAddress().getStringValue()+" "+state.toString());
		}
	}



}
