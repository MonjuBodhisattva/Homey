package com.example.fujinohiroki.homey;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ListView chatListView;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "onCreate()");

        chatListView = (ListView) findViewById(R.id.listView);

        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfig);

        editText = (EditText) findViewById(R.id.edit_text);

        RealmResults<ChatMessage> chatMessages =
                realm.where(ChatMessage.class).findAll();
        ChatMessageAdapter adapter =
                new ChatMessageAdapter(this, chatMessages);
        chatListView.setAdapter(adapter);
    }

    public void onSaveTapped(View view) {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(ChatMessage.class).max("id");
        long nextId = 1;
        if(maxId != null) nextId = maxId.longValue() + 1;
        ChatMessage chat = realm.createObject(ChatMessage.class, nextId);
        //chat.setId(nextId);
        chat.setDate(date);
        chat.setMessage(editText.getText().toString());
        chat.setBot(false);
        realm.commitTransaction();

        Toast.makeText(this, "メッセージを送信しました", Toast.LENGTH_SHORT).show();
        editText.getEditableText().clear();
    }
}