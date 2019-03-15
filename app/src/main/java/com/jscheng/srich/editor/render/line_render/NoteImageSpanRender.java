package com.jscheng.srich.editor.render.line_render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteImageSpan;
import com.jscheng.srich.image_loader.NoteImageLoader;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageSpanRender extends NoteLineSpanRender<NoteImageSpan> {

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
        Bitmap bitmap = NoteImageLoader.with(mView.getContext()).getBitmap(url, width);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_note_edit_loading);
        }
        return new NoteImageSpan(bitmap, width);
    }
}
