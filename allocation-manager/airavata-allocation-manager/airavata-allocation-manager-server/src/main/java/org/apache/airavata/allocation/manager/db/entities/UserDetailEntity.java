package org.apache.airavata.allocation.manager.db.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the USER_DETAILS database table.
 * 
 */
@Entity
@Table(name="USER_DETAILS")
@NamedQuery(name="UserDetailEntity.findAll", query="SELECT u FROM UserDetailEntity u")
public class UserDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USERNAME")
	private String username;

	@Lob
	@Column(name="EMAIL")
	private String email;

	@Lob
	@Column(name="FULL_NAME")
	private String fullName;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="USER_TYPE")
	private String userType;

	public UserDetailEntity() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}