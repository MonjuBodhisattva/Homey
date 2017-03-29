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
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.R.attr.id;

public class SignupActivity extends AppCompatActivity {
    //インテント
    Intent intent;
    EditText username;
    EditText password;
    EditText email;
    Realm realm;
    Button button2;

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

        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().schemaVersion(0).migration(new Migration()).build();
        realm = Realm.getInstance(realmConfig);

        //入力された値を取得
        Resources res = getResources();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        button2 = (Button) findViewById(R.id.button2);

        Button mSignUpButton = (Button) findViewById(R.id.register_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickButton(view);
            }
        });

        //ボタン２をクリックしてRealmを確認する
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Number userid;
                userid = realm.where(User.class).max("id");
                RealmResults<User> usernamer = realm.where(User.class).findAll();
                System.out.println(userid);
                System.out.println(usernamer);
            }
        });
    }
    /**
     * ユーザー認証
     */
    public void userAuthentication() {

        //RealmQuery<User> emailRegistered =
        Long cnt = realm.where(User.class).contains("email", email.getText().toString()).contains("password",password.getText().toString();
        if(cnt >=)1}


    //登録ボタンをクリック
    //各欄に何も入力されていないとき

    public void onClickButton(View v) {
        View focusView = null;
        boolean flag = true;
        if (username.length() == 0) {
            username.setError("ユーザー名が入力されていません");
            focusView = username;
            flag = false;
        } else if (email.length() == 0) {
            email.setError("メールアドレスが入力されていません。");
            focusView = email;
            flag = false;
        } else if (!isEmailValid(email.getText().toString())) {
            email.setError("入力されたメールアドレスは適切ではありません。");
            focusView = email;
            flag = false;
        } else if (password.length() == 0) {
            password.setError("パスワードが入力されていません。");
            focusView = password;
            flag = false;
        } else if (!isPasswordValid(password.getText().toString())) {
            password.setError("パスワードは8文字以上15文字以下にしてください。");
            focusView = password;
            flag = false;
        } else {
            username.setError(null);
            password.setError(null);
            email.setError(null);
        }

        if (!(focusView == null)) {
            focusView.requestFocus();
        }
        registerUserInfo();
        realm.close();
        if (flag == true) {
            Intent moveIntent = new Intent(this, MainActivity.class);
            startActivity(moveIntent);
        }

    }


    /**
     * ユーザー情報を登録する。
     */
    private void registerUserInfo() {
        Date date = new Date();
        realm.beginTransaction();
        Number maxId = realm.where(User.class).max("id");
        long nextId = 1;
        if (maxId != null) nextId = maxId.longValue() + 1;
        User user = realm.createObject(User.class, nextId);
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

    /**
     * ＠を含んでいない場合はエラーメッセージを表示
     *
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * パスワードは8文字以上15文字以下で入力されなければエラーメッセージを表示
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        boolean flag = false;

        if (password.length() >= 8 && password.length() <= 15) {
            flag = true;
        }
        return flag;
    }

}

