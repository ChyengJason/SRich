package com.jscheng.srich.editor.spans;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteItalicSpan extends StyleSpan{

    public NoteItalicSpan() {
        super(Typeface.ITALIC);
    }

    public static NoteItalicSpan create() {
        return new NoteItalicSpan();
    }
}
