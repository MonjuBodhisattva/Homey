package com.example.fujinohiroki.homey;

import java.util.Date;

/**
 * Created by fujinohiroki on 2017/04/02.
 */

public class ChatMessage {
    String message;
    boolean isBot;
    Date date;

    public void setMessage(String message) { this.message = message; }

    public void setIsBot(boolean isBot) { this.isBot = isBot; }

    public void setDate(Date date) { this.date = date; }

    public String getMessage() { return message; }

    public boolean getIsBot() { return isBot; }

    public Date getDate() { return date; }

}
