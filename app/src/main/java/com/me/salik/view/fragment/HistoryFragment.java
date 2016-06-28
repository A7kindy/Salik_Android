package com.me.salik.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.me.salik.R;
import com.me.salik.view.adapter.HistoryAdapter;
import com.me.salik.view.base.BaseFragment;

/**
 * Created by MAC on 6/13/16.
 */
public class HistoryFragment extends BaseFragment {

    View rootView;
    HistoryAdapter adapter;
    ListView listView;

    public HistoryFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        initView();
        return rootView;
    }

    public void initView(){
        listView = (ListView)rootView.findViewById(R.id.list_view);
        adapter = new HistoryAdapter(getActivity());
        listView.setAdapter(adapter);
    }
}
