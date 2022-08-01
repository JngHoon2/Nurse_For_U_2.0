package com.nfu2.Injection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nfu2.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InjectionCheck extends AppCompatActivity {

    private TextView txtDRUG, txtDOSAGE, txtROUTE, txtDUEDATE, txtDODATE, txtDOMAN, txtCHECK, txtMENU;
    private EditText txtMESSAGE;
    private TextView txtPCODE, txtPNAME, txtPSEX, txtPROOM, txtPAGE;
    private String drug;
    private Button btnSAVE;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

    private String patientkey="E01348005";
    private String Injectionkey;

    private String dodate="1";
    private String doman="1";
    private String message="";


    private void saveInjection(String Dodate, String Doman, boolean Check, String Message){
        INJECTION injection = new INJECTION(Dodate,Doman,Check, Message);
        databaseReference.child("Patient").child("E01348005").child("INJECTION").child(Injectionkey).child("Dodate").setValue(injection.getDodate());
        databaseReference.child("Patient").child("E01348005").child("INJECTION").child(Injectionkey).child("Doman").setValue(injection.getDoman());
        databaseReference.child("Patient").child("E01348005").child("INJECTION").child(Injectionkey).child("Check").setValue(injection.getCheck());
        databaseReference.child("Patient").child("E01348005").child("INJECTION").child(Injectionkey).child("Message").setValue(injection.getMessage());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injection_check);


        txtPCODE = findViewById(R.id.txtPCODE);
        txtPNAME = findViewById(R.id.txtPNAME);
        txtPSEX = findViewById(R.id.txtPSEX);
        txtPROOM = findViewById(R.id.txtPROOM);
        txtPAGE = findViewById(R.id.txtPAGE);

        txtDRUG = findViewById(R.id.txtDRUG);
        txtDOSAGE = findViewById(R.id.txtDOSAGE);
        txtROUTE = findViewById(R.id.txtROUTE);
        txtDUEDATE = findViewById(R.id.txtDUEDATE);
        txtDODATE = findViewById(R.id.txtDODATE);
        txtDOMAN = findViewById(R.id.txtDOMAN);
        txtCHECK = findViewById(R.id.txtCHECK);
        txtMENU = findViewById(R.id.txtMENU);
        txtMESSAGE = findViewById(R.id.txtMESSAGE);
        btnSAVE = findViewById(R.id.btnSAVE);

        Intent intent =getIntent();
        patientkey=intent.getExtras().getString("patientkey");
        Injectionkey=intent.getExtras().getString("injectionkey");

        //상단 환자정보
        databaseReference.child("Patient").child(patientkey).child("Pt_info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtPCODE.setText(snapshot.child("code").getValue(String.class));
                txtPNAME.setText((snapshot.child("id").getValue(String.class))+" 환자");
                txtPSEX.setText(snapshot.child("sex").getValue(String.class));
                txtPROOM.setText(snapshot.child("room").getValue(Integer.class).toString());
                txtPAGE.setText(snapshot.child("age").getValue(Integer.class).toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("load data error", String.valueOf(error.toException()));
            }
        });
        //투약
        databaseReference.child("Patient").child(patientkey).child("INJECTION").child(Injectionkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtDRUG.setText(snapshot.child("Drug").getValue(String.class));
                txtDOSAGE.setText(snapshot.child("Dosage").getValue(String.class));
                txtROUTE.setText(snapshot.child("Route").getValue(String.class));
                String duedate = snapshot.child("Duedate").getValue(Long.class).toString();
                txtDUEDATE.setText(duedate.substring(0,2)+"/"+duedate.substring(2,4)+"/"+duedate.substring(4,6)+'\n'+duedate.substring(6,8)+":"+duedate.substring(8,10));
                if(snapshot.child("Check").getValue(Boolean.class)==true){
                    txtDODATE.setText(snapshot.child("Dodate").getValue(String.class));
                    txtDOMAN.setText(snapshot.child("Doman").getValue(String.class));
                    txtCHECK.setText("●");
                    btnSAVE.setText("저장");
                    txtMESSAGE.setText(snapshot.child("Message").getValue(String.class));
                };
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("load data error", String.valueOf(error.toException()));
            }
        });

        btnSAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSAVE.getText().equals("투약완료")){
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd hh:mm");
                    String dotime = dateFormat.format(date);
                    txtDODATE.setText(dotime.substring(0,8)+'\n'+dotime.substring(9,14));
                    txtDOMAN.setText("이준용");
                    txtCHECK.setText("●");
                    btnSAVE.setText("저장");
                }
                else if(btnSAVE.getText().equals("저장")){
                    dodate=txtDODATE.getText().toString();
                    doman=txtDOMAN.getText().toString();
                    message=txtMESSAGE.getText().toString();
                    saveInjection(dodate, doman,true, message);
                    Toast.makeText(getApplicationContext(), "저장 완료.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}