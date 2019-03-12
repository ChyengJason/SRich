package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.util.Size;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jscheng.srich.utils.StorageUtil;
import com.jscheng.srich.utils.VersionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created By Chengjunsen on 2019/3/12
 */
public abstract class AbstractImagePool {
    /**
     * URL网络失败最大重试次数
     */
    private static final int MaxUrlFailedTime = 2;
    /**
     * 正在请求下载的url集合
     */
    private static List<String> mRequestingUrls;
    /**
     * 失败的URL的key和失败次数
     */
    private static HashMap<String, Integer> mFailedUrls;
    /**
     * DiskLrcCache文件夹名字
     */
    private static final String DiskLrcCacheDirName = "image";
    /**
     * 最大内存缓存空间
     */
    private static final int mMemoryCacheSize = (int)Runtime.getRuntime().maxMemory() / 8;
    /**
     * 最大磁盘缓存空间
     */
    private final static int mDiskCacheSize = 10 * 1024 * 1024;
    /**
     * 磁盘图片缓存
     */
    private static DiskLruCache mDiskCache;
    /**
     * 内存图片缓存
     */
    private static LruCache<String, Bitmap> mMemoryCache;
    /**
     * 线程池
     */
    private static ExecutorService mExecutors;
    /**
     * 监听器
     */
    private static IImagePoolListener mListener;

    public AbstractImagePool(Context context, IImagePoolListener listener) {
        try {
            mListener = listener;
            mRequestingUrls = new ArrayList<>();
            mFailedUrls = new HashMap<>();

            mExecutors = Executors.newFixedThreadPool(3);
            mMemoryCache = new LruCache<String, Bitmap>(mMemoryCacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };

            File diskCacheFile = getDiskCachePath(context, DiskLrcCacheDirName);
            int versionCode = VersionUtil.getVersionCode(context);
            mDiskCache = DiskLruCache.open(diskCacheFile, versionCode, 1, mDiskCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCachePath(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator + uniqueName);
    }

    public void submit(final String url, final String key) {
        mExecutors.submit(new Runnable() {
            @Override
            public void run() {
                if (mRequestingUrls.contains(url)) {
                    mListener.loadedFailed(key, url, "it is requesting");
                } else if (mFailedUrls.containsKey(key) && mFailedUrls.get(key) >= MaxUrlFailedTime){
                    mListener.loadedFailed(key, url, "it is out of max failed time");
                } else {
                    mRequestingUrls.add(url);
                    AbstractImagePool.this.run(url, key);
                }
            }
        });
    }

    protected static Bitmap getDiskCacheBitmap(String key, int maxWidth) {
        try {
            Size actualSize = getDiskCacheBitmapSize(key);
            if (actualSize != null && actualSize.getWidth() > 0 && actualSize.getHeight() > 0) {
                DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                if (snapshot != null) {
                    snapshot = mDiskCache.get(key);
                    InputStream inputStream = snapshot.getInputStream(0);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    if (maxWidth > 0)  {
                        options.inSampleSize = actualSize.getWidth() > maxWidth ? Math.round((float)actualSize.getWidth()/(float)maxWidth) : 1;
                    }
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                    if (bitmap != null) {
                        mMemoryCache.put(key, bitmap);
                    }
                    return bitmap;
                } else {
                    mDiskCache.remove(key);
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean isMemoryBitmap(String key) {
       if (mMemoryCache.get(key) != null) {
           return true;
       }
       Size size = getDiskCacheBitmapSize(key);
       return size != null && size.getHeight() > 0 && size.getWidth() > 0;
    }

    protected static Size getDiskCacheBitmapSize(String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
            if (snapshot != null) {
                FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(0);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
                int bitmapWidth = options.outWidth;
                int bitmapHeight = options.outHeight;
                return new Size(bitmapWidth, bitmapHeight);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Bitmap getMemoryCacheBitmap(String key) {
        return mMemoryCache.get(key);
    }

    protected void onLoadedSuccess(InputStream inputStream, String key, String url) {
        writeDiskCacheInputStream(inputStream, key);
        Size size = getDiskCacheBitmapSize(key);
        if (size != null && size.getWidth() > 0 && size.getHeight() > 0) {
            mRequestingUrls.remove(url);
            mListener.loadedSuccess(url, key);
        } else {
            removeDiskCacheInputStream(key);
            onLoadedFailed(key, url, "it is not img");
        }
    }

    protected void onLoadedFailed(String key, String url, String err) {
        mRequestingUrls.remove(url);
        Integer time = mFailedUrls.get(key);
        mFailedUrls.put(key, time == null ? 1 : time + 1);
        mListener.loadedFailed(url, key, err);
    }

    private void writeDiskCacheInputStream(InputStream inputStream, String key) {
        try {
            DiskLruCache.Editor editor = null;
            editor = mDiskCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            byte[] data = new byte[1024];
            while (inputStream.read(data) != -1) {
                outputStream.write(data);
            }
            inputStream.close();
            outputStream.close();
            editor.commit();
            mDiskCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeDiskCacheInputStream(String key) {
        try {
            mDiskCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void run(String url, String key);

}

