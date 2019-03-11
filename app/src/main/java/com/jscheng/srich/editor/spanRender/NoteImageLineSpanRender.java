package com.jscheng.srich.editor.spanRender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.jscheng.srich.R;
import com.jscheng.srich.image_loader.NoteImagePool;
import com.jscheng.srich.editor.spans.NoteImageSpan;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageLineSpanRender extends NoteLineSpanRender<NoteImageSpan>{

    private View mView;

    public NoteImageLineSpanRender(View view) {
        this.mView = view;
    }

    @Override
    protected boolean isImageStyle(Paragraph paragraph) {
       return paragraph.isImage();
    }

    @Override
    protected NoteImageSpan createImageSpan(String url) {
        int width = mView.getWidth() - mView.getPaddingLeft() - mView.getPaddingRight();
        Bitmap bitmap = NoteImagePool.getInstance(mView.getContext()).getBitmap(url, width);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_compose);
        }
        return new NoteImageSpan(bitmap, width);
    }
}
