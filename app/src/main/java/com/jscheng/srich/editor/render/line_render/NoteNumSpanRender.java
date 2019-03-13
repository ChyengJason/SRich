package com.jscheng.srich.editor.render.line_render;

import android.view.View;

import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteNumSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;
import com.jscheng.srich.utils.DisplayUtil;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteNumSpanRender extends NoteLineSpanRender<NoteNumSpan> {

    public NoteNumSpanRender(){
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.isNumList();
    }

    @Override
    protected NoteNumSpan createSpan(int num, int level, String url) {
        return NoteNumSpan.create(num);
    }
}
