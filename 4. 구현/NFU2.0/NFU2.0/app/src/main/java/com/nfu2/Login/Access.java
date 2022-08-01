package com.nfu2.Login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.nfu2.LocalDB.LocalDB;
import com.nfu2.LocalDB.LoginDB;
import com.nfu2.Main.Main;
import com.nfu2.R;

import java.util.List;
import java.util.concurrent.Executor;

public class Access extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Button btn_goLogin;
    private String userCode;

    private boolean flag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_access);

        btn_goLogin = findViewById(R.id.btn_loginPage);
        btn_goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goLogin =  new Intent(Access.this, Login.class);
                Access.this.startActivity(goLogin);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserData();
    }

    private void checkUserData(){
        if(flag){
            authentication();
            biometricPrompt.authenticate(promptInfo);
        }
        else{
            // 로그인 화면으로 이동합니다.
            Intent goLogin =  new Intent(Access.this, Login.class);
            Access.this.startActivity(goLogin);
            finish();
        }
    }

    private void authentication(){
        final LoginDB db = Room.databaseBuilder(this, LoginDB.class, "Login-info").allowMainThreadQueries().build();
        List<LocalDB> localDBList = db.localDao().getAll();
        for(LocalDB userProfile : localDBList) {
            userCode = userProfile.getUser_id();
            String a = userProfile.getUser_id() + "\n flag:" + userProfile.isBioFlag();
            Log.e("test", a);
        }

        executor = ContextCompat.getMainExecutor(getApplicationContext());

        biometricPrompt = new BiometricPrompt(Access.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                btn_goLogin.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "사용자정보 확인", Toast.LENGTH_SHORT).show();

                // 메인화면으로 이동합니다.
                Intent goMain =  new Intent(Access.this, Main.class);
                goMain.putExtra("userCode",userCode);
                Access.this.startActivity(goMain);
                finish();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("생체 인증")
                .setSubtitle("인증해주세요.")
                .setNegativeButtonText("취소")
                .build();
    }
}

