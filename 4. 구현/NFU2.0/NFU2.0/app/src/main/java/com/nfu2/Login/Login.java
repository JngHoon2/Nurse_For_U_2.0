package com.nfu2.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nfu2.LocalDB.LocalDB;
import com.nfu2.LocalDB.LoginDB;
import com.nfu2.Main.Main;
import com.nfu2.R;

import java.util.Iterator;

public class Login extends Activity {

    //fireBase 관련 변수
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef = database.getReference();

    // UI 컴포넌트 관련 변수
    private EditText txtId;
    private EditText txtPwd;
    private Button btnLogin;
    private CheckBox chbBioLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);

        final LoginDB db = Room.databaseBuilder(this, LoginDB.class, "Login-info").allowMainThreadQueries().build();

        txtId = findViewById(R.id.user_id);
        txtPwd = findViewById(R.id.user_pwd);
        btnLogin = findViewById(R.id.btn_loginPage);
        chbBioLogin = findViewById(R.id.chb_bioLogin);

        // 로그인 버튼 클릭 시
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String id = txtId.getText().toString().trim();
                        String pwd = txtPwd.getText().toString().trim();
                        Object user_id;
                        int cnt = 0;

                        // 루트 노드를 정의합니다.
                        Iterator<DataSnapshot> rootNode = dataSnapshot.getChildren().iterator();

                        // 루트 노드로 부터 하위 노드를 하나 씩 비교합니다.
                        while(rootNode.hasNext()){
                            DataSnapshot node = rootNode.next();
                            user_id = node.getKey();

                            // ID가 일치하는 경우
                            if(id.equals(user_id)) {
                                //ID의 하위 노드에 존재하는 PWD를 확인합니다.
                                if(pwd.equals(node.child("pwd").getValue())){
                                    bioLogin();
                                    String userCode = node.getKey().toString();
                                    Intent intent = new Intent(Login.this, Main.class);
                                    intent.putExtra("userCode",userCode);
                                    Login.this.startActivity(intent);
                                    finish();
                                    cnt += 1;
                                    break;
                                }
                            }
                        }
                        // 일치하는 ID가 존재하지 않는 경우
                        if(cnt!=1){
                            Toast.makeText(getApplicationContext(),"계정을 다시 확인하세요.",Toast.LENGTH_SHORT).show();////여기부터 내일다시해
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            // 생체 인식 로그인 설정 메소드
            public void bioLogin(){
                chbBioLogin = findViewById(R.id.chb_bioLogin);
                // 생체 인식 체크 박스가 체크되어 있는 경우
                if(chbBioLogin.isChecked()){
                    // 로그인 정보를 로컬데이터 베이스에 저장합니다.
                    db.localDao().insert(new LocalDB(chbBioLogin.isChecked(), txtId.getText().toString(), txtPwd.getText().toString()));
                }
            }
        });
    }
}
