package com.jscheng.srich.image_loader;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public interface ImageTarget {

    void onResourceReady(String url, String key);

    void onResourceFailed(String url, String key, String err);

    String getUrl();

    int getMaxWidth();
}
