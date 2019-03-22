package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class ImageLoader implements ImageFetcherCallback{

    private ImageMemoryCache mMemoryCache;

    private ImageDiskCache mDiskCache;

    private ImageFetcher mFetcher;

    private Context mAppContext;

    private Handler mMainHandler;

    private Vector<ImageTarget> mTargets;

    private Vector<ImageGlobalListener> mGlobalListener;

    private volatile static ImageLoader instance = null;

    public static ImageLoader with(Context context) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader(context);
                }
            }
        }
        return instance;
    }

    public ImageLoader(Context context) {
        mAppContext = context.getApplicationContext();
        mMemoryCache = new ImageMemoryCache();
        mDiskCache = new ImageDiskCache(context);
        mFetcher = new ImageFetcher(mAppContext, mDiskCache, this);
        mMainHandler = new Handler(Looper.getMainLooper());
        mTargets = new Vector<>();
        mGlobalListener = new Vector<>();
    }

    public void load(String url, ImageView imageView) {
        String key = ImageKeyFactory.generateKey(url);
        ImageViewTarget mViewTarget = new ImageViewTarget(imageView, key, url);

        int width = imageView.getMeasuredWidth();
        if (width > 0 && mMemoryCache.isCache(key, width)) {
            mViewTarget.onResourceReady(url, key);
            return;
        }
        if (mDiskCache.isCache(key)) {
            mViewTarget.onResourceReady(url, key);
            return;
        }
        mTargets.add(mViewTarget);
        mFetcher.load(url, key);
    }

    public void load(String url) {
        String key = ImageKeyFactory.generateKey(url);
        if (mDiskCache.isCache(key)) {
            return;
        }
        mFetcher.load(url, key);
    }

    public Bitmap get(String url, int maxWidth) {
        String key = ImageKeyFactory.generateKey(url);
        Bitmap bitmap = mMemoryCache.get(key, maxWidth);
        if (bitmap != null) {
            return bitmap;
        }

        bitmap = mDiskCache.get(key, maxWidth);
        if (bitmap != null) {
            mMemoryCache.put(key, maxWidth, bitmap);
            return bitmap;
        }

        mFetcher.load(url, key);
        return null;
    }

    public Bitmap getCache(String url, int maxWidth) {
        String key = ImageKeyFactory.generateKey(url);
        Bitmap bitmap = mMemoryCache.get(key, maxWidth);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = mDiskCache.get(key, maxWidth);
        if (bitmap != null) {
            mMemoryCache.put(key, maxWidth, bitmap);
            return bitmap;
        }
        return null;
    }

    public void addGlobalImageListener(ImageGlobalListener listener) {
        mGlobalListener.add(listener);
    }

    public void removeGlobalImageListener(ImageGlobalListener listener) {
        mGlobalListener.remove(listener);
    }

    private void notifyGlobalImageListenerFailed(String url, String err) {
        for (ImageGlobalListener listener: mGlobalListener) {
            listener.onImageLoadFailed(url, err);
        }
    }

    private void notifyGlobalImageListenerSuccess(String url) {
        for (ImageGlobalListener listener: mGlobalListener) {
            listener.onImageLoadSuccess(url);
        }
    }

    private void notifyImageTargetFailed(String url, String key, String err) {
        Iterator<ImageTarget> iter = mTargets.iterator();
        while (iter.hasNext()) {
            ImageTarget target = iter.next();
            if (target.getUrl().equals(url)) {
                target.onResourceFailed(url, key, err);
                iter.remove();
            }
        }
    }

    private void notifyImageTargetSuccess(String url, String key) {
        Iterator<ImageTarget> iter = mTargets.iterator();
        while (iter.hasNext()) {
            ImageTarget target = iter.next();
            if (target.getUrl().equals(url)) {
                target.onResourceReady(url, key);
                iter.remove();
            }
        }
    }

    @Override
    public void onFetchFailed(final String url, final String key, final String err) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyGlobalImageListenerFailed(url, err);
                notifyImageTargetFailed(url, key, err);
            }
        });
    }

    @Override
    public void onFetchSuccess(final String url, final String key) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                notifyGlobalImageListenerSuccess(url);
                notifyImageTargetSuccess(url, key);
            }
        });
    }
}
