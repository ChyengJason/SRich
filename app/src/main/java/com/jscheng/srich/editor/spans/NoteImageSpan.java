package com.jscheng.srich.editor.spans;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.ReplacementSpan;
import android.util.Size;

import java.lang.ref.WeakReference;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageSpan extends ReplacementSpan implements NoteClickSpan, AlignmentSpan{

    private WeakReference<Bitmap> mBitmap;
    private int mViewWidth;
    private int mVerticalMargin;
    private float mBitmapDestWidth;
    private float mBitmapDestHeight;

    public NoteImageSpan(Bitmap bitmap, int width) {
        this.mBitmap = new WeakReference<>(bitmap);
        this.mViewWidth = width;
        this.mVerticalMargin = 50;
        this.mBitmapDestWidth = Math.min(bitmap.getWidth(), mViewWidth);
        this.mBitmapDestHeight = bitmap.getHeight() * (mBitmapDestWidth / (float) bitmap.getWidth());
    }

    @Override
    public Layout.Alignment getAlignment() {
        return Layout.Alignment.ALIGN_CENTER;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        Bitmap bitmap = mBitmap.get();
        if (fm != null && bitmap != null) {
            fm.ascent = -bitmap.getHeight() - mVerticalMargin /2;
            fm.descent = 0;

            fm.top = fm.ascent - mVerticalMargin / 2;
            fm.bottom = 0;
        }
        return (int)mBitmapDestWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Bitmap bitmap = mBitmap.get();
        if (bitmap != null) {
            canvas.save();

            Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect destRect = new Rect(0, 0, (int)mBitmapDestWidth, (int)mBitmapDestHeight);

            int transY = (int) (bottom - mBitmapDestHeight - mVerticalMargin);
            transY -= paint.getFontMetricsInt().descent;
            int transX = (int) x;
            canvas.translate(transX, transY);
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
            canvas.restore();
        }
    }
}
