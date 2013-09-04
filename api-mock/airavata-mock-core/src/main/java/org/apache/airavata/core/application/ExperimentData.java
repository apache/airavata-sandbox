package org.apache.airavata.core.application;

import java.util.Date;

public class ExperimentData {
	private String experimentId;
	private String experimentTemplateId;
	private Date submissionDate;
	private String data;
	private String inputData;
	public ExperimentData() {
	}
	
	public ExperimentData(String experimentId, String experimentTemplateId,
			Date submissionDate, String data, String inputData) {
		this.experimentId = experimentId;
		this.experimentTemplateId = experimentTemplateId;
		this.submissionDate = submissionDate;
		this.data = data;
		this.inputData = inputData;
	}

	public String getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}
	public String getExperimentTemplateId() {
		return experimentTemplateId;
	}
	public void setExperimentTemplateId(String experimentTemplateId) {
		this.experimentTemplateId = experimentTemplateId;
	}
	public Date getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}
}
