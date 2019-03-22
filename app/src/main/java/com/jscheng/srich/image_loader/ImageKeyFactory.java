package com.jscheng.srich.image_loader;

import com.jscheng.srich.utils.MdUtil;

/**
 * Created By Chengjunsen on 2019/3/21
 */
public class ImageKeyFactory {

    public static String generateKey(String url) {
        return MdUtil.encode(url);
    }

    public static String generateKey(String url, int width) {
        return MdUtil.encode(url + width);
    }
}
