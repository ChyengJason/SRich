package com.jscheng.srich.editor.spanRender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

import com.jscheng.srich.R;
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
    protected boolean isParagraphStyle(Paragraph paragraph) {
        return paragraph.isImage();
    }

    @Override
    protected NoteImageSpan createSpan(int num) {
        Bitmap bitmap = BitmapFactory.decodeResource(mView.getResources(), R.mipmap.ic_compose);
        int width = mView.getWidth() - mView.getPaddingLeft() - mView.getPaddingRight();
        return new NoteImageSpan(bitmap, width);
    }
}
