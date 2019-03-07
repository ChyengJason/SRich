package com.jscheng.srich.editor.spanRender;

import com.jscheng.srich.editor.spans.NoteIndentationSpan;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/7
 */
public class NoteIndentationSpanRender extends NoteLineSpanRender<NoteIndentationSpan> {

    @Override
    protected boolean isDentationStyle(Paragraph paragraph) {
        return paragraph.getIndentation() > 0;
    }

    @Override
    protected NoteIndentationSpan createSpan(int level) {
        return NoteIndentationSpan.create(level);
    }
}
