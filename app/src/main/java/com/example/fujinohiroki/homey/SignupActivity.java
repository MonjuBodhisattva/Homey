package com.example.fujinohiroki.homey;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {
    //インテント
    Intent intent;
    EditText username;
    EditText password;
    EditText email;

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
        }else if{(password.length() == 0) {
            password.setError(getResources().getString(R.id.password));
        }else if{(email.length() == 0){
            email.setError(getResources().getString(R.id.email));
        }
        else{
                setError(null);
            }
        }
            username.setError(null);
        }
    }

}
