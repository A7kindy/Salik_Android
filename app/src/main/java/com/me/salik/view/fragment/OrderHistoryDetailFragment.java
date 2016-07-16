package com.me.salik.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.modal.HistoryInfo;
import com.me.salik.view.activity.HomeActivity;
import com.me.salik.view.base.BaseFragment;

/**
 * Created by MAC on 7/4/16.
 */
public class OrderHistoryDetailFragment extends BaseFragment implements HomeActivity.OnBackKeyPressedListener{

    LayoutInflater layoutInflater;
    View rootView;

    HomeActivity homeActivity;

    int index = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_order_detail, container,false);
        layoutInflater = inflater;
        homeActivity = (HomeActivity)getActivity();
        homeActivity.pushHistoryStack(this);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            index = bundle.getInt("index");
        }

        initUI();
        return rootView;
    }

    private void initUI(){
        HistoryInfo historyInfo = DataManagement.getInstance().getHistoryInfos().get(index);

        ((TextView)rootView.findViewById(R.id.phone_number)).setText(historyInfo.getOrder_phone_number());
        ((TextView)rootView.findViewById(R.id.location)).setText(historyInfo.getOrder_location_address());
        ((TextView)rootView.findViewById(R.id.datetime)).setText(historyInfo.getOrder_completed_time());

    }


    @Override
    public void onBack() {
        getFragmentManager().popBackStack();
    }
}
