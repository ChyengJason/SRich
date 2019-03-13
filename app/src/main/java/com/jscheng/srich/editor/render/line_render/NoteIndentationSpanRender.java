package com.jscheng.srich.editor.render.line_render;

import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteIndentationSpan;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/7
 */
public class NoteIndentationSpanRender extends NoteLineSpanRender<NoteIndentationSpan> {

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.getIndentation() > 0;
    }

    @Override
    protected NoteIndentationSpan createSpan(int num, int level, String url) {
        return NoteIndentationSpan.create(level);
    }
}
