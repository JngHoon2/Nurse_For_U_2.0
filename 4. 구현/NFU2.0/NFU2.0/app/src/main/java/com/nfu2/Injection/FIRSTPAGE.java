package com.nfu2.Injection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nfu2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class FIRSTPAGE extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<INJECTION> arrayList= new ArrayList<>();;

    private Button btnDO, btnLIST;
    private Button scanQRBtn, btnpatientlist;
    private TextView txtcode1, txtcode2, txtname;
    private CheckBox cboxend;

    int a=0;
    private IntentIntegrator qrScan;
    private String patientkey;
    private String Drugname;

    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMdd");
    private String ntime = simpleDateFormat.format(mDate)+"0000";
    private long nowtime = Long.parseLong(ntime);
    private int position=0;
    private int count=0;

    private INJECTION injectionlist[] = new INJECTION[30];

    private CustomAdapterIJ adapterIJ = new CustomAdapterIJ(arrayList, this);

    public static Context context_FIRSTPAGE;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = dateFormat.format(date); return getTime;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        context_FIRSTPAGE=this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnDO = (Button) findViewById(R.id.btnDO);
        btnLIST = (Button) findViewById(R.id.btnLIST);
        scanQRBtn = (Button) findViewById(R.id.scanQR);
        btnpatientlist = (Button) findViewById(R.id.btnpatientlist);

        txtcode1 = (TextView) findViewById(R.id.txtcode1);
        txtcode2 = (TextView) findViewById(R.id.txtcode2);
        txtname = (TextView) findViewById(R.id.txtname);

        cboxend = (CheckBox) findViewById(R.id.cboxend);

        qrScan = new IntentIntegrator(this);

        cboxend.setOnCheckedChangeListener(this::onCheckedChanged);

        //database
        recyclerView.setAdapter(adapterIJ);
        adapterIJ.setItemClickListener(new OnPersonItemClickListener() {
            @Override
            public void OnItemClick(CustomAdapterIJ.CustomViewHolderIJ holder, View view, int position) {
                INJECTION item = adapterIJ.getItem(position);
                Intent intent =new Intent(FIRSTPAGE.this, InjectionCheck.class);
                intent.putExtra("patientkey", patientkey);
                intent.putExtra("injectionkey", item.getInjectionkey());
                startActivity(intent);
            }
        });
        scanQRBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });
        btnpatientlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FIRSTPAGE.this, RECORD.class);
                startActivity(intent);
            }
        });



        adapterIJ.notifyDataSetChanged();
    }
    private void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        arrayList.clear();
        if(compoundButton.isChecked()){
            for(int i=0; i<injectionlist.length;i++){
                if(injectionlist[i]!=null){
                    arrayList.add(injectionlist[i]);
                    Collections.sort(arrayList, new INJECTION());
                }
                else{
                    break;
                }
            }
            for(int i=0; i< arrayList.size();i++){
                if(arrayList.get(i).getDuedate()>nowtime){
                    position=i;
                    break;
                }
            }
            if(count!=arrayList.size()){
                count=arrayList.size();
                recyclerView.scrollToPosition(position);
            }
            adapterIJ.notifyDataSetChanged();
        }
        else if(compoundButton.isChecked()==false){
            for(int i=0; i<injectionlist.length;i++){
                if(injectionlist[i]!=null&&injectionlist[i].getCheck()==false){
                    arrayList.add(injectionlist[i]);
                    Collections.sort(arrayList, new INJECTION());
                }
                else{
                    continue;
                }
            }
            for(int i=0; i< arrayList.size();i++){
                if(arrayList.get(i).getDuedate()>nowtime){
                    position=i;
                    break;
                }
            }
            if(count!=arrayList.size()){
                count=arrayList.size();
                recyclerView.scrollToPosition(position);
            }
            adapterIJ.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                // todo
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    if(obj.getString("QR종류").equals("환자QR")){
                        Toast.makeText(this, "Scanned : 환자QR", Toast.LENGTH_SHORT).show();
                        txtcode1.setText(obj.getString("환자코드"));
                        txtname.setText(obj.getString("환자명"));
                    }
                    else if(obj.getString("QR종류").equals("투약QR")){
                        Toast.makeText(this, "Scanned : 투약QR", Toast.LENGTH_SHORT).show();
                        txtcode2.setText(obj.getString("투약품"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    txtcode1.setText(result.getContents());
                }
                if(txtcode1.equals(null)!=true&&txtcode2.equals(null)!=true){
                    patientkey = txtcode1.getText().toString();
                    Drugname = txtcode2.getText().toString();
                    //here
                    databaseReference.child("Patient").child(patientkey).child("INJECTION").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            arrayList.clear();
                            int list = 0;
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                INJECTION injection = snapshot.getValue(INJECTION.class);
                                if(injection.getDrug().equals(Drugname)){
                                    injectionlist[list]=injection;
                                    list++;
                                    arrayList.add(injection);
                                    Collections.sort(arrayList, new INJECTION());
                                }
                                else{
                                    continue;
                                }
                            }
                            for(int i=0; i< arrayList.size();i++){
                                if(arrayList.get(i).getDuedate()>nowtime){
                                    position=i;
                                    break;
                                }
                            }
                            if(count!=arrayList.size()){
                                count=arrayList.size();
                                recyclerView.scrollToPosition(position);
                            }
                            adapterIJ.notifyDataSetChanged();
                            cboxend.setChecked(true);
                            list=0;
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("FIRSTPAGE", String.valueOf(error.toException()));
                        }
                    });
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}