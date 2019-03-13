package com.jscheng.srich.editor.render.word_render;

import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteSuperscriptSpanRender extends NoteWordSpanRender<NoteSuperscriptSpan> {
    @Override
    protected NoteSuperscriptSpan createSpan() {
        return NoteSuperscriptSpan.create();
    }

    @Override
    protected int getStyle() {
        return Style.SuperScript;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
