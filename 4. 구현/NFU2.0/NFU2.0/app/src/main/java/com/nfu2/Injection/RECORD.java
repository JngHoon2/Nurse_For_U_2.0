package com.nfu2.Injection;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.nfu2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RECORD extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<INJECTION> arrayList= new ArrayList<>();;

    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMdd");
    private String ntime = simpleDateFormat.format(mDate)+"0000";
    private long nowtime = Long.parseLong(ntime);
    private int position=0;
    private int count=0;

    private CheckBox cboxend;

    private INJECTION injectionlist[] = new INJECTION[30];

    private CustomAdapterIJ adapterIJ = new CustomAdapterIJ(arrayList, this);

    public static Context context_RECORD;

    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();




    private List<String> list;          // ???????????? ?????? ???????????????
    private ListView listView;          // ????????? ????????? ???????????????
    private EditText editSearch;        // ???????????? ????????? Input ???
    private ImageButton btnSearch;
    private CustomAdapterIJ2 adapter;      // ??????????????? ????????? ?????????
    private ArrayList<String> arraylist;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        context_RECORD=this;

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterIJ);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "QR?????? ?????? ??? ???????????????.", Toast.LENGTH_SHORT).show();
            }
        });

        cboxend = (CheckBox) findViewById(R.id.cboxend);
        cboxend.setOnCheckedChangeListener(this::onCheckedChanged);



        editSearch = (EditText) findViewById(R.id.editSearch);
        editSearch.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        final InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE) ;
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] patient=editSearch.getText().toString().split(" ");

                databaseReference.child("Patient").child(patient[1]).child("INJECTION").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        arrayList.clear();
                        int list = 0;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            INJECTION injection = snapshot.getValue(INJECTION.class);
                            injectionlist[list]=injection;
                            list++;
                            arrayList.add(injection);
                            Collections.sort(arrayList, new INJECTION());
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

                    }
                });
                //manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //listView.setVisibility(View.INVISIBLE);
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        // ???????????? ????????????.
        list = new ArrayList<String>();
        arraylist = new ArrayList<String>();

        // ????????? ????????? ???????????? ?????? ????????????.
        settingList();

        // ???????????? ?????? ???????????? arraylist??? ????????????.// list ???????????? ?????????.

        //arraylist.addAll(list);

        // ???????????? ????????? ???????????? ????????????.
        adapter = new CustomAdapterIJ2(list, this);

        // ??????????????? ???????????? ????????????.
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //String name[]=arraylist.get(position).split("     ");
                //editSearch.setText(name[0]);
                editSearch.setText(list.get(position));
            }
        });


        // input?????? ???????????? ????????? "addTextChangedListener" ????????? ???????????? ????????????.

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // input?????? ????????? ?????????????????? ????????????.
                // search ???????????? ????????????.
                String text = editSearch.getText().toString();
                search(text);
            }
        });
    }

    // ????????? ???????????? ?????????
    public void search(String charText) {

        // ?????? ??????????????? ???????????? ????????? ?????? ????????????.
        list.clear();
        // ?????? ????????? ???????????? ?????? ???????????? ????????????.
        if (charText.length() == 0) {
            list.addAll(arraylist);
            btnSearch.setVisibility(View.INVISIBLE);
        }
        // ?????? ????????? ??????..
        else
        {
            // ???????????? ?????? ???????????? ????????????.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist??? ?????? ???????????? ???????????? ??????(charText)??? ???????????? ????????? true??? ????????????.
                if (arraylist.get(i).replaceAll("\\s+","").contains(charText.replaceAll("\\s+","")))
                {
                    // ????????? ???????????? ???????????? ????????????.
                    list.add(arraylist.get(i));
                }
            }
            if(list.size()==1){
                btnSearch.setVisibility(View.VISIBLE);
            }
            else{
                btnSearch.setVisibility(View.INVISIBLE);
            }
        }
        // ????????? ???????????? ????????????????????? ???????????? ???????????? ????????? ???????????? ????????? ????????????.
        adapter.notifyDataSetChanged();
    }

    // ????????? ????????? ???????????? ???????????? ????????????.
    private void settingList(){
        databaseReference.child("Patient").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String patientname = snapshot.child("Pt_info").child("id").getValue(String.class)+" "+snapshot.child("Pt_info").child("code").getValue(String.class);
                    list.add(patientname);
                    arraylist.add(patientname);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FIRSTPAGE", String.valueOf(error.toException()));
            }
        });
    }
}