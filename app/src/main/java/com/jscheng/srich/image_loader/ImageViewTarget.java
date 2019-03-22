package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class ImageViewTarget implements ImageTarget {
    private WeakReference<ImageView> mImageView;
    private String url;
    private String key;

    public ImageViewTarget(ImageView imageView, String key, String url) {
        imageView.setTag(key);
        this.mImageView = new WeakReference(imageView) ;
        this.key = key;
        this.url = url;
    }

    @Override
    public void onResourceReady(final String url, final String key) {
        final ImageView imageView = mImageView.get();
        if (imageView == null) {
            return;
        }
        if (imageView.getMeasuredWidth() > 0) {
            setResource(url, key);
        } else {
            final ViewTreeObserver observer = imageView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    setResource(url, key);
                    imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }
    }

    private void setResource(String url, String key) {
        final ImageView imageView = mImageView.get();
        if (imageView == null) {
            return;
        }
        Bitmap bitmap = ImageLoader.with(imageView.getContext()).getCache(url, imageView.getMeasuredWidth());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onResourceFailed(String url, String key, String err) {
        Log.e("TAG", "loadBitmap faied: " + url + "  " + err);
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int getMaxWidth() {
        final ImageView imageView = mImageView.get();
        if (imageView != null) {
            return imageView.getMeasuredWidth();
        }
        return 0;
    }
}
