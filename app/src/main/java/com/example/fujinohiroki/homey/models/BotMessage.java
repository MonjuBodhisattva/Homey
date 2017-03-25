package com.example.fujinohiroki.homey.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by fujinohiroki on 2017/03/24.
 */

public class BotMessage extends RealmObject {
    @PrimaryKey
    private long id;
    private ChatMessage chatMessage;
    private String message;
    private Date date;
    private Boolean like;

    public void setChatMessage(ChatMessage chatMessage) { this.chatMessage = chatMessage; }

    public void setMessage(String message) { this.message = message; }

    public void setDate(Date date) { this.date = date; }

    public void setLike(Boolean like) { this.like = like; }
}
