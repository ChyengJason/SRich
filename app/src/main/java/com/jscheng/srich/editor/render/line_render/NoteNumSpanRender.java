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
    private View mView;
    private int mTextDpSize = 15;

    public NoteNumSpanRender(View view){
        mView = view;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.isNumList();
    }

    @Override
    protected NoteNumSpan createSpan(int num, int level) {
        int textSize = DisplayUtil.sp2px(mView.getContext(), mTextDpSize);
        return NoteNumSpan.create(textSize, num);
    }
}
