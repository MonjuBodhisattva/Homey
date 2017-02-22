package com.example.fujinohiroki.homey;

import io.realm.RealmObject;

/**
 * Created by yanagawa on 2017/02/22.
 */

public class user extends RealmObject {

    @PrimaryKey
    private long id;
    private String name;
    private long pass;
    private String email;
}
