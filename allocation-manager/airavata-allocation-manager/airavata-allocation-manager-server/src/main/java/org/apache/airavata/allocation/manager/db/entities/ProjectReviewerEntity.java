package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author harsha
 */
/**
 * The persistent class for the PROJECT_REVIEWER database table.
 * 
 */
@Entity
@Table(name="PROJECT_REVIEWER")
@NamedQuery(name="ProjectReviewerEntity.findAll", query="SELECT p FROM ProjectReviewerEntity p")
public class ProjectReviewerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProjectReviewerEntityPK id;

	public ProjectReviewerEntity() {
	}

	public ProjectReviewerEntityPK getId() {
		return this.id;
	}

	public void setId(ProjectReviewerEntityPK id) {
		this.id = id;
	}

}