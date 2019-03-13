package com.jscheng.srich.image_loader;

import android.content.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    NetworkImagePool(NoteMemoryCache memoryCache, NoteDiskCache diskCache, Context context, IImagePoolListener listener) {
        super(memoryCache, diskCache, listener);

        int mOkhttpCacheSize = 10 * 1024 * 1024;
        File okhttpCacheFile = getDiskCachePath(context, OkhttpCacheDirName);
        mOkhttpClient = new OkHttpClient.Builder()
                .cache(new Cache(okhttpCacheFile, mOkhttpCacheSize))
                .build();
    }

    @Override
    protected void submitTask(String url, String key) {
            try {
                Request bitmapRequest = new Request.Builder().get().url(url).build();
                Call call = mOkhttpClient.newCall(bitmapRequest);
                Response response = call.execute();
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    onLoadedSuccess(inputStream, key, url);
                } else {
                    onLoadedFailed(key, url, response.message());
                }
            } catch (IOException e) {
                e.printStackTrace();
                onLoadedFailed(key, url, e.toString());
            }
    }

    @Override
    protected boolean isUrl(String url) {
        return url.toLowerCase().startsWith("http");
    }

    private File getDiskCachePath(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator +uniqueName);
    }
}