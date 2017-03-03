package com.example.fujinohiroki.homey;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    EditText userMessage;
    ListView chatListView;
    Realm realm;
    String botMessage;
    int itemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "onCreate()");

        // chatのlistView取得
        chatListView = (ListView) findViewById(R.id.chatView);
        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfig);

        userMessage = (EditText) findViewById(R.id.user_message); // 送信メッセージの取得

        // メッセージを全て取得してListView上に出力する
        final RealmResults<ChatMessage> chatMessages =
                realm.where(ChatMessage.class).findAll();
        ChatMessageAdapter adapter =
                new ChatMessageAdapter(this, chatMessages);
        chatListView.setAdapter(adapter);
        itemCount = chatListView.getCount();
        // listViewを一番下にする
        chatListView.setItemChecked(itemCount - 1, true);
        chatListView.setSelection(itemCount - 1);
    }

    /**
     * メッセージを送信する
     * Realmオブジェクトを通してデータを永続化する
     *
     * @param view
     */
    public void onSendMessage(View view) {
        // キーボードを非表示にする
        closeKeyBoard(view);
        // ユーザのメッセージ永続化
        saveBotMessage(false);
        Toast.makeText(this, "メッセージを送信しました", Toast.LENGTH_SHORT).show();
        userMessage.getEditableText().clear(); // 入力文字を削除
        chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
        // botから送信されるメッセージを永続化する
        saveBotMessage(true);
        // listViewを下までスクロール
        chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
    }

    /**
     * ボットが返すメッセージ今はランダムに生成を行う
     *
     * @return
     */
    private String getMessageByBot() {
        String[] botMessages = {"素敵ね", "そら助かるわ", "いいね！グッジョブ！"};
        Random rnd = new Random();
        int ran = rnd.nextInt(3);
        return botMessages[ran];
    }

    /**
     * メッセージを保存する
     * isBotがtrueの時はbotの入力を永続化、 falseの時はユーザの入力を永続化
     */
    private void saveBotMessage(boolean isBot) {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(ChatMessage.class).max("id");
        long nextId = 1;
        if (maxId != null) nextId = maxId.longValue() + 1;
        ChatMessage chat = realm.createObject(ChatMessage.class, nextId);
        // 各要素の永続化
        chat.setDate(date);
        if (isBot) {
            // botが返すメッセージを取得する
            botMessage = getMessageByBot();
            chat.setMessage(botMessage);
        } else {
            chat.setMessage(userMessage.getText().toString());
        }
        chat.setBot(isBot);
        realm.commitTransaction();
    }

    /**
     * キーボードを非表示にする
     */
    private void closeKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}