package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;

import java.io.BufferedInputStream;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public abstract class ImageJob implements Runnable{

    protected String key;

    protected String url;

    protected Context context;

    private ImageJobCallback callback;

    private ImageDiskCache mDiskCache;

    public ImageJob(Context context, String key, String url, ImageJobCallback callback, ImageDiskCache diskCache) {
        this.key = key;
        this.url = url;
        this.callback = callback;
        this.mDiskCache = diskCache;
        this.context = context;
    }

    protected void successed(BufferedInputStream inputStream) {
        Size size = DownSampler.getDimensions(inputStream, new BitmapFactory.Options());
        mDiskCache.put(inputStream, key);
        Bitmap bitmap = mDiskCache.get(key, 0);
        callback.onJobSuccess(url, key);
    }

    protected void failed(String err) {
        callback.onJobFailed(url, key, err);
    }
}
