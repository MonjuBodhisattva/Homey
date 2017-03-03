package com.example.fujinohiroki.homey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class signup extends AppCompatActivity {
    String userName;
    String passWord;
    String eMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //入力された値を取得
        Resources res = getResources();
        userName = res.getString(R.string.userName);
        passWord = res.getString(R.string.passWord);
        eMail = res.getString(R.string.eMail);
    }

    //登録ボタンがクリックされたとき
    public void onClickButton(View view);
}
}
