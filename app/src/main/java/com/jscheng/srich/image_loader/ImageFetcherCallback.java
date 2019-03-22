package com.jscheng.srich.image_loader;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public interface ImageFetcherCallback {
    void onFetchFailed(final String url, final String key, final String err);
    void onFetchSuccess(final String url, final String key);
}
