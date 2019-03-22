package com.jscheng.srich.image_loader;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public interface ImageGlobalListener {
    void onImageLoadSuccess(String url);
    void onImageLoadFailed(String url, String err);
}
