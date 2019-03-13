package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.style.LeadingMarginSpan;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteNumSpan implements LeadingMarginSpan {
    private int mNum;
    private int mTextSize;
    private int mWidth = 50;

    public NoteNumSpan(int textSize, int num) {
        this.mNum = num;
        this.mTextSize = textSize;
    }

    public static NoteNumSpan create(int textSize, int num) {
        return new NoteNumSpan(textSize, num);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mWidth;
    }

    @Override
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        canvas.save();
        paint.setTextSize(mTextSize);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(mNum) + ".", x + dir, baseline, paint);
        canvas.restore();
    }
}
