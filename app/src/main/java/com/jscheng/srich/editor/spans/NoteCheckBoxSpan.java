package com.jscheng.srich.editor.spans;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteCheckBoxSpan extends ReplacementSpan implements NoteClickSpan{
    private Bitmap mBitmap;
    private int margin;
    private int width;
    private int heigth;

    public NoteCheckBoxSpan(Bitmap bitmap) {
        width = 50;
        heigth = 50;
        mBitmap = bitmap;
        margin = 20;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        return width + margin;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        canvas.save();
        if (mBitmap != null) {
            int destTop = top + (bottom - top - heigth) / 2;
            int destBottom = destTop + heigth;
            Rect resRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect destrect = new Rect((int)x, destTop, (int)x + width, destBottom);
            canvas.drawBitmap(mBitmap, resRect, destrect, paint);
        }
        canvas.restore();
    }

    public static NoteCheckBoxSpan create(Bitmap bitmap) {
        return new NoteCheckBoxSpan(bitmap);
    }
}
