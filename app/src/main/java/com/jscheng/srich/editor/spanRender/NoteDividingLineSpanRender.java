package com.jscheng.srich.editor.spanRender;

import android.view.View;

import com.jscheng.srich.editor.spans.NoteDividingLineSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteDividingLineSpanRender extends NoteLineSpanRender {
    private View view;

    public NoteDividingLineSpanRender(View view) {
        this.view = view;
    }

    @Override
    protected boolean isStyle(Paragraph paragraph) {
        return Style.isStyle(paragraph.getLineStyle(), Style.DividingLine);
    }

    @Override
    protected Object createSpan() {
        return NoteDividingLineSpan.create(view.getMeasuredWidth());
    }
}
