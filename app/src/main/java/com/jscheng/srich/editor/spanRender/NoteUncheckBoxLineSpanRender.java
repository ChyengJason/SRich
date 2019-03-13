package com.jscheng.srich.editor.spanRender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.spans.NoteCheckBoxSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteUncheckBoxLineSpanRender extends NoteLineSpanRender<NoteCheckBoxSpan> {

    private View mView;
    private Bitmap mBitmap;

    public NoteUncheckBoxLineSpanRender(View view) {
        this.mView = view;
        this.mBitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.mipmap.ic_note_edit_uncheck);
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return Style.isLineStyle(paragraph.getLineStyle(), Style.UnCheckBox);
    }

    @Override
    protected NoteCheckBoxSpan createSpan(int num) {
        return NoteCheckBoxSpan.create(mBitmap);
    }
}
