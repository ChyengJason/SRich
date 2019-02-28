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
public class NoteBulletSpan extends ReplacementSpan {

    private int mRadius = 5;
    private int mMargin = 10;

    public static NoteBulletSpan create() {
        return new NoteBulletSpan();
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return 2 * mRadius + 2 * mMargin;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        final float yPosition = (top + bottom) / 2f;
        final float xPosition = x + mMargin;

        paint.setColor(Color.BLACK);
        canvas.save();
        canvas.translate(xPosition, yPosition);
        canvas.drawCircle(0, 0, mRadius, paint);
        canvas.restore();
    }
}
