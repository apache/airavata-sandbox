package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;


/**
 * The persistent class for the REQUEST_STATUS database table.
 * 
 */
@Entity
@Table(name="REQUEST_STATUS")
@NamedQuery(name="RequestStatusEntity.findAll", query="SELECT r FROM RequestStatusEntity r")
public class RequestStatusEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PROJECT_ID")
	private String projectId;

	@Column(name="END_DATE")
	private BigInteger endDate;

	@Lob
	@Column(name="REVIEWERS")
	private String reviewers;

	@Column(name="START_DATE")
	private BigInteger startDate;

	@Column(name="STATUS")
	private String status;

	public RequestStatusEntity() {
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public BigInteger getEndDate() {
		return this.endDate;
	}

	public void setEndDate(BigInteger endDate) {
		this.endDate = endDate;
	}

	public String getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(String reviewers) {
		this.reviewers = reviewers;
	}

	public BigInteger getStartDate() {
		return this.startDate;
	}

	public void setStartDate(BigInteger startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}