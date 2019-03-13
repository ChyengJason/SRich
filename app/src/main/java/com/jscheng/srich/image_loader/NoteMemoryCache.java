package com.jscheng.srich.image_loader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created By Chengjunsen on 2019/3/13
 */
public class NoteMemoryCache {
    /**
     * 最大内存缓存空间
     */
    private static final int mMemoryCacheSize = (int)Runtime.getRuntime().maxMemory() / 8;
    /**
     * 内存图片缓存
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    public NoteMemoryCache() {
        mMemoryCache = new LruCache<String, Bitmap>(mMemoryCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public synchronized Bitmap get(String key) {
        return mMemoryCache.get(key);
    }

    public synchronized void remove(String key) {
        mMemoryCache.remove(key);
    }

    public synchronized void put(String key, Bitmap bitmap) {
        mMemoryCache.put(key, bitmap);
    }

    public synchronized boolean isCache(String key) {
        return mMemoryCache.get(key) != null;
    }
}
