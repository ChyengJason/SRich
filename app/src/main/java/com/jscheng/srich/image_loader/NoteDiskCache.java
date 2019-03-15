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

/**
 * Created By Chengjunsen on 2019/3/13
 */
public class NoteDiskCache {
    /**
     * DiskLrcCache文件夹名字
     */
    private static final String DiskLrcCacheDirName = "image";
    /**
     * 最大磁盘缓存空间
     */
    private final static int mDiskCacheSize = 10 * 1024 * 1024;
    /**
     * 磁盘图片缓存
     */
    private static DiskLruCache mDiskCache;

    public NoteDiskCache(Context context) {
        try {
            File diskCacheFile = getDiskCachePath(context, DiskLrcCacheDirName);
            int versionCode = VersionUtil.getVersionCode(context);
            mDiskCache = DiskLruCache.open(diskCacheFile, versionCode, 1, mDiskCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isCache(String key) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return snapshot != null && snapshot.getLength(0) > 0;
    }

    public Size getSize(String key) {
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

    public void put(InputStream inputStream, String key) {
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

    public Bitmap get(String key) {
        return get(key, 0);
    }

    public Bitmap get(String key, int maxWidth) {
        try {
            Size actualSize = getSize(key);
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

    public boolean remove(String key) {
        try {
            return mDiskCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private File getDiskCachePath(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator + uniqueName);
    }
}
