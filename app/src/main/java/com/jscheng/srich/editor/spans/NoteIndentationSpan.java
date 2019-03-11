package com.jscheng.srich.editor.spans;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;

/**
 * Created By Chengjunsen on 2019/3/7
 */
public class NoteIndentationSpan implements LeadingMarginSpan {

    private int mLevel;
    private int mMargin;

    public NoteIndentationSpan(int level) {
        this.mLevel = level;
        this.mMargin = 50;
    }

    public static NoteIndentationSpan create(int level) {
        return new NoteIndentationSpan(level);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mMargin * mLevel;
    }

    @Override
    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

    }
}
