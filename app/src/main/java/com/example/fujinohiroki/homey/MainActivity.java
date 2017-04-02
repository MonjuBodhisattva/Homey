package com.example.fujinohiroki.homey;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fujinohiroki.homey.adapters.ChatMessageAdapter;
import com.example.fujinohiroki.homey.models.BotMessage;
import com.example.fujinohiroki.homey.models.Migration;
import com.example.fujinohiroki.homey.models.User;
import com.example.fujinohiroki.homey.models.UserMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
    Long loginUserId;
    ChatMessageAdapter adapter;
    ArrayList<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "onCreate()");

        // chatのlistView取得
        chatListView = (ListView) findViewById(R.id.chatView);
        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().schemaVersion(3).migration(new Migration()).build();
        realm = Realm.getInstance(realmConfig);

        userMessage = (EditText) findViewById(R.id.user_message); // 送信メッセージの取得

        // ログインしているユーザーのID取得
        loginUserId = getUserId();

        // メッセージを全て取得してListView上に出力する
        //final RealmResults<ChatMessage> chatMessages =
                //realm.where(ChatMessage.class).findAll();
        chatMessages = getChatMessage(loginUserId);
        adapter = new ChatMessageAdapter(this, chatListView.getId(), chatMessages);
        chatListView.setAdapter(adapter);
        itemCount = chatListView.getCount();
        // listViewを一番下にする
        chatListView.setItemChecked(itemCount - 1, true);
        chatListView.setSelection(itemCount - 1);
        realm.close();
    }

    private ArrayList<ChatMessage> getChatMessage(long userId) {
        final RealmResults<BotMessage> botMessages =
                realm.where(BotMessage.class).findAll();
        ArrayList<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
        for(final BotMessage botMessage : botMessages) {
            UserMessage userMessage = botMessage.getUserMessage();
            if(userMessage != null) {
                ChatMessage chatMessageByUser = new ChatMessage();
                chatMessageByUser.setMessage(userMessage.getMessage());
                chatMessageByUser.setDate(userMessage.getDate());
                chatMessageByUser.setIsBot(false);
                chatMessageList.add(chatMessageByUser);
            }
            ChatMessage chatMessageByBot = new ChatMessage();
            chatMessageByBot.setMessage(botMessage.getMessage());
            chatMessageByBot.setDate(botMessage.getDate());
            chatMessageByBot.setIsBot(true);
            chatMessageList.add(chatMessageByBot);
        }
        return chatMessageList;
    }

    // SharedPreferencesからログインユーザーのIDを取得する
    private Long getUserId() {
        SharedPreferences prefs = getSharedPreferences("Data", Context.MODE_PRIVATE);
        loginUserId = prefs.getLong("loginUserId", 0);
        return loginUserId;
    }

    private User getUser(long userId) {
        User user = realm.where(User.class)
                .equalTo("id", userId).findFirst();
        return user;
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
        final UserMessage user = saveUserMessage();
        Toast.makeText(this, "メッセージを送信しました", Toast.LENGTH_SHORT).show();
        userMessage.getEditableText().clear(); // 入力文字を削除
        chatListView.smoothScrollToPosition(chatListView.getCount() - 1);
        // botから送信されるメッセージを永続化する
        //saveBotMessage();
        getBotMessage(user);
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
     */
    private UserMessage saveUserMessage() {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(UserMessage.class).max("id");
        User loginUser = getUser(loginUserId);
        long nextId = 1;
        if (maxId != null) nextId = maxId.longValue() + 1;
        UserMessage user = realm.createObject(UserMessage.class, nextId);
        // 各要素の永続化
        user.setDate(date);
        user.setUser(loginUser);
        user.setMessage(userMessage.getText().toString());
        realm.commitTransaction();
        System.out.println(user);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(user.getMessage());
        chatMessage.setIsBot(false);
        chatMessage.setDate(user.getDate());
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        return user;
    }

    private void saveBotMessage(UserMessage userMessage, String botMessage) {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(BotMessage.class).max("id");
        long nextId = 1;
        if(maxId != null) nextId = maxId.longValue() + 1;
        BotMessage bot = realm.createObject(BotMessage.class, nextId);
        bot.setUserMessage(userMessage);
        bot.setDate(date);
        bot.setMessage(botMessage);
        bot.setLike(false);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(bot.getMessage());
        chatMessage.setIsBot(true);
        chatMessage.setDate(bot.getDate());
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        realm.commitTransaction();
    }

    /**
     * キーボードを非表示にする
     */
    private void closeKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getBotMessage(final UserMessage userMessage) {

        final String requestUrl = "http://52.86.121.7";

        RequestQueue getQueue = Volley.newRequestQueue(this);

        /*StringRequest sRequest = new StringRequest(Request.Method.POST, requestUrl,

                // 通信成功
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //
                    }
                },

                // 通信失敗
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "通信に失敗しました。", Toast.LENGTH_SHORT).show();
                    }
                }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("message", userMessage.getText().toString());
                    return params;
                }
        };*/
        StringRequest sRequest = new StringRequest(Request.Method.GET, requestUrl,

                // 通信成功
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // ここにレスポンスが入る
                        String botMessage = response;
                        saveBotMessage(userMessage, botMessage);
                    }
                },

                // 通信失敗
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "通信に失敗しました。", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        getQueue.add(sRequest);
    }

}