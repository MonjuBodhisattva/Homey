package com.example.fujinohiroki.homey;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    Long loginUserId;
    ChatMessageAdapter adapter;
    ArrayList<ChatMessage> chatMessages;
    Button sendMessageButton;
    TextView changeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "onCreate()");

        // chatのlistView取得
        chatListView = (ListView) findViewById(R.id.chatView);
        sendMessageButton = (Button) findViewById(R.id.button);
        changeChat = (TextView) findViewById(R.id.change_chat);
        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().schemaVersion(3).migration(new Migration()).build();
        realm = Realm.getInstance(realmConfig);

        userMessage = (EditText) findViewById(R.id.user_message); // 送信メッセージの取得

        // ログインしているユーザーのID取得
        loginUserId = getUserId();

        getSupportActionBar().hide();
        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFA500'>Homey</font>"));

        // メッセージを全て取得してListView上に出力する
        //final RealmResults<ChatMessage> chatMessages =
        //realm.where(ChatMessage.class).findAll();
        chatMessages = getChatMessage(loginUserId, true);
        adapter = new ChatMessageAdapter(this, chatListView.getId(), chatMessages);
        chatListView.setAdapter(adapter);
        itemCount = chatListView.getCount();
        // listViewを一番下にする
        chatListView.setItemChecked(itemCount - 1, true);
        chatListView.setSelection(itemCount - 1);
        realm.close();
    }

    private ArrayList<ChatMessage> getChatMessage(long userId, boolean isAll) {
        RealmResults<BotMessage> botMessages;
        if(isAll) {
            botMessages =
                    realm.where(BotMessage.class).equalTo("userMessage.user.id", userId).findAll();
        } else {
            botMessages =
                    realm.where(BotMessage.class).equalTo("userMessage.user.id", userId).equalTo("like", true).findAll();
        }
        //final RealmResults<BotMessage> botMessages =
        //                realm.where(BotMessage.class).findAll();
        ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
        for (final BotMessage botMessage : botMessages) {
            UserMessage userMessage = botMessage.getUserMessage();
            if (userMessage != null) {
                ChatMessage chatMessageByUser = new ChatMessage(userMessage.getId(), userMessage.getMessage(), false, userMessage.getDate(), false);
                System.out.println(chatMessageByUser);
                chatMessageList.add(chatMessageByUser);
            }
            ChatMessage chatMessageByBot = new ChatMessage(botMessage.getId(), botMessage.getMessage(), true, botMessage.getDate(), botMessage.getLike());
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
        if (userMessage.getText().toString().length() == 0) {
            userMessage.setError("メッセージを入力してください");
            userMessage.requestFocus();
        } else {
            // ボタン無効化
            sendMessageButton.setEnabled(false);
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
            // ボタン有効化
            sendMessageButton.setEnabled(true);
        }
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
        ChatMessage chatMessage = new ChatMessage(nextId, user.getMessage(), false, user.getDate(), false);
        chatMessages.add(chatMessage);
        adapter.notifyDataSetChanged();
        return user;
    }

    private void saveBotMessage(UserMessage userMessage, String botMessage) {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(BotMessage.class).max("id");
        long nextId = 1;
        if (maxId != null) nextId = maxId.longValue() + 1;
        BotMessage bot = realm.createObject(BotMessage.class, nextId);
        bot.setUserMessage(userMessage);
        bot.setDate(date);
        bot.setMessage(botMessage);
        bot.setLike(false);
        ChatMessage chatMessage = new ChatMessage(nextId, botMessage, true, bot.getDate(), false);
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

    /**
     * APIにリクエストを送る
     *
     * @param userMessage
     */
    private void getBotMessage(final UserMessage userMessage) {

        final String requestUrl = "http://52.86.121.7/dialog/";

        RequestQueue getQueue = Volley.newRequestQueue(this);

        StringRequest sRequest = new StringRequest(Request.Method.GET, requestUrl + userMessage.getMessage(),

                // 通信成功
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // レスポンス文字列をjsonオブジェクトに変換して解析してゆく
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("ResultSet");
                            String botMessage = result.getString("response");
                            saveBotMessage(userMessage, botMessage);
                        } catch (JSONException e) {
                            // error
                        }
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

    public void onClickLike(View view) {
        long chatId = (long) view.getTag();
        /*View tempView = (View) view.getTag(2);
        ImageButton tempImageButton = (ImageButton) tempView.findViewById(R.id.likeButton);*/
        ImageButton tempImageButton = (ImageButton) view.findViewById(R.id.likeButton);
        System.out.println(chatId);
        realm.beginTransaction();
        BotMessage botMessage = realm.where(BotMessage.class).equalTo("id", chatId).findFirst();
        botMessage.setLike(!botMessage.getLike());
        realm.commitTransaction();
        System.out.println(botMessage.getLike());
        tempImageButton.setImageResource(botMessage.getLike()? R.drawable.heart : R.drawable.empty_heart);
        adapter.notifyDataSetChanged();
    }

    public void onChangeChat(View view) {
        String state = changeChat.getText().toString();
        if(state == "All") {
            chatMessages = getChatMessage(loginUserId, true);
            changeChat.setText("Like");
        } else {
            chatMessages = getChatMessage(loginUserId, false);
            changeChat.setText("All");
        }
        adapter = new ChatMessageAdapter(this, chatListView.getId(), chatMessages);
        chatListView.setAdapter(adapter);
        itemCount = chatListView.getCount();
        // listViewを一番下にする
        chatListView.setItemChecked(itemCount - 1, true);
        chatListView.setSelection(itemCount - 1);
        adapter.notifyDataSetChanged();
    }
}