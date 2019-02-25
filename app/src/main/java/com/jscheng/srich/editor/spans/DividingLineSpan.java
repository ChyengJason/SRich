package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class DividingLineSpan extends ReplacementSpan {
    private int mWidth;

    public DividingLineSpan(int mWidth) {
        this.mWidth = mWidth;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setStrokeWidth(1);
        int lineY = top + (bottom - top) / 2;
        canvas.drawLine(x , lineY, x + mWidth, lineY, paint);
    }
}
