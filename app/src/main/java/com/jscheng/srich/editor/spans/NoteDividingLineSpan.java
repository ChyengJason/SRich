package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteDividingLineSpan extends ReplacementSpan implements AlignmentSpan {

    private int mlineWidth;
    private int mMargin;

    public NoteDividingLineSpan(int width) {
        this.mMargin = 50;
        this.mlineWidth = width - 2 * mMargin;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return mlineWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2);
        canvas.drawLine(x , y, x + mlineWidth, y, paint);
    }

    public static NoteDividingLineSpan create(int mWidth) {
        return new NoteDividingLineSpan(mWidth);
    }

    @Override
    public Layout.Alignment getAlignment() {
        return Layout.Alignment.ALIGN_CENTER;
    }
}
