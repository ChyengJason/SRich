package com.jscheng.srich.editor.render.line_render;

import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteBulletSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteBulletLineSpanRender extends NoteLineSpanRender {

    public NoteBulletLineSpanRender() {
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.isBulletList();
    }

    @Override
    protected Object createSpan(int num, int level, String url) {
        return NoteBulletSpan.create();
    }
}
