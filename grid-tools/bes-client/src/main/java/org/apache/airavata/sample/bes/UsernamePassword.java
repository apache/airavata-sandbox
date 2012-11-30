package org.apache.airavata.sample.bes;
import java.io.Serializable;



/**
 * holds username and password for some types of data staging, 
 * as found in the job description
 * 
 * @author schuller
 */
public class UsernamePassword implements Serializable {

	private static final long serialVersionUID=1L;
	
	private final String user;
	
	private final String password;
	
	public UsernamePassword(String user, String password){
		this.user=user;
		this.password=password;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String toString(){
		return "UsernamePassword["+user+" "+password+"]";
	}
}
