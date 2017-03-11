package com.example.fujinohiroki.homey;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Date;

import io.realm.Realm;

public class SignupActivity extends AppCompatActivity {
    //インテント
    Intent intent;
    EditText username;
    EditText password;
    EditText email;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //インテントを取得
        intent = getIntent();
        //入力された値を取得
        Resources res = getResources();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
    }
    //登録ボタンをクリック
    //各欄に何も入力されていないとき

    public void onClickButton(View v) {
        if (username.length() == 0) {
            username.setError(getResources().getString(R.id.username));
        } else if (password.length() == 0) {
            password.setError(getResources().getString(R.id.password));
        } else if (email.length() == 0) {
            email.setError(getResources().getString(R.id.email));
        } else {
            username.setError(null);
            password.setError(null);
            email.setError(null);
        }
    }
    /**
     * ユーザー情報を登録する。
     *
     */
    private void registerUserInfo(){
        Date date = new Date();
        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setName(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        realm.commitTransaction();

    }

//    private void saveBotMessage(boolean isBot) {
//        Date date = new Date();
//        realm.beginTransaction();
//        Number maxId = realm.where(ChatMessage.class).max("id");
//        long nextId = 1;
//        if (maxId != null) nextId = maxId.longValue() + 1;
//        ChatMessage chat = realm.createObject(ChatMessage.class, nextId);
//        // 各要素の永続化
//        chat.setDate(date);
//        if (isBot) {
//            // botが返すメッセージを取得する
//            botMessage = getMessageByBot();
//            chat.setMessage(botMessage);
//        } else {
//            chat.setMessage(userMessage.getText().toString());
//        }
//        chat.setBot(isBot);
//        realm.commitTransaction();
//    }
}