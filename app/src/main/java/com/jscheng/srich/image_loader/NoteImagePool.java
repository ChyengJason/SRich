package com.jscheng.srich.image_loader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jscheng.srich.utils.MdUtil;
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

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// todo 拆分
public class NoteImagePool {
    private static final String TAG = "NoteImagePool";
    /**
     * DiskLrcCache文件夹名字
     */
    private static final String DiskLrcCacheDirName = "image";
    /**
     * okhttp 文件名字
     */
    private static final String OkhttpCacheDirName = "okhttp";
    /**
     * okhttp 最大缓存空间
     */
    private static int mOkhttpCacheSize;
    /**
     * URL网络失败最大重试次数
     */
    private static final int MaxUrlFailedTime = 2;
    /**
     * 内存图片缓存
     */
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 磁盘图片缓存
     */
    private DiskLruCache mDiskCache;
    /**
     * 最大内存缓存空间
     */
    private int mMemoryCacheSize;
    /**
     * 最大磁盘缓存空间
     */
    private int mDiskCacheSize;
    /**
     * 正在请求下载的url集合
     */
    private List<String> mRequestingUrls;
    /**
     * 失败的URL的key和失败次数
     */
    private HashMap<String, Integer> mFailedUrls;
    /**
     * 监听器
     */
    private List<NoteImageListener> mImageListeners;

    private OkHttpClient mOkhttpClient;

    private Handler mMainHandler;

    private static NoteImagePool instance;

    private Context mContext;

    private ExecutorService mThreadPool;

    public static NoteImagePool getInstance(Context context) {
        if (instance == null) {
            synchronized (NoteImagePool.class) {
                if (instance == null) {
                    instance = new NoteImagePool(context);
                }
            }
        }
        return instance;
    }

    private NoteImagePool(Context context) {
        mContext = context;
        mRequestingUrls = new ArrayList<>();
        mFailedUrls = new HashMap<>();
        mImageListeners = new ArrayList<>();
        mThreadPool = Executors.newFixedThreadPool(2);
        mMainHandler = new Handler(Looper.getMainLooper());

        mMemoryCacheSize = (int)Runtime.getRuntime().maxMemory() / 8;
        mMemoryCache = new LruCache<String, Bitmap>(mMemoryCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        mDiskCacheSize = 10 * 1024 * 1024;
        File diskCacheFile = getDiskCacheDir(context, DiskLrcCacheDirName);
        int versionCode = VersionUtil.getVersionCode(context);
        try {
            mDiskCache = DiskLruCache.open(diskCacheFile, versionCode, 1, mDiskCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mOkhttpCacheSize = 10 * 1024 * 1024;
        File okhttpCacheFile = getDiskCacheDir(context, OkhttpCacheDirName);
        mOkhttpClient = new OkHttpClient.Builder()
                .cache(new Cache(okhttpCacheFile, mOkhttpCacheSize))
                .build();
    }

    public void loadBitmap(String url) {
        String key = instance.getKeyFromUrl(url);
        if (isMemoryBitmap(key)) {
            return;
        }
        if (isLocalUrl(url)) {
            submitLocalBitmap(url, key);
            return;
        }
        if (isHttpUrl(url)) {
            submitNetworkBitmap(url, key);
            return;
        }
    }

    public Bitmap getBitmap(String url , int maxWidth) {
        String key = instance.getKeyFromUrl(url);
        Bitmap bitmap = getMemoryCacheBitmap(key);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = getDiskCacheBitmap(key, maxWidth);
        if (bitmap != null) {
            return bitmap;
        }
        if (isLocalUrl(url)) {
            submitLocalBitmap(url, key);
        } else if (isHttpUrl(url)) {
            submitNetworkBitmap(url, key);
        }
        return null;
    }

    private Bitmap getMemoryCacheBitmap(String key) {
        return mMemoryCache.get(key);
    }

    private boolean isMemoryBitmap(String key) {
        try {
            if (mMemoryCache.get(key) != null) {
                return true;
            }
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            if (snapshot != null && snapshot.getLength(0) > 0) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Bitmap getDiskCacheBitmap(String key, int maxWidth) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            if (snapshot != null) {
                FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(0);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
                int bitmapWidth = options.outWidth;

                snapshot = mDiskCache.get(key);
                inputStream = (FileInputStream) snapshot.getInputStream(0);
                options.inSampleSize = bitmapWidth > maxWidth ? Math.round((float)bitmapWidth/(float)maxWidth) : 1;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
                if (bitmap != null) {
                    mMemoryCache.put(key, bitmap);
                }
                return bitmap;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isLocalUrl(String url) {
        return url.toLowerCase().startsWith("content");
    }

    private boolean isHttpUrl(String url) {
        return url.toLowerCase().startsWith("http");
    }

    private void submitLocalBitmap(String url, String key) {
        if (url.toLowerCase().contains("content")) {
            mThreadPool.submit(new SubmitLocalBitmapRunnalbe(url, key));
        }
    }

    private void submitNetworkBitmap(final String url, final String key) {
        if (mRequestingUrls.contains(url)) {
            Log.e(TAG, "getNetworkBitmap: " + url + " is Requesting" );
        } else if (mFailedUrls.containsKey(key) && mFailedUrls.get(key) >= MaxUrlFailedTime){
            Log.e(TAG, "getNetworkBitmap: " + url + " is max failed time" );
        } else {
            Request bitmapRequest = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            Call call = mOkhttpClient.newCall(bitmapRequest);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    notifyImageListenersFailed(url, e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    mThreadPool.submit(new SubmitDiskLruCacheRunnable(response.body().bytes(), url, key));
                }
            });
        }
    }

    private class SubmitLocalBitmapRunnalbe implements Runnable {

        private String url;
        private String key;
        public SubmitLocalBitmapRunnalbe (String url, String key) {
            this.url = url;
            this.key = key;
        }
        @Override
        public void run() {
            try {
                Uri uri = Uri.parse(url);
                InputStream inputStream = mContext.getContentResolver().openInputStream(uri);

                BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
                decodeOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, decodeOptions);

                int actualWidth = decodeOptions.outWidth;
                int actualHeight = decodeOptions.outHeight;
                if (actualWidth <= 0 || actualHeight <= 0) {
                    Log.e(TAG, "parseNetworkResponse: is not image");
                    notifyImageListenersFailed(url, "it is not image");
                } else {
                    inputStream = mContext.getContentResolver().openInputStream(uri);
                    DiskLruCache.Editor editor = mDiskCache.edit(key);
                    OutputStream outputStream = editor.newOutputStream(0);
                    byte[] data = new byte[1024];
                    while(inputStream.read(data) != -1) {
                        outputStream.write(data);
                    }
                    outputStream.close();
                    editor.commit();
                    mDiskCache.flush();
                    notifyImageListenersSuccess(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class SubmitDiskLruCacheRunnable implements Runnable {
        private byte[] data;
        private String key;
        private String url;

        public SubmitDiskLruCacheRunnable(byte[] data, String url, String key) {
            this.data = data;
            this.url = url;
            this.key = key;
        }
        @Override
        public void run() {
            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            if (actualWidth <= 0 || actualHeight <= 0) {
                Log.e(TAG, "parseNetworkResponse: is not image");
                return;
            }
            try {
                DiskLruCache.Editor editor = mDiskCache.edit(key);
                OutputStream outputStream = null;
                outputStream = editor.newOutputStream(0);
                outputStream.write(data);
                outputStream.close();
                editor.commit();
                mDiskCache.flush();
                notifyImageListenersSuccess(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getKeyFromUrl(String url) {
        return MdUtil.encode(url);
    }

    private File getDiskCacheDir(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        Log.e(TAG, "getDiskCacheDir: " + cachePath + File.separator +uniqueName);
        return new File( cachePath + File.separator +uniqueName);
    }

    public void addImageListener(NoteImageListener listener) {
        mImageListeners.add(listener);
    }

    public void removeImageListener(NoteImageListener listener) {
        mImageListeners.remove(listener);
    }

    private void notifyImageListenersSuccess(final String url) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (NoteImageListener listener: mImageListeners) {
                    listener.onNoteImageSuccess(url);
                }
            }
        });
    }

    private void notifyImageListenersFailed(final String url, final String err) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                for (NoteImageListener listener: mImageListeners) {
                    listener.onNoteImageFailed(url, err);
                }
            }
        });
    }

}