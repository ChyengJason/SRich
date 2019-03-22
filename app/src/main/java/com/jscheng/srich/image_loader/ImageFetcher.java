package com.jscheng.srich.image_loader;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created By Chengjunsen on 2019/3/21
 */
class ImageFetcher implements ImageJobCallback{
    private static final String TAG = "TAG";

    /**
     * 失败最大重试次数
     */
    private static final int MaxUrlFailedTime = 2;

    /**
     * 正在请求下载的url集合
     */
    private volatile static List<String> mRequestingUrls;

    /**
     * 失败的URL的key和失败次数
     */
    private static HashMap<String, Integer> mFailedUrls;

    /**
     * 线程池
     */
    private static ExecutorService mExecutors;

    private ImageDiskCache mDiskCache;

    private Context mAppContext;

    private ImageFetcherCallback mCallback;

    public ImageFetcher(Context context, ImageDiskCache cache, ImageFetcherCallback callback) {
        mDiskCache = cache;
        mAppContext = context;
        mCallback = callback;
        mRequestingUrls = new ArrayList<>();
        mFailedUrls = new HashMap<>();
        mExecutors = Executors.newFixedThreadPool(3);
    }

    public synchronized void load(String url, String key) {
        if (mRequestingUrls.contains(url)) {
            Log.e(TAG, "load: url is requesting");
            return;
        }
        if (mFailedUrls.containsKey(key) && mFailedUrls.get(key) > MaxUrlFailedTime) {
            onJobFailed(url, key, "load: url is failed max time");
            return;
        }
        mRequestingUrls.add(url);
        ImageJob job = ImageJobFactory.build(mAppContext, url, key, this, mDiskCache);
        mExecutors.submit(job);
    }

    @Override
    public synchronized void onJobFailed(final String url, final String key, final String err) {
        mRequestingUrls.remove(url);
        Integer time = mFailedUrls.get(key);
        time = time == null ? 1 : time + 1;
        mFailedUrls.put(key, time);

        mCallback.onFetchFailed(url, key, err);
    }

    @Override
    public synchronized void onJobSuccess(final String url, final String key) {
        mRequestingUrls.remove(url);

        mCallback.onFetchSuccess(url, key);
    }
}
