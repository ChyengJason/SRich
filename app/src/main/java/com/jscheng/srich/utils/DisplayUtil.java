package com.jscheng.srich.utils;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class DisplayUtil {
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }
}
