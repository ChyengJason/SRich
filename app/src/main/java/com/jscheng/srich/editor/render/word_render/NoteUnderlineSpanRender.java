package com.jscheng.srich.editor.render.word_render;

import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteUnderlineSpanRender extends NoteWordSpanRender<NoteUnderLineSpan> {
    @Override
    protected NoteUnderLineSpan createSpan() {
        return NoteUnderLineSpan.create();
    }

    @Override
    protected int getStyle() {
        return Style.UnderLine;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
