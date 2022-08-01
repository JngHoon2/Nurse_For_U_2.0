package com.nfu2.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.nfu2.R;

import java.util.Scanner;

public class Access extends Activity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_access);

        checkUserData();
    }


    public void checkUserData(){

        // 로그인 화면으로 이동합니다.
        Intent goLogin =  new Intent(Access.this, Login.class);
        Access.this.startActivity(goLogin);
        finish();

        // 메인 화면으로 이동합니다.
    }
}
