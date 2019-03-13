package com.jscheng.srich.editor.render.word_render;

import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.editor.spans.NoteBackgroundSpan;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteBackgroundSpanRender extends NoteWordSpanRender<NoteBackgroundSpan> {
    @Override
    protected NoteBackgroundSpan createSpan() {
        return NoteBackgroundSpan.create();
    }

    @Override
    protected int getStyle() {
        return Style.BackgroudColor;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
