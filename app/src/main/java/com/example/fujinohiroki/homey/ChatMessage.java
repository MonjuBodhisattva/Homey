package com.example.fujinohiroki.homey;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fujinohiroki on 2017/02/11.
 */

public class ChatMessage extends RealmObject {
    @PrimaryKey
    private long id;
    private String message;
    private Date date;
    private boolean isBot;

    public String getMessage() {
        return message;
    }

    public boolean getIsBot() {
        return isBot;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBot(boolean isBot) {
        this.isBot = isBot;
    }
}
