package com.jscheng.srich.image_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Size;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jscheng.srich.utils.StorageUtil;
import com.jscheng.srich.utils.VersionUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class ImageDiskCache {
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

    public ImageDiskCache(Context context) {
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
            DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
            if (snapshot != null) {
                snapshot = mDiskCache.get(key);
                BitmapFactory.Options options = new BitmapFactory.Options();
                BufferedInputStream inputStream = new BufferedInputStream(snapshot.getInputStream(0));
                int degree = DownSampler.getRotate(inputStream);
                Size actualSize = DownSampler.getDimensions(inputStream, options);

                int actualWidth = (degree == 90 || degree == 180) ? actualSize.getHeight() : actualSize.getWidth();
                if (maxWidth > 0)  {
                    options.inSampleSize = actualWidth > maxWidth ? Math.round((float)actualWidth/(float)maxWidth) : 1;
                }
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = DownSampler.decodeStream(inputStream, options);
                bitmap = rotate(bitmap, degree);
                inputStream.close();
                return bitmap;
            } else {
                mDiskCache.remove(key);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap rotate(Bitmap bitmap, int degree) {
        if (degree <= 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return newBitmap;
    }

    private File getDiskCachePath(Context context, String uniqueName){
        String cachePath = StorageUtil.getDiskCachePath(context);
        return new File( cachePath + File.separator + uniqueName);
    }
}
