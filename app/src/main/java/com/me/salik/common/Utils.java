package com.me.salik.common;

import android.widget.EditText;

/**
 * Created by MAC on 6/14/16.
 */
public class Utils {

    public static boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}
