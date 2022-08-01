package com.nfu2.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nfu2.Injection.FIRSTPAGE;
import com.nfu2.Login.UserInfo;
import com.nfu2.R;

import java.util.ArrayList;
import java.util.Collections;

public class Main extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private androidx.appcompat.widget.Toolbar toolbar;
    private TextView txt_name;
    private TextView txt_position;
    private TextView txt_userCode;
    private TextView txt_team;
    private ImageView img_QR;
    private Button btnTakover;
    private Button btnEMR;
    private Button btnInjection;
    private Button btnScheduler;
    private Button btnNotification;
    private Context context;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_main);

        toolbar = findViewById(R.id.notifi_toolbar);
        txt_name = findViewById(R.id.txt_name);
        txt_position = findViewById(R.id.txt_position);
        txt_userCode = findViewById(R.id.txt_userCode);
        txt_team = findViewById(R.id.txt_team);
        img_QR = findViewById(R.id.img_QR);
        btnTakover = findViewById(R.id.btn_goTakeover);
        btnEMR = findViewById(R.id.btn_goEMR);
        btnInjection = findViewById(R.id.btn_goInjection);
        btnScheduler = findViewById(R.id.btn_goScheduler);
        btnNotification = findViewById(R.id.btn_goNotification);
        listView = findViewById(R.id.mainListview);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent dintent = getIntent();
        Bundle bundle = dintent.getExtras();
        String code = bundle.getString("userCode");
        Log.e("", code);

        mDatabase.child("User").child(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                Log.e("name", userInfo.getName().toString());
                Log.e("position", userInfo.getPosition().toString());
                Log.e("team", userInfo.getTeam().toString());

                txt_name.setText(userInfo.getName().toString());
                txt_position.setText(userInfo.getPosition().toString());
                txt_userCode.setText(code);
                txt_team.setText(userInfo.getTeam().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // 공지 사항 미리보기 출력
        context = this;
        ArrayList<Notification_Adapter> listViewData = new ArrayList<>();
        mDatabase.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listViewData.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Notification_Adapter item = new Notification_Adapter();
                    item.setTitle(dataSnapshot.child("title").getValue().toString());
                    item.setWriter(dataSnapshot.child("writer").getValue().toString());
                    item.setDate(dataSnapshot.child("date").getValue().toString());

                    listViewData.add(item);
                }
                Collections.reverse(listViewData);
                Listview_Adapter adapter = new Listview_Adapter(listViewData, context);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 공지사항 버튼 클릭
        btnNotification.setOnClickListener(v -> {

        });

        // 인수인계 버튼 클릭
        btnTakover.setOnClickListener( v ->{
            Intent goInjection = new Intent(Main.this, FIRSTPAGE.class);
            startActivity(goInjection);
        });

        // EMR 버튼 클릭
        btnEMR.setOnClickListener( v ->{

        });

        // 투약관리 버튼 클릭
        btnInjection.setOnClickListener( v ->{
            Intent goInjection = new Intent(Main.this, FIRSTPAGE.class);
            goInjection.putExtra("patientkey", code);
            startActivity(goInjection);
        });

        // 스케줄러 버튼 클릭
        btnScheduler.setOnClickListener( v ->{

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }
}
