package com.jscheng.srich.image_loader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.jscheng.srich.utils.UriPathUtil;

/**
 * Created By Chengjunsen on 2019/3/22
 */
public class FileImageJob extends ImageJob {

    public FileImageJob(Context context, String key, String url, ImageJobCallback callback, ImageDiskCache diskCache) {
        super(context, key, url, callback, diskCache);
    }

    @Override
    public void run() {
        String path = null;
        if (isContentUrl(url)) {
            path = UriPathUtil.getAbsulotePath(context, Uri.parse(url));
        } else if (isFileUrl(url)){
            path = url;
        }
        if (TextUtils.isEmpty(path)) {
            failed("url is not valid");
        } else {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
                successed(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                failed(e.toString());
            }
        }
    }

    private boolean isFileUrl(String url) {
        return new File(url).exists();
    }

    private boolean isContentUrl(String url) {
        return url.toLowerCase().startsWith("content");
    }
}
