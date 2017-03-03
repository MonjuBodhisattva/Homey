package com.example.fujinohiroki.homey;

import io.realm.RealmObject;

/**
 * Created by yanagawa on 2017/03/03.
 */

public class User extends RealmObject{
    private String          name;
    private long            id;
    private String          password;
    private String          email;


    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
