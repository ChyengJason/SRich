package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created By Chengjunsen on 2019/3/12
 */
public class LocalImagePool extends AbstractImagePool {
    private static final String TAG = "LocalImagePool";

    private Context mContext;

    public LocalImagePool(Context context, IImagePoolListener listener) {
        super(context, listener);
        mContext = context;
    }

    @Override
    protected void run(String url, String key) {
        try {
            Uri uri = Uri.parse(url);
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            onLoadedSuccess(inputStream, key, url);
        } catch (IOException e) {
            e.printStackTrace();
            onLoadedFailed(key, url, e.toString());
        }
    }
}
