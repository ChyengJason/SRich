package com.jscheng.srich.editor.spans;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteNumSpan implements LeadingMarginSpan {
    private int mNum;
    private int mMargin = 15;
    private int mWidth = 40;

    public NoteNumSpan(int num) {
        this.mNum = num;
    }

    public static NoteNumSpan create(int num) {
        return new NoteNumSpan(num);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mWidth + mMargin * 2;
    }

    @Override
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned)text).getSpanStart(this) == start) {
            paint.setColor(Color.BLACK);
            Paint.Style style = paint.getStyle();
            paint.setStyle(Paint.Style.FILL);
            int transX = x + dir + mMargin;
            int transY = baseline;
            canvas.save();
            canvas.translate(transX, transY);
            canvas.drawText(String.valueOf(mNum) + ".", 0, 0, paint);
            canvas.restore();
            paint.setStyle(style);
        }
    }
}
