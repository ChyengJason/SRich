package com.jscheng.srich.editor.spanRender;

import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteStrikethroughWordSpanRender extends NoteWordSpanRender<NoteStrikethroughSpan> {

    @Override
    protected NoteStrikethroughSpan createSpan() {
        return NoteStrikethroughSpan.create();
    }

    @Override
    protected int getStyle() {
        return Style.Strikethrough;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
