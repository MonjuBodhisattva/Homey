package com.example.fujinohiroki.homey;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupActionBar();
        //インテントを取得
        intent = getIntent();
        //入力された値を取得
        Resources res = getResources();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);

        Button mSignUpButton = (Button) findViewById(R.id.register_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });
    }
    //登録ボタンをクリック
    //各欄に何も入力されていないとき

    public void onClickButton(View v) {
        View focusView = null;

        if (username.length() == 0) {
            username.setError("ユーザー名が入力されていません");
            focusView = username;
        } else if (email.length() == 0) {
            email.setError("メールアドレスが入力されていません。");
            focusView = email;
        } else if (!isEmailValid(email.getText().toString())) {
            email.setError("入力されたメールアドレスは適切ではありません。");
            focusView = email;
        } else if (password.length() == 0) {
            password.setError("パスワードが入力されていません。");
            focusView = password;
        } else if (!isPasswordValid(password.getText().toString())){
            password.setError("パスワードは8文字以上15文字以下にしてください。");
            focusView = password;
        } else {
            username.setError(null);
            password.setError(null);
            email.setError(null);
        }
        focusView.requestFocus();
    }

    /**
     * ユーザー情報を登録する。
     */
    private void registerUserInfo() {
        Date date = new Date();
        realm.beginTransaction();
        User user = realm.createObject(User.class);
        user.setName(username.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        realm.commitTransaction();

    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        System.out.println("isPasswordValid");
        boolean flag = false;

        if (password.length() >= 8 && password.length() <= 15){
            flag = true;
        }

        return flag;
    }

}//To do list
//
//    パスワードはハッシュ化する？

