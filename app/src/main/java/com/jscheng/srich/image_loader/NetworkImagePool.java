package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

import com.jscheng.srich.utils.StorageUtil;

public class NetworkImagePool extends AbstractImagePool {
    private static final String TAG = "NetWorkImagePool";
    /**
     * okhttp
     */
    private OkHttpClient mOkhttpClient;
    /**
     * okhttp 文件名字
     */
    private static final String OkhttpCacheDirName = "okhttp";

    NetworkImagePool(Context context, IImagePoolListener listener) {
        super(context, listener);

        int mOkhttpCacheSize = 10 * 1024 * 1024;
        File okhttpCacheFile = getDiskCacheDir(context, OkhttpCacheDirName);
        mOkhttpClient = new OkHttpClient.Builder()
                .cache(new Cache(okhttpCacheFile, mOkhttpCacheSize))
                .build();
    }

    @Override
    protected void run(String url, String key) {
            try {
                Request bitmapRequest = new Request.Builder()
                        .get()
                        .url(url)
                        .build();
                Call call = mOkhttpClient.newCall(bitmapRequest);
                Response response = call.execute();
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    onLoadedSuccess(inputStream, key, url);
//                    onLoadedFailed(key, url, response.message());
                } else {
                    onLoadedFailed(key, url, response.message());
                }

            } catch (IOException e) {
                e.printStackTrace();
                onLoadedFailed(key, url, e.toString());
            }
    }

    private File getDiskCacheDir(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator +uniqueName);
    }
}