package com.jscheng.srich.image_loader;

public interface IImagePoolListener {
    void loadedSuccess(String url, String key);
    void loadedFailed(String url, String key, String err);
}