package com.jscheng.srich.utils;

import android.os.Build;

/**
 * Created By Chengjunsen on 2019/2/22
 */
public final class OsUtil {
    public static boolean isFlyme() {
        String manufacturer = Build.MANUFACTURER;
        if ("meizu".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean isEMUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("huawei".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }
}