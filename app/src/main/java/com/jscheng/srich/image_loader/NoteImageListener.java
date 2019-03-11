package com.jscheng.srich.image_loader;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public interface NoteImageListener {
    void onNoteImageSuccess(String url);
    void onNoteImageFailed(String url, String err);
}
