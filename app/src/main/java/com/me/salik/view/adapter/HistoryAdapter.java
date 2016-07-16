package com.me.salik.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.modal.HistoryInfo;

import java.util.ArrayList;

/**
 * Created by MAC on 7/2/16.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutinflater;
    ArrayList<HistoryInfo> historyInfos;

    public HistoryAdapter(Context context){
        this.context = context;
        this.layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        historyInfos = new ArrayList<>();
        historyInfos = DataManagement.getInstance().getHistoryInfos();

    }

    @Override
    public int getCount() {
        return historyInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return historyInfos.get(position);
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
            convertView = layoutinflater.inflate(R.layout.item_history, parent, false);

            listViewHolder.order_id = (TextView)convertView.findViewById(R.id.order_id);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.order_id.setText(String .valueOf(historyInfos.get(position).getOrder_id()));

        return convertView;
    }

    static class ViewHolder {
        TextView order_id;
    }
}
