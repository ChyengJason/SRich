package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteNumSpan extends ReplacementSpan{
    private int mNum;
    private int mTextSize;
    private int mWidth = 50;

    public NoteNumSpan(int textSize, int num) {
        this.mNum = num;
        this.mTextSize = textSize;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        canvas.save();
        paint.setTextSize(mTextSize);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(mNum) + ".", x, y, paint);
        canvas.restore();
    }

    public static NoteNumSpan create(int textSize, int num) {
        return new NoteNumSpan(textSize, num);
    }
}
