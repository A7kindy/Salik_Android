package com.me.salik.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.me.salik.R;
import com.me.salik.modal.DataManagement;
import com.me.salik.view.activity.LoginActivity;
import com.me.salik.view.activity.MainActivity;
import com.me.salik.view.adapter.ListMenuAdapter;
import com.me.salik.view.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by MAC on 6/13/16.
 */
public class SlideMenuFragment extends BaseFragment implements View.OnClickListener {

    View rootView;
    MainActivity delegate;

    ListView menuListView;
    int[] menuItemIndex;
    String[] menuItemTitle;

    ListMenuAdapter adapter;

    Button logout;

    public SlideMenuFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_slide_menu, container, false);

        initData();
        initUI();
        return this.rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        delegate = (MainActivity) getActivity();
        delegate.setSlideMenuFragment(this);
        mCallback = (SideMenuItemClickListener) delegate;

        // initialize members
    }

    public void initData(){

    }

    public void initUI(){
        menuItemIndex = getResources().getIntArray(R.array.nav_drawer_index);
        menuItemTitle = getResources().getStringArray(R.array.nav_drawer_items);
        menuListView = (ListView)this.rootView.findViewById(R.id.list_menu);
        menuListView.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new ListMenuAdapter(getActivity().getApplicationContext(), menuItemTitle);
        menuListView.setAdapter(adapter);

        logout = (Button)rootView.findViewById(R.id.log_out);
        logout.setOnClickListener(this);

    }

    private void selectMenuItem(int position, String title) {
        mCallback.onMenuItemSelected(position, menuItemTitle[position]);

    }

    public class SlideMenuClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectMenuItem(position, menuItemTitle[position]);
        }
    }

    public interface SideMenuItemClickListener {
        public void onMenuItemSelected(int position, String title);
    }

    private SideMenuItemClickListener mCallback;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.log_out){
            initAppData();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void initAppData(){
        DataManagement.getInstance().orderInfos = new ArrayList<>();
    }
}
