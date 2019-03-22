package com.jscheng.srich.image_loader;

import android.content.Context;

import com.jscheng.srich.utils.StorageUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public class HttpImageJob extends ImageJob {
    /**
     * okhttp
     */
    private static OkHttpClient mOkhttpClient;
    /**
     * okhttp 文件名字
     */
    private static final String OkhttpCacheDirName = "okhttp";

    public HttpImageJob(Context context, String key, String url, ImageJobCallback callback, ImageDiskCache diskCache) {
        super(context, key, url, callback, diskCache);

        int mOkhttpCacheSize = 10 * 1024 * 1024;
        File okhttpCacheFile = getDiskCachePath(context, OkhttpCacheDirName);
        mOkhttpClient = new OkHttpClient.Builder()
                .cache(new Cache(okhttpCacheFile, mOkhttpCacheSize))
                .build();
    }

    @Override
    public void run() {
        try {
            Request bitmapRequest = new Request.Builder().get().url(url).build();
            Call call = mOkhttpClient.newCall(bitmapRequest);
            Response response = call.execute();
            if (response.isSuccessful()) {
                BufferedInputStream inputStream = new BufferedInputStream(response.body().byteStream());
                successed(inputStream);
            } else {
                failed(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
            failed(e.toString());
        }
    }

    private File getDiskCachePath(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator +uniqueName);
    }
}
