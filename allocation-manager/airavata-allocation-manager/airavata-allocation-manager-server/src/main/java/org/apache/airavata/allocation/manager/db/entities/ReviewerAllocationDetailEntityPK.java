package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the REVIEWER_ALLOCATION_DETAILS database table.
 * 
 */
@Embeddable
public class ReviewerAllocationDetailEntityPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="PROJECT_ID")
	private String projectId;

	@Column(name="USERNAME")
	private String username;

	public ReviewerAllocationDetailEntityPK() {
	}
	public String getProjectId() {
		return this.projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReviewerAllocationDetailEntityPK)) {
			return false;
		}
		ReviewerAllocationDetailEntityPK castOther = (ReviewerAllocationDetailEntityPK)other;
		return 
			this.projectId.equals(castOther.projectId)
			&& this.username.equals(castOther.username);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.projectId.hashCode();
		hash = hash * prime + this.username.hashCode();
		
		return hash;
	}
}