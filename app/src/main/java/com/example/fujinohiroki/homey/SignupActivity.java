package com.example.fujinohiroki.homey;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fujinohiroki.homey.models.Migration;
import com.example.fujinohiroki.homey.models.User;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.R.attr.id;

public class SignupActivity extends AppCompatActivity {
    //インテント
    Intent intent;
    EditText userName;
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

        // Realmインスタンスの初期化
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().schemaVersion(3).migration(new Migration()).build();
        realm = Realm.getInstance(realmConfig);

        //入力された値を取得
        Resources res = getResources();
        userName = (EditText) findViewById(R.id.username);
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
        if (userName.length() == 0) {
            userName.setError("ユーザー名が入力されていません");
            focusView = userName;
        } else if (email.length() == 0) {
            email.setError("メールアドレスが入力されていません。");
            focusView = email;
        } else if (!isEmailValid(email.getText().toString())) {
            email.setError("入力されたメールアドレスは適切ではありません。");
            focusView = email;
        } else if (password.length() == 0) {
            password.setError("パスワードが入力されていません。");
            focusView = password;
        } else if (!isPasswordValid(password.getText().toString())) {
            password.setError("パスワードは8文字以上15文字以下にしてください。");
            focusView = password;
        } else {
            userName.setError(null);
            password.setError(null);
            email.setError(null);
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else {
            //認証
            if (certificationUserInfo()) {
                //登録用関数
                registerUserInfo();
                realm.close();
                //遷移
                Intent moveIntent = new Intent(this, MainActivity.class);
                startActivity(moveIntent);
                //}
            }
        }
    }

    /*
    *ユーザー認証
    */
    public boolean certificationUserInfo() {
        Long cnt = realm.where(User.class).equalTo("email", email.getText().toString()).equalTo("name", userName.getText().toString()).count();
        if (cnt >= 1) {
            Toast.makeText(SignupActivity.this, "入力された情報は既に登録されています。", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
        user.setName(userName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        realm.commitTransaction();
        realm.close();
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

