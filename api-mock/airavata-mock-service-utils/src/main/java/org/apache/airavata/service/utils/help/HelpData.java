package org.apache.airavata.service.utils.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpData {
	private String title;
	private String description;
	private List<String> syntax;
	private Map<String,String> parameters;
	private List<String> examples;
	private List<String> notes;
	public HelpData(String tile, String description) {
		setTitle(tile);
		setDescription(description);
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getSyntax() {
		if (syntax==null){
			syntax=new ArrayList<String>();
		}
		return syntax;
	}
	public void setSyntax(List<String> syntax) {
		this.syntax = syntax;
	}
	public Map<String, String> getParameters() {
		if (parameters==null){
			parameters=new HashMap<String, String>();
		}
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public List<String> getExamples() {
		if (examples==null){
			examples=new ArrayList<String>();
		}
		return examples;
	}
	public void setExamples(List<String> examples) {
		this.examples = examples;
	}

	public List<String> getNotes() {
		if (notes==null){
			notes=new ArrayList<String>();
		}
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}
}
