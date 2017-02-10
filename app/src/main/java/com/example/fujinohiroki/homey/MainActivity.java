package com.example.fujinohiroki.homey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);
        editText = (EditText) findViewById(R.id.edit_text);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // エディットテキストのテキストを取得
                String text = editText.getText().toString();
                // 入力した文字列の削除
                editText.getEditableText().clear();
                if (i == 0) {
                    // 取得したテキストを TextView に張り付ける
                    textView.setText(text);
                    textView2.setText("素敵ね");
                } else if (i == 1) {
                    textView3.setText(text);
                    textView4.setText("そら助かるわ");
                } else if (i == 2) {
                    textView5.setText(text);
                    textView6.setText("いいね！グッジョブ！");
                } else {
                }
                i++;
            }
        });
    }
}