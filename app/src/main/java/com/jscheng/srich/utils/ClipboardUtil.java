package com.jscheng.srich.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipboardUtil {

    public static void copy(String content, Context context)
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, content);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }

    public static String paste(Context context)
    {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 获取剪贴板的剪贴数据集
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData != null && clipData.getItemCount() > 0) {
            // 从数据集中获取（粘贴）第一条文本数据
            CharSequence text = clipData.getItemAt(0).getText();
            return text.toString();
        }
        return null;
    }

}
