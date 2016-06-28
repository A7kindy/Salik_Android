package com.me.salik.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.salik.R;

import java.util.ArrayList;

/**
 * Created by MAC on 6/13/16.
 */
public class ListMenuAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutinflater;
    private String[] arrayList;

    public ListMenuAdapter(Context context, String[] arrayList){
        this.context = context;
        this.layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.length;
    }

    @Override
    public Object getItem(int position) {
        return arrayList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;

        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.item_list_menu, parent, false);

            listViewHolder.textView = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.textView.setText(arrayList[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
    }
}
