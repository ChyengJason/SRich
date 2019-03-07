package com.jscheng.srich.editor.spans;

import android.text.style.URLSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteUriSpan extends URLSpan{

    public NoteUriSpan(String url) {
        super(url);
    }

    public static NoteUriSpan create(String url) {
        return new NoteUriSpan(url);
    }
}
