package com.jscheng.srich.editor.spans;

import android.text.style.AbsoluteSizeSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteFontSizeSpan extends AbsoluteSizeSpan {

    public NoteFontSizeSpan(int size) {
        super(size, true);
    }

    public static NoteFontSizeSpan create(int size) {
        return new NoteFontSizeSpan(size);
    }
}
