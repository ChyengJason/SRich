package com.jscheng.srich.editor.spans;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteBoldSpan extends StyleSpan {

    public NoteBoldSpan() {
        super(Typeface.BOLD);
    }

    public static NoteBoldSpan create() {
        return new NoteBoldSpan();
    }
}
