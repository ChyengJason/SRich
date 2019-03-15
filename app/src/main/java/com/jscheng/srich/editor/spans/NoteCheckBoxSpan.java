package com.jscheng.srich.editor.spans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteCheckBoxSpan implements NoteClickSpan, LeadingMarginSpan {
    private Bitmap mBitmap;
    private int mMargin = 12;
    private int mWidth = 50;


    public NoteCheckBoxSpan(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return mWidth + mMargin;
    }

    @Override
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (((Spanned)text).getSpanStart(this) == start) {
            paint.setColor(Color.BLACK);
            Paint.Style style = paint.getStyle();
            paint.setStyle(Paint.Style.FILL);
            int transX = x + dir;
            int transY = top;
            canvas.save();
            canvas.translate(transX, transY);
            Rect resRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect destrect = new Rect(0, 0, mWidth, mWidth);
            canvas.drawBitmap(mBitmap, resRect, destrect, paint);
            canvas.restore();
            paint.setStyle(style);
        }
    }

    public static NoteCheckBoxSpan create(Bitmap bitmap) {
        return new NoteCheckBoxSpan(bitmap);
    }

}
