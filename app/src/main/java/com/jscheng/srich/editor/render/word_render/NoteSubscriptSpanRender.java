package com.jscheng.srich.editor.render.word_render;

import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteSubscriptSpanRender extends NoteWordSpanRender<NoteSubscriptSpan> {
    @Override
    protected NoteSubscriptSpan createSpan() {
        return NoteSubscriptSpan.create();
    }

    @Override
    protected int getStyle() {
        return Style.SubScript;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
