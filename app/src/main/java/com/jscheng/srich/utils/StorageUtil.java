package com.jscheng.srich.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created By Chengjunsen on 2019/3/8
 */
public class StorageUtil {

    public static String readFile(String path) {
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            String line;
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static void overwiteFile(String path, String text) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDiskCachePath(Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
