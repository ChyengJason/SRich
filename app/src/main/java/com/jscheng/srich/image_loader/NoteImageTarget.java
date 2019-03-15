package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created By Chengjunsen on 2019/3/15
 */
public class NoteImageTarget implements NoteImageListener{

    private WeakReference<ImageView> mImageView;
    private String url;
    private String key;

    public NoteImageTarget(ImageView imageView, String url, String key) {
        NoteImageLoader.addImageListener(this);
        this.mImageView = new WeakReference(imageView) ;
        imageView.setTag(key);
        this.url = url;
        this.key = key;
    }

    @Override
    public void onNoteImageSuccess(String url) {
        if (!url.equals(this.url)) {
            return;
        }
        final ImageView imageView = mImageView.get();
        if (imageView != null) {
            if (imageView.getMeasuredWidth() > 0) {
                loadBitmap();
            } else {
                final ViewTreeObserver observer = imageView.getViewTreeObserver();
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        loadBitmap();
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });
            }
        }
        NoteImageLoader.removeImageListener(this);
    }

    @Override
    public void onNoteImageFailed(String url, String err) {
        if (!url.equals(this.url)) {
            return;
        }
        NoteImageLoader.removeImageListener(this);
    }

    private void loadBitmap() {
        ImageView imageView = mImageView.get();
        if (imageView != null && key.equals(imageView.getTag())) {
            imageView.setTag(null);
            Log.e("TAG", "loadBitmap: " + url );
            Bitmap bitmap = NoteImageLoader.with(imageView.getContext()).getBitmap(url, imageView.getMeasuredWidth());
            imageView.setImageBitmap(bitmap);
        }
    }
}
