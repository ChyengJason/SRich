package com.jscheng.srich.image_loader;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public interface ImageJobCallback {
    void onJobFailed(final String url, final String key, final String err);
    void onJobSuccess(final String url, final String key);
}
