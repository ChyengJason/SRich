package com.jscheng.srich.editor;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jscheng.srich.NoteServicePool;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created By Chengjunsen on 2019/3/8
 */
public class NoteImagePool {

    private List<ImageLoaderListener> loaderListeners;

    public static NoteImagePool mInstance;

    public static NoteImagePool getInstance() {
        if (mInstance == null) {
            synchronized (NoteImagePool.class) {
                if (mInstance == null) {
                    mInstance = new NoteImagePool();
                }
            }
        }
        return mInstance;
    }

    private NoteImagePool() {
        loaderListeners = new ArrayList<>();
    }

    public interface ImageLoaderListener {
        void onBitmapReady(String url);
        void onBitmapFailed(String url);
    }

    public void addLoaderListener(ImageLoaderListener loaderListener) {
        this.loaderListeners.add(loaderListener);
    }

    public void removeLoaderListener(ImageLoaderListener loaderListener) {
        this.loaderListeners.remove(loaderListener);
    }

    public String loadUrl(View view, final String url) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(view)
                .asBitmap()
                .apply(options)
                .load(url)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        for (ImageLoaderListener loaderListener: loaderListeners) {
                            loaderListener.onBitmapFailed(url);
                        }
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        for (ImageLoaderListener loaderListener: loaderListeners) {
                            loaderListener.onBitmapReady(url);
                        }
                        return true;
                    }
                });
        return url;
    }

    public Bitmap getCacheBitmap(View view, String url) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .onlyRetrieveFromCache(true);

        FutureTarget<Bitmap> futureBitmap = Glide.with(view)
                .asBitmap()
                .apply(options)
                .load(url)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Bitmap bitmap = null;
        try {
            bitmap = futureBitmap.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
