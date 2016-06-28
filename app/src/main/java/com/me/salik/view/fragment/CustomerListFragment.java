package com.me.salik.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.me.salik.R;
import com.me.salik.view.adapter.CustomerListAdapter;
import com.me.salik.view.base.BaseFragment;



/**
 * Created by MAC on 6/13/16.
 */
public class CustomerListFragment extends BaseFragment {

    View rootView;
    CustomerListAdapter adapter;
    ListView listView;

    public CustomerListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_customer_list, container,false);
        initUI();
        return this.rootView;
    }


    public void initUI(){
        listView = (ListView)rootView.findViewById(R.id.list_view);
        adapter = new CustomerListAdapter(getActivity());
        listView.setAdapter(adapter);
    }

}
