package com.nfu2.Main;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nfu2.R;

import java.util.List;

public class Listview_Adapter extends BaseAdapter {

    private Context context;
    private List<Notification_Adapter> list;
    private LayoutInflater inflate;
    public Boolean flag = true;

    public Listview_Adapter(List<Notification_Adapter> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(flag)
        {
            return 2;
        }
        else{ return list.size(); }
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Notification_Adapter na = list.get(i);

        if(view == null){
            view = inflate.inflate(R.layout.item_notificationlist, null);
        }

        TextView lbl_title = view.findViewById(R.id.lbl_title);
        TextView lbl_writer = view.findViewById(R.id.lbl_writer);
        TextView lbl_date = view.findViewById(R.id.lbl_date);

        lbl_title.setText(na.getTitle());
        lbl_writer.setText(na.getWriter());
        lbl_date.setText(na.getDate());

        return view;

    }
}