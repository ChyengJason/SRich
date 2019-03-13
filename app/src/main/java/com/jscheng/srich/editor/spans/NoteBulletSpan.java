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
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteBulletSpan implements LeadingMarginSpan {

    private int mRadius = 5;
    private int mMargin = 50;

    public static NoteBulletSpan create() {
        return new NoteBulletSpan();
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return (mMargin + mRadius) * 2;
    }

    @Override
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        Paint.Style style = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.save();
        canvas.translate(x + mMargin, (bottom - top - mRadius) / 2);
        canvas.drawCircle(0, 0, mRadius, paint);
        canvas.restore();
        paint.setStyle(style);
    }
}
