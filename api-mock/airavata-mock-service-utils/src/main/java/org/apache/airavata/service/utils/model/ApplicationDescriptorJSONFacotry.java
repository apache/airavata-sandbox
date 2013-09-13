package org.apache.airavata.service.utils.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.airavata.core.application.ApplicationParameter;
import org.apache.airavata.core.application.GramApplicationDescriptor;
import org.apache.airavata.core.application.LocalApplicationDescriptor;
import org.apache.airavata.core.application.ParameterType;
import org.apache.airavata.service.utils.json.ConversionUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public class ApplicationDescriptorJSONFacotry implements JSONObjectFactory {
	private static ApplicationDescriptorJSONFacotry defaultInstance;
	private static List<Class<?>> applicationClasses = new ArrayList<Class<?>>();
	
	static{
		applicationClasses.add(LocalApplicationDescriptor.class);
		applicationClasses.add(GramApplicationDescriptor.class);
	}
	
	private ApplicationDescriptorJSONFacotry() {
	}
	
	public static ApplicationDescriptorJSONFacotry getInstance(){
		if (defaultInstance==null){
			defaultInstance=new ApplicationDescriptorJSONFacotry();
		}
		return defaultInstance;
	}
	
	@Override
	public List<Class<?>> getTypes() {
		return applicationClasses;
	}

	@Override
	public String getJSONTypeTemplate(Class<?> cl) throws JsonGenerationException, JsonMappingException, IOException {
		String result=null;
		if (cl==LocalApplicationDescriptor.class){
			LocalApplicationDescriptor app = new LocalApplicationDescriptor();
			app.setApplicationName("{application.name}");
			app.getInputs().add(new ApplicationParameter("{input.parameter.name}","{input.parameter.value}",ParameterType.STRING));
			app.getOutputs().add(new ApplicationParameter("{output.parameter.name}","{output.parameter.value}",ParameterType.STRING));
			app.setExecutablePath("{application.executable.location}");
			app.setScratchLocation("{scratch.directory.location}");
			String jsonString = ConversionUtils.getJSONString(app); 
			jsonString=jsonString.replaceAll("STRING", "{parameter.type}");
			result=jsonString;
		} else if (cl==GramApplicationDescriptor.class){
			GramApplicationDescriptor app = new GramApplicationDescriptor();
			app.setApplicationName("{application.name}");
			app.getInputs().add(new ApplicationParameter("{input.parameter.name}","{input.parameter.value}",ParameterType.STRING));
			app.getOutputs().add(new ApplicationParameter("{output.parameter.name}","{output.parameter.value}",ParameterType.STRING));
			app.setExecutablePath("{application.executable.location}");
			app.setScratchLocation("{scratch.directory.location}");
			app.setGramHost("{gram.host.ip.location}");
			app.setGridFTPEndpoint("{grid.ftp.url}");
			String jsonString = ConversionUtils.getJSONString(app); 
			jsonString=jsonString.replaceAll("STRING", "{parameter.type}");
			result=jsonString;
		}
		return result;
	}
	@Override
	public String getTypeName(Class<?> cl) {
		return cl.getSimpleName();
	}
	
	@Override
	public String getTypeDescription(Class<?> cl) {
		String result=null;
		if (cl==LocalApplicationDescriptor.class){
			result="Defines computational resource residing in the host which Airavata server is running";
		}else if (cl==GramApplicationDescriptor.class){
			result="Defines computational resource residing in a GRAM host";
		}
		return result;
	}

}
