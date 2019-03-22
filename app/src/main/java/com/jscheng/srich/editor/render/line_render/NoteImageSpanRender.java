package com.jscheng.srich.editor.render.line_render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteImageSpan;
import com.jscheng.srich.image_loader.ImageLoader;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageSpanRender extends NoteLineSpanRender<NoteImageSpan> {

    private int margin = 50;
    private View mView;

    public NoteImageSpanRender(View view) {
        this.mView = view;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
       return paragraph.isImage();
    }

    @Override
    protected NoteImageSpan createSpan(int num, int level, String url) {
        int width = mView.getWidth() - mView.getPaddingLeft() - mView.getPaddingRight();
        Bitmap bitmap = ImageLoader.with(mView.getContext()).get(url, width);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_note_edit_loading);
        }

        int actualWidth = Math.min(width, bitmap.getWidth());
        int actualHeight = (int) ((float) actualWidth / bitmap.getWidth() * bitmap.getHeight());

        BitmapDrawable drawable = new BitmapDrawable(mView.getResources(), bitmap);
        drawable.setBounds(0, 0, actualWidth, actualHeight);

        return new NoteImageSpan(drawable);
    }
}
