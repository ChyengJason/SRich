package com.jscheng.srich.editor.spanRender;

import com.jscheng.srich.editor.spans.NoteImageSpan;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteImageLineSpanRender extends NoteLineSpanRender<NoteImageSpan>{
    @Override
    protected boolean isParagraphStyle(Paragraph paragraph) {
        return false;
    }

    @Override
    protected NoteImageSpan createSpan(int num) {
        return null;
    }
}
