package com.jscheng.srich.editor.render.line_render;

import android.graphics.Bitmap;
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
        Drawable drawable;
        if (bitmap != null) {
            drawable = new BitmapDrawable(mView.getResources(), bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        } else {
            drawable = mView.getResources().getDrawable(R.mipmap.ic_note_edit_loading);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        return new NoteImageSpan(drawable, width);
    }
}
