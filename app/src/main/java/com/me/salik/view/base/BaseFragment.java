package com.me.salik.view.base;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MAC on 6/13/16.
 */
public class BaseFragment extends Fragment {

    protected BaseFragment rootFragment;
    protected BaseFragment activeFragment;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    public BaseApplication apps;

    protected BaseActivity baseActivity;
    protected View rootView;
    private static BaseFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;

        apps = (BaseApplication)getActivity().getApplicationContext();
        progressDialog = new ProgressDialog(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            baseActivity = (BaseActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must extend BaseActivity");
        }
    }

    @Override
    public void onDestroyView() {
        baseActivity.hideKeyboard();
        super.onDestroyView();
    }

    public boolean backButtonPressed() {
        return true;
    }

    public static FragmentManager getBaseFragmentMagager() {
        return fragment.getFragmentManager();
    }

    protected boolean inflateViewIfNull(LayoutInflater inflater,
                                        ViewGroup container, int resource) {
        if (rootView == null) {
            rootView = inflater.inflate(resource, container, false);
            return true;
        } else {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
            return false;
        }
    }

    public void showMsg(String msg) {
        dismissProgress();
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
//        builder.setIcon(R.drawable.logo_icon_small);
//        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    public void showLoadingProgressDialog(){
        this.showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(String msg){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress(){
        if (this.progressDialog != null){
            this.progressDialog.dismiss();;
        }
    }


}
