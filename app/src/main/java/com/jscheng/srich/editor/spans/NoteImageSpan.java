package com.jscheng.srich.editor.spans;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

import java.lang.ref.WeakReference;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageSpan extends ReplacementSpan implements NoteClickSpan{

    private WeakReference<Bitmap> mBitmap;
    private int mWidth;

    public NoteImageSpan(Bitmap bitmap, int width) {
        this.mBitmap = new WeakReference<>(bitmap);
        this.mWidth = width;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Bitmap bitmap = mBitmap.get();
        if (fm != null && bitmap != null) {
            fm.ascent = -bitmap.getHeight();
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Bitmap bitmap = mBitmap.get();
        if (bitmap != null) {
            canvas.save();

            float destWidth = Math.min(bitmap.getWidth(), mWidth);
            float destHeight = bitmap.getHeight() * (destWidth / (float) bitmap.getWidth());

            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect destRect = new Rect(0, 0, (int)destWidth, (int)destHeight);

            int transY = (int) (bottom - destHeight);
            transY -= paint.getFontMetricsInt().descent;
            int transX = (int) (x + (mWidth - destWidth) / 2);
            canvas.translate(transX, transY);
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
            canvas.restore();
        }
    }
}
