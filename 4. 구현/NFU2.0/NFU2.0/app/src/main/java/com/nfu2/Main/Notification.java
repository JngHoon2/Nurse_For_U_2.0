package com.nfu2.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nfu2.Login.Login;
import com.nfu2.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Notification extends AppCompatActivity {

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private androidx.appcompat.widget.Toolbar notifi_toolbar;
    private TextView txt_txt_notifi_cnt;
    private ListView listView;
    private Context context;

    private FloatingActionButton btn_add;

    public String title, writer, date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_notification);


        notifi_toolbar = findViewById(R.id.notifi_toolbar);
        txt_txt_notifi_cnt = findViewById(R.id.txt_notifi_cnt);
        listView = findViewById(R.id.listView);
        btn_add = findViewById(R.id.fab);

        // 툴바 설정 관련 코드 아래 4줄
        setSupportActionBar(notifi_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.before);

        // 공지사항 갯수 출력
        mDatabase.child("Notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cnt = Long.toString(snapshot.getChildrenCount());
                txt_txt_notifi_cnt.setText(cnt);
                Log.e("DataBase", "cnt: " + cnt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 공지사항 리스트뷰 출력
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
                adapter.flag = false;
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // 추가 버튼 클릭 메소드
        btn_add.setOnClickListener( v -> {
            Intent goAddNotification = new Intent(Notification.this, AddNotification.class);
            startActivity(goAddNotification);
        });
    }

    // 툴바의 뒤로가기 버튼 관련 메소드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
