package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class ImageMemoryCache {
    /**
     * 最大内存缓存空间
     */
    private static final int mMemoryCacheSize = (int)Runtime.getRuntime().maxMemory() / 8;
    /**
     * 内存图片缓存
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    public ImageMemoryCache() {
        mMemoryCache = new LruCache<String, Bitmap>(mMemoryCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public synchronized Bitmap get(String key, int width) {
        return mMemoryCache.get(key + width);
    }

    public synchronized void remove(String key, int width) {
        mMemoryCache.remove(key + width);
    }

    public synchronized void put(String key, int width, Bitmap bitmap) {
        mMemoryCache.put(key + width, bitmap);
    }

    public synchronized boolean isCache(String key, int width) {
        return mMemoryCache.get(key + width) != null;
    }
}
