package org.apache.airavata.sample.bes;

import de.fzj.unicore.uas.security.ClientProperties;
import eu.unicore.security.util.client.IClientProperties;

public abstract class AbstractJobCommand {
	
	
	
	public static final String factoryUrl = "https://zam1161v01.zam.kfa-juelich.de:8002/INTEROP1/services/BESFactory?res=default_bes_factory";
	public static final String dateJsdlPath = "src/test/resources/date.xml";
	
	protected IClientProperties securityProperties;
	
	
	public AbstractJobCommand() {
		securityProperties = initSecurityProperties();
	}
	
	protected ClientProperties initSecurityProperties() {

		ClientProperties sp = new ClientProperties();

		sp.setSslEnabled(true);
		sp.setSignMessage(true);

		sp.setKeystore("src/test/resources/demo-keystore.jks");
		sp.setKeystorePassword("654321");
		sp.setKeystoreAlias("demouser-new");
		sp.setKeystoreType("JKS");

		return sp;

	}
	
}
