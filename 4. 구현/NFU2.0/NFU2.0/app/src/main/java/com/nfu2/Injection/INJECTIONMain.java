package com.nfu2.Injection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nfu2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class INJECTIONMain extends AppCompatActivity {
    private Button btnDO, btnLIST;
    private Button scanQRBtn;
    private TextView txtcode1, txtcode2, txtcheck;

    int a=0;
    private IntentIntegrator qrScan;


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseRef = database.getReference();

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = dateFormat.format(date); return getTime;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDO = (Button) findViewById(R.id.btnDO);
        btnLIST = (Button) findViewById(R.id.btnLIST);
        scanQRBtn = (Button) findViewById(R.id.scanQR);

        txtcode1 = (TextView) findViewById(R.id.txtcode1);
        txtcode2 = (TextView) findViewById(R.id.txtcode2);
        txtcheck = (TextView) findViewById(R.id.txtcheck);

        qrScan = new IntentIntegrator(this);

        btnDO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(INJECTIONMain.this, com.nfu2.Injection.FIRSTPAGE.class);
                intent.putExtra("patientkey", txtcode1.getText());
                startActivity(intent);
            }
        });
        btnLIST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        scanQRBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrScan.setPrompt("Scanning...");
                qrScan.initiateScan();
            }
        });
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
                    }
                    else if(obj.getString("QR종류").equals("투약QR")){
                        Toast.makeText(this, "Scanned : 투약QR", Toast.LENGTH_SHORT).show();
                        txtcode2.setText(obj.getString("환자코드"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    txtcode1.setText(result.getContents());
                }
                if(txtcode1.equals(null)!=true&&txtcode1.getText().equals(txtcode2.getText())){
                    txtcheck.setText("◯");
                }
                else{
                    txtcheck.setText("⨉");
                }
                if(txtcheck.getText().equals("◯")){
                    btnDO.setVisibility(View.VISIBLE);
                    btnLIST.setVisibility(View.VISIBLE);
                }
                else{
                    //btnDO.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}