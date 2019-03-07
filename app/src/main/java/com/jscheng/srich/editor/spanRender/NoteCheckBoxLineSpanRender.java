package com.jscheng.srich.editor.spanRender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.spans.NoteCheckBoxLineSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteCheckBoxLineSpanRender extends NoteLineSpanRender<NoteCheckBoxLineSpan> {

    private View mView;
    private Bitmap mBitmap;

    public NoteCheckBoxLineSpanRender(View view) {
        this.mView = view;
        this.mBitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.mipmap.ic_note_edit_check);
    }

    @Override
    protected boolean isParagraphStyle(Paragraph paragraph) {
        return false;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return Style.isLineStyle(paragraph.getLineStyle(), Style.CheckBox);
    }

    @Override
    protected NoteCheckBoxLineSpan createSpan(int num) {
        return NoteCheckBoxLineSpan.create(mBitmap);
    }
}
