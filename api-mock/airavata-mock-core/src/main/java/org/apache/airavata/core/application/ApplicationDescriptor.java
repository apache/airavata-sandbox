package org.apache.airavata.core.application;

import java.util.ArrayList;
import java.util.List;

public abstract class ApplicationDescriptor {
	private String applicationName;
	private List<ApplicationParameter> inputs;
	private List<ApplicationParameter> outputs;
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public List<ApplicationParameter> getInputs() {
		if (inputs==null){
			inputs=new ArrayList<ApplicationParameter>();
		}
		return inputs;
	}
	public void setInputs(List<ApplicationParameter> inputs) {
		this.inputs = inputs;
	}
	public List<ApplicationParameter> getOutputs() {
		if (outputs==null){
			outputs=new ArrayList<ApplicationParameter>();
		}
		return outputs;
	}
	public void setOutputs(List<ApplicationParameter> outputs) {
		this.outputs = outputs;
	}
}
