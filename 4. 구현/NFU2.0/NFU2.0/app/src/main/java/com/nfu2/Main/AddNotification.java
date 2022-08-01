package com.nfu2.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nfu2.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNotification extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private androidx.appcompat.widget.Toolbar addNotifi_toolbar;
    private EditText txt_title;
    private EditText txt_content;
    private Button btn_write;
    private String pkey;
    private String writer = "테스트작성자";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_addnoti);

        addNotifi_toolbar = findViewById(R.id.addNotifi_toolbar);
        txt_title = findViewById(R.id.txt_title);
        txt_content = findViewById(R.id.txt_content);
        btn_write = findViewById(R.id.btn_write);

        // 툴바 설정 관련 코드
        setSupportActionBar(addNotifi_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.before);

        //pkey(공지번호) 설정
        mDatabase.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int cnt = (int)snapshot.getChildrenCount();
                pkey = Integer.toString(cnt + 1);
                Log.e("DataBase", "pkey: " + pkey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        // 작성 완료 버튼 메소드
        btn_write.setOnClickListener(v -> {
            if (txt_title.getText().length() == 0 || txt_content.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "누락된 정보가 있습니다.", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("작성 완료");
                builder.setMessage("작성 중이던 내용을 반영하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                long now = System.currentTimeMillis();

                                String setTitle = txt_title.getText().toString();
                                String setContent = txt_content.getText().toString();
                                String setDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(now);

                                //데이터베이스에 반영하는 코드
                                reflect_notification(pkey, setTitle, setContent, setDate, writer);
                                Toast.makeText(getApplicationContext(), "저장되었습니다.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                builder.create().show();
            }
        });
    }

    public void reflect_notification(String pkey, String title, String content, String date, String writer){
        Notification_Adapter noti = new Notification_Adapter(title, content, date, writer);
        mDatabase.child("Notification").child(pkey).setValue(noti);
    }

    // 툴바의 뒤로가기 버튼 관련 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                deleteCheck();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 하드웨어 뒤로가기 버튼 관련 메소드
    @Override
    public void onBackPressed() {
        deleteCheck();
    }

    // 내용삭제 알림 메소드
    public void deleteCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("작성 취소");
        builder.setMessage("작성 중이던 내용은 저장되지 않습니다.\n취소하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#D3D3D3"));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#98DFFF"));
            }
        });

        dialog.show();
    }
}
