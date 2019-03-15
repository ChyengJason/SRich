package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created By Chengjunsen on 2019/3/15
 */
public class NoteImageTarget implements NoteImageListener{

    private ImageView mImageView;
    private String url;
    private String key;

    public NoteImageTarget(Context context, ImageView imageView, String url, String key) {
        NoteImageLoader.with(context).addImageListener(this);
        this.mImageView = imageView;
        this.mImageView.setTag(key);
        this.url = url;
        this.key = key;
    }

    @Override
    public void onNoteImageSuccess(String url) {
        if (url == this.url) {
            if (mImageView.getMeasuredWidth() > 0) {
                loadBitmap();
            } else {
                final ViewTreeObserver observer = mImageView.getViewTreeObserver();
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        loadBitmap();
                        observer.removeOnPreDrawListener(this);
                        return false;
                    }
                });
            }
        }
        NoteImageLoader.with(mImageView.getContext()).removeImageListener(this);
    }

    @Override
    public void onNoteImageFailed(String url, String err) {
        NoteImageLoader.with(mImageView.getContext()).removeImageListener(this);
    }

    private void loadBitmap() {
        if (mImageView.getTag() == key) {
            Bitmap bitmap = NoteImageLoader.with(mImageView.getContext()).getBitmap(url, mImageView.getMeasuredWidth());
            mImageView.setImageBitmap(bitmap);
            mImageView.setTag(null);
        }
    }
}
