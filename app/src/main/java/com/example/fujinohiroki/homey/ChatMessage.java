package com.example.fujinohiroki.homey;

import java.util.Date;

/**
 * Created by fujinohiroki on 2017/04/02.
 */

public class ChatMessage {
    long chatId;
    String message;
    boolean isBot;
    Date date;
    boolean like;

    ChatMessage() {}
    ChatMessage(long chatId, String message, boolean isBot, Date date, boolean like) {
        this.chatId = chatId;
        this.message = message;
        this.isBot = isBot;
        this.date = date;
        this.like = like;
    }

    public void setMessage(String message) { this.message = message; }

    public void setIsBot(boolean isBot) { this.isBot = isBot; }

    public void setDate(Date date) { this.date = date; }

    public void setLike(boolean like) { this.like = like; }

    public void setChatId(long chatId) { this.chatId = chatId; }

    public String getMessage() { return message; }

    public boolean getIsBot() { return isBot; }

    public Date getDate() { return date; }

    public boolean getLike() { return like; }

    public long getChatId() { return chatId; }

}
