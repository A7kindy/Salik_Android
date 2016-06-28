package com.me.salik.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.modal.OrderInfo;

import java.util.ArrayList;

/**
 * Created by MAC on 6/15/16.
 */
public class HistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutinflater;
    ArrayList<OrderInfo> orderInfos;


    public HistoryAdapter(Context context){
        this.context = context;
        this.layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        orderInfos = new ArrayList<>();
        orderInfos = DataManagement.getInstance().getOrderInfos();
    }

    @Override
    public int getCount() {
        return orderInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return orderInfos.get(position);
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
            convertView = layoutinflater.inflate(R.layout.item_history_list, parent, false);

            listViewHolder.customer_id = (TextView)convertView.findViewById(R.id.customer_id);
            listViewHolder.phone_number = (TextView)convertView.findViewById(R.id.phone_number);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.customer_id.setText(String .valueOf(orderInfos.get(position).getOrder_id()));
        listViewHolder.phone_number.setText(orderInfos.get(position).getOrder_phone_number());

        return convertView;
    }

    static class ViewHolder {

        TextView customer_id;
        TextView phone_number;
    }
}
