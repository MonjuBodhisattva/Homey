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

}
