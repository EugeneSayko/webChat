package com.eugene.webchatapp.models;

/**
 * Created by eugene on 18.05.16.
 */
public class User extends Entity {

    private String name;
    private String password;

    public User(){

    }

    public User(String id, String name, String password) {
        super(id);
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
