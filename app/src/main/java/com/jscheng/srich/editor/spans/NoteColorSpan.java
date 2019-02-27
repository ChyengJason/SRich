package com.jscheng.srich.editor.spans;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteColorSpan extends ForegroundColorSpan {
    public NoteColorSpan(int color) {
        super(color);
    }

    public static NoteColorSpan create(int color) {
        return new NoteColorSpan(color);
    }
}
