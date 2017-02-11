package com.example.fujinohiroki.homey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    int i = 0;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("", "onCreate()");

        // Realmインスタンスの初期化
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        Realm realm = Realm.getInstance(realmConfig);

        editText = (EditText) findViewById(R.id.edit_text);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String text = editText.getText().toString();
                // 入力した文字列の削除
                editText.getEditableText().clear();
            }
        });
    }
}