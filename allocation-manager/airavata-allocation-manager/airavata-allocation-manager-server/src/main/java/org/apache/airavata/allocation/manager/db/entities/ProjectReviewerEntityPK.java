/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author harsha
 */
/**
 * The primary key class for the PROJECT_REVIEWER database table.
 * 
 */
@Embeddable
public class ProjectReviewerEntityPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PROJECT_ID")
	private String projectId;

	@Column(name="REVIEWER")
	private String reviewer;

	public ProjectReviewerEntityPK() {
	}
	public String getProjectId() {
		return this.projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getReviewer() {
		return this.reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProjectReviewerEntityPK)) {
			return false;
		}
		ProjectReviewerEntityPK castOther = (ProjectReviewerEntityPK)other;
		return 
			this.projectId.equals(castOther.projectId)
			&& this.reviewer.equals(castOther.reviewer);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.projectId.hashCode();
		hash = hash * prime + this.reviewer.hashCode();
		
		return hash;
	}
}