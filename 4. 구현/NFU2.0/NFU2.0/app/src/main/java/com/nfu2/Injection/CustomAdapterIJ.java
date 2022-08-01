package com.nfu2.Injection;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nfu2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterIJ extends RecyclerView.Adapter<CustomAdapterIJ.CustomViewHolderIJ> implements com.nfu2.Injection.OnPersonItemClickListener {

    private ArrayList<INJECTION> arrayList;
    private Context context;
    com.nfu2.Injection.OnPersonItemClickListener listener;

    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyMMddHHmm");
    private String ntime = simpleDateFormat.format(mDate);
    private long nowtime = Long.parseLong(ntime);


    public CustomAdapterIJ(ArrayList<INJECTION> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolderIJ onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.injection_item, parent, false);
                //LayoutInflater.from(parent.getContext()).inflate(R.layout.injection_item, parent, false);
        //CustomViewHolderIJ holderIJ = new CustomViewHolderIJ(view);
        return new CustomViewHolderIJ(view, this::OnItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolderIJ holder, int position) {
        INJECTION item = arrayList.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void OnItemClick(CustomViewHolderIJ holder, View view, int position) {
        if(listener!=null){
            listener.OnItemClick(holder, view, position);
        }
    }

    public void setItemClickListener(com.nfu2.Injection.OnPersonItemClickListener listener){
        this.listener = listener;
    }

    public class CustomViewHolderIJ extends RecyclerView.ViewHolder{

        TextView txtDRUG, txtDOSAGE, txtROUTE, txtDUEDATE, txtDODATE, txtDOMAN, txtCHECK;

        CustomViewHolderIJ(@NonNull View itemView, com.nfu2.Injection.OnPersonItemClickListener listener) {
            super(itemView);
            this.txtDRUG = itemView.findViewById(R.id.txtDRUG);
            this.txtDOSAGE = itemView.findViewById(R.id.txtDOSAGE);
            this.txtROUTE = itemView.findViewById(R.id.txtROUTE);
            this.txtDUEDATE = itemView.findViewById(R.id.txtDUEDATE);
            this.txtDODATE = itemView.findViewById(R.id.txtDODATE);
            this.txtDOMAN = itemView.findViewById(R.id.txtDOMAN);
            this.txtCHECK = itemView.findViewById(R.id.txtCHECK);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getBindingAdapterPosition();
                    if(listener!=null){
                        listener.OnItemClick(CustomViewHolderIJ.this,v,position);
                    }
                }
            });
        }
        public void setItem(INJECTION item) {

            if(item.getCheck()==true){
                this.itemView.setBackgroundColor(Color.GRAY);
            }
            else{
                this.itemView.setBackgroundColor(Color.BLUE);
            }
            txtDRUG.setText(item.getDrug());
            txtDOSAGE.setText(item.getDosage());
            txtROUTE.setText(item.getRoute());
            String duedate = String.valueOf(item.getDuedate());
            txtDUEDATE.setText(duedate.substring(0,2)+"/"+duedate.substring(2,4)+"/"+duedate.substring(4,6)+'\n'+duedate.substring(6,8)+":"+duedate.substring(8,10));
            txtDODATE.setText(item.getDodate());
            txtDOMAN.setText(item.getDoman());
            if(item.getCheck()==true){
                txtCHECK.setText("●");
            }
            else{
                txtCHECK.setText(null);
            }
        }
    }
    public INJECTION getItem(int position){
        return arrayList.get(position);
    }
}