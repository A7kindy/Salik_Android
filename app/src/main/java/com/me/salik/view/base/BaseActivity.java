package com.me.salik.view.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.me.salik.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Created by MAC on 6/13/16.
 */
public class BaseActivity  extends SherlockFragmentActivity {

    public BaseApplication apps;

    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    protected BaseFragment rootFragment;
    protected BaseFragment activeFragment;
    private static Context context;

    public static enum CUSTOM_ANIMATIONS {
        FADE_IN, SLIDE_FROM_LEFT, SLIDE_FROM_RIGHT, SLIDE_FROM_TOP, SLIDE_FROM_BOTTOM
    }

    @Override
    public View onCreateView(String name, Context c, AttributeSet attrs) {
        context = c;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        apps = (BaseApplication)getApplicationContext();
        return super.onCreateView(name, context, attrs);
    }

    public void showFragment(int contentFrame, BaseFragment fragment,
                             CUSTOM_ANIMATIONS animation) {
        showFragment(contentFrame, fragment, false, animation);
    }

    public void showFragment(int contentFrame, BaseFragment fragment,
                             boolean addToBackStack, CUSTOM_ANIMATIONS animation) {
        this.activeFragment = fragment;
        String tag = UUID.randomUUID().toString();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        switch (animation) {
            case FADE_IN:
                transaction.setCustomAnimations(R.anim.abc_fade_in,
                        R.anim.abc_fade_out, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
            case SLIDE_FROM_LEFT:
                transaction.setCustomAnimations(R.anim.abc_slide_in_left,
                        R.anim.abc_slide_out_right, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
            case SLIDE_FROM_RIGHT:
                transaction.setCustomAnimations(R.anim.abc_slide_in_right,
                        R.anim.abc_slide_out_left, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
            case SLIDE_FROM_BOTTOM:
                transaction.setCustomAnimations(R.anim.abc_slide_in_bottom,
                        R.anim.abc_slide_out_top, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
            case SLIDE_FROM_TOP:
                transaction.setCustomAnimations(R.anim.abc_slide_in_top,
                        R.anim.abc_slide_out_bottom, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
            default:
                transaction.setCustomAnimations(R.anim.abc_fade_in,
                        R.anim.abc_fade_out, R.anim.abc_fade_in,
                        R.anim.abc_fade_out);
                break;
        }

        transaction.replace(contentFrame, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0
                && addToBackStack == false)
            rootFragment = fragment;
    }


    public void showFragment(int contentFrame, BaseFragment fragment,
                             boolean addToBackStack, boolean remove, CUSTOM_ANIMATIONS animation) {

        if (remove) {
            this.popToRoot();
        }
        showFragment(contentFrame, fragment, addToBackStack, animation);
    }

    public void popToRoot() {
        int backStackCount = getSupportFragmentManager()
                .getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = getSupportFragmentManager()
                    .getBackStackEntryAt(i).getId();
            getSupportFragmentManager().popBackStack(backStackId,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        activeFragment = null;
    }

    @Override
    public void onBackPressed() {
        if (activeFragment != null) {
            if (activeFragment.backButtonPressed()) {
                super.onBackPressed();
                int backStackCount = getSupportFragmentManager()
                        .getBackStackEntryCount();
                if (backStackCount > 0) {
                    String tag = getSupportFragmentManager()
                            .getBackStackEntryAt(backStackCount - 1).getName();
                    activeFragment = (BaseFragment) getSupportFragmentManager()
                            .findFragmentByTag(tag);
                } else {
                    if (activeFragment.equals(rootFragment))
                        activeFragment = null;
                    else
                        activeFragment = rootFragment;
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    /**** Hide soft keyboard ****/
    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = getCurrentFocus();
        if (view == null)
            return;

        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /** Get the application context of the activity **/
    public static Context getContext() {
        return context;
    }

    public void showLoadingProgressDialog(){
        this.showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(CharSequence msg){
        if (this.progressDialog == null){
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
        }

        this.progressDialog.setMessage(msg);
        this.progressDialog.show();
    }

    public void dismissProgress(){
        if (this.progressDialog != null){
            this.progressDialog.dismiss();
        }
    }

    public void showMsg(String msg) {
        dismissProgress();
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
//        builder.setIcon(R.drawable.logo_icon_small);
//        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}

