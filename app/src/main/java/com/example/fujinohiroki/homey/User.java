package com.example.fujinohiroki.homey;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by yanagawa on 2017/03/03.
 * 以下では、次のUserテーブルを用意しています。
 * {フィールド名,データ型}
 * {name,文字列型}
 * {id,long型}
 * {password,文字列型}
 * {email,文字列型}
 */

public class User extends RealmObject{
    //idが一意であることを保証するため。これにより、idを指定すれば、更新や削除ができます。
    @PrimaryKey
    private long            id;
    //name,password,emailは、それぞれ「ユーザー名」「パスワード」「メールアドレス」を保存するために用意しました。
    private String          name;
    private String          password;
    private String          email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
