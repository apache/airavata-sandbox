package org.apache.airavata.services.registry.rest;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/29/12
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class User {

    private String userName;
    private String password;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
