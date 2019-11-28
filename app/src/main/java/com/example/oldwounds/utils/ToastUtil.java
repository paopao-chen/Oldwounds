package com.example.oldwounds.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Create by Politness Chen on 2019/11/23--15:57
 * desc:
 */
public class ToastUtil {

    public static void showShort(Context context,String text) {
        Toast toast = Toast.makeText(context,null,Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }

    public static void showLong(Context context,String text) {
        Toast toast = Toast.makeText(context,null,Toast.LENGTH_LONG);
        toast.setText(text);
        toast.show();
    }

}
