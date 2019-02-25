package com.jscheng.srich.editor.spans;

import android.graphics.Color;
import android.text.style.BackgroundColorSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class BackgroundSpan extends BackgroundColorSpan {

    private final static int color = Color.YELLOW;

    public BackgroundSpan() {
        super(color);
    }
}
