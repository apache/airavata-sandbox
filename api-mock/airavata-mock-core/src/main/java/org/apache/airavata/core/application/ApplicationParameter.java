package org.apache.airavata.core.application;

public class ApplicationParameter {
	private String name;
	private String value;
	private ParameterType type;
	
	public ApplicationParameter() {
	}
	
	public ApplicationParameter(String name, String value, ParameterType type) {
		setName(name);
		setValue(value);
		setType(type);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value=value;
	}
	
	public ParameterType getType(){
		return type;
	}
	
	public void setType(ParameterType type){
		this.type=type;
	}
}
