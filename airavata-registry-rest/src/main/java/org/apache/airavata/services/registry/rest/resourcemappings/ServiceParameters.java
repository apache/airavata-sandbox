package org.apache.airavata.services.registry.rest.resourcemappings;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ServiceParameters {

	String type;
	String name;
	String dataType;
	String description;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
