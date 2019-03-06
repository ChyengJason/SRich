package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteDividingLineSpan extends ReplacementSpan {

    private int mViewWidth;
    private int mlineWidth;
    private int mMargin;

    public NoteDividingLineSpan(int width) {
        this.mViewWidth = width;
        this.mMargin = 50;
        this.mlineWidth = width - 2 * mMargin;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mlineWidth + mMargin;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        int lineX = (int)x + mMargin;
        int lineY = top + (bottom - top) / 2;
        canvas.drawLine(lineX , lineY, lineX + mlineWidth, lineY, paint);
    }

    public static NoteDividingLineSpan create(int mWidth) {
        return new NoteDividingLineSpan(mWidth);
    }
}
