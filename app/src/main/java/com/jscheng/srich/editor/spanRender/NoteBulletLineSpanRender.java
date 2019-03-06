package com.jscheng.srich.editor.spanRender;

import android.view.View;

import com.jscheng.srich.editor.spans.NoteBulletSpan;
import com.jscheng.srich.editor.spans.NoteDividingLineSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteBulletLineSpanRender extends NoteLineSpanRender {

    public NoteBulletLineSpanRender() {
    }

    @Override
    protected boolean isParagraphStyle(Paragraph paragraph) {
        return false;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return Style.isLineStyle(paragraph.getLineStyle(), Style.BulletList);
    }

    @Override
    protected Object createSpan(int num) {
        return NoteBulletSpan.create();
    }
}
