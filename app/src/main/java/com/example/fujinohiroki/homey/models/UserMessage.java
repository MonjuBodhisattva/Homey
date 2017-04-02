package com.example.fujinohiroki.homey.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fujinohiroki on 2017/02/11.
 */

public class UserMessage extends RealmObject {
    @PrimaryKey
    private long id;
    private String message;
    private Date date;
    private User user;

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() { return date; }

    public void setId(long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) { this.user = user; }
}
