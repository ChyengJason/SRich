package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created By Chengjunsen on 2019/3/12
 */
public abstract class AbstractImagePool {
    /**
     * 失败最大重试次数
     */
    private static final int MaxUrlFailedTime = 2;
    /**
     * 正在请求下载的url集合
     */
    private static List<String> mRequestingUrls;
    /**
     * 失败的URL的key和失败次数
     */
    private static HashMap<String, Integer> mFailedUrls;
    /**
     * 线程池
     */
    private static ExecutorService mExecutors;
    /**
     * 监听器
     */
    private IImagePoolListener mListener;

    private NoteMemoryCache mMemoryCache;

    private NoteDiskCache mDiskCache;

    public AbstractImagePool(NoteMemoryCache memoryCache, NoteDiskCache diskCache, IImagePoolListener listener) {
        mListener = listener;
        mMemoryCache = memoryCache;
        mDiskCache = diskCache;

        mRequestingUrls = new ArrayList<>();
        mFailedUrls = new HashMap<>();
        mExecutors = Executors.newFixedThreadPool(3);
    }

    public void submit(final String url, final String key) {
        mExecutors.submit(new Runnable() {
            @Override
            public void run() {
                if (mFailedUrls.containsKey(key) && mFailedUrls.get(key) >= MaxUrlFailedTime){
                    mListener.loadedFailed(key, url, "it is out of max failed time");
                } else if (!mRequestingUrls.contains(url)) {
                    mRequestingUrls.add(url);
                    submitTask(url, key);
                }
            }
        });
    }

    protected void onLoadedSuccess(InputStream inputStream, String key, String url) {
        mDiskCache.put(inputStream, key);
        Size size = mDiskCache.getSize(key);
        if (size != null && size.getWidth() > 0 && size.getHeight() > 0) {
            mRequestingUrls.remove(url);
            mListener.loadedSuccess(url, key);
        } else {
            mDiskCache.remove(key);
            onLoadedFailed(key, url, "it is not img");
        }
    }

    protected void onLoadedFailed(String key, String url, String err) {
        mRequestingUrls.remove(url);
        Integer time = mFailedUrls.get(key);
        mFailedUrls.put(key, time == null ? 1 : time + 1);
        mListener.loadedFailed(url, key, err);
    }

    protected abstract void submitTask(String url, String key);

    protected abstract boolean isUrl(String url);
}

