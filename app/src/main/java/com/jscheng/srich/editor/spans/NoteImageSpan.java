package com.jscheng.srich.editor.spans;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.style.AlignmentSpan;
import android.text.style.ImageSpan;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageSpan extends ImageSpan implements AlignmentSpan, NoteClickSpan{

    private int margin = 30;

    public NoteImageSpan(BitmapDrawable drawable) {
        super(drawable, ALIGN_BASELINE);
    }

    @Override
    public Layout.Alignment getAlignment() {
        return Layout.Alignment.ALIGN_CENTER;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       @IntRange(from = 0) int start, @IntRange(from = 0) int end,
                       @Nullable Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom - margin * 2;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        BitmapDrawable d = (BitmapDrawable)getDrawable();
        Rect rect = d.getBounds();
        canvas.save();

        int transY = bottom - rect.bottom - margin;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        d.draw(canvas);
        canvas.restore();
    }
}
