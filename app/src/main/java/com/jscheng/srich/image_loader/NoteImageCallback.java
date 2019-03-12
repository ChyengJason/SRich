package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;

/**
 * Created By Chengjunsen on 2019/3/12
 */
public interface NoteImageCallback {
    void onNoteImageSuccess(Bitmap bitmap);
    void onNoteImageFailed(String err);
}
