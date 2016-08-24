package com.me.salik.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.modal.OrderInfo;

import java.util.ArrayList;

/**
 * Created by MAC on 6/30/16.
 */
public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutinflater;
    ArrayList<OrderInfo> orderInfos;

    public OrderListAdapter(Context context, ArrayList<OrderInfo> orderInfos){
        this.context = context;
        this.layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orderInfos = orderInfos;

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
            convertView = layoutinflater.inflate(R.layout.item_order_list, parent, false);

            listViewHolder.address = (TextView)convertView.findViewById(R.id.address);
            listViewHolder.distance = (TextView)convertView.findViewById(R.id.distance);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        listViewHolder.address.setText(String .valueOf(orderInfos.get(position).getOrder_location_address()));
        listViewHolder.distance.setText(String .valueOf((int)orderInfos.get(position).getOrder_distance()));

        return convertView;
    }


    static class ViewHolder {
        TextView address;
        TextView distance;
    }
}
