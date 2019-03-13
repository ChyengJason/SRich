package com.jscheng.srich.editor.render.line_render;

import android.view.View;

import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteDividingLineSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteDividingSpanRender extends NoteLineSpanRender<NoteDividingLineSpan> {
    private View view;

    public NoteDividingSpanRender(View view) {
        this.view = view;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.isDividingLine();
    }

    @Override
    protected NoteDividingLineSpan createSpan(int num, int level, String url) {
        int width = view.getWidth() - view.getPaddingLeft() - view.getPaddingRight();
        return NoteDividingLineSpan.create(width);
    }
}
