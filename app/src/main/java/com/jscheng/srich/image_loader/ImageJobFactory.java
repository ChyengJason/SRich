package com.jscheng.srich.image_loader;

import android.content.Context;

/**
 * Created By Chengjunsen on 2019/3/22
 * 简单的工厂方法
 */
public class ImageJobFactory {

    public static ImageJob build(Context context, String url, String key, ImageJobCallback callback, ImageDiskCache mCache) {
        if (url.startsWith("http")) {
            return new HttpImageJob(context, key, url, callback, mCache);
        } else {
            return new FileImageJob(context, key, url, callback, mCache);
        }
    }
}
