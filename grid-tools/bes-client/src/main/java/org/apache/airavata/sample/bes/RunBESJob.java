package org.apache.airavata.sample.bes;

import java.io.File;
import org.ggf.schemas.jsdl.x2005.x11.jsdl.JobDefinitionDocument;
import de.fzj.unicore.uas.security.ClientProperties;
import eu.unicore.security.util.client.IClientProperties;

public class RunBESJob {

	protected IClientProperties securityProperties;

	static final String factoryUrl = "https://zam1161v01.zam.kfa-juelich.de:8002/INTEROP1/services/BESFactory?res=default_bes_factory";

	static final String jsdlPath = "src/test/resources/date.xml";

	public RunBESJob() {
		securityProperties = initSecurityProperties();
	}

	public void runJob() {

		JobDefinitionDocument jobDoc = null;
		try {
			jobDoc = JobDefinitionDocument.Factory.parse(new File(jsdlPath));
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

	protected ClientProperties initSecurityProperties() {

		ClientProperties sp = new ClientProperties();

		sp.setSslEnabled(true);
		sp.setSignMessage(true);

		sp.setKeystore("src/test/resources/demo-keystore.jks");
		sp.setKeystorePassword("654321");
		sp.setKeystoreAlias("demouser-new");
		sp.setKeystoreType("JKS");

//		sp.setTruststore("src/test/resources/demo-keystore.jks");
//		sp.setTruststorePassword("654321");
//		sp.setKeystoreType("JKS");

		return sp;

	}

	
}
