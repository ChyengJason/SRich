package com.jscheng.srich.editor.spanRender;

import android.view.View;

import com.jscheng.srich.editor.spans.NoteNumSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;
import com.jscheng.srich.utils.DisplayUtil;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteNumLineSpanRender extends NoteLineSpanRender<NoteNumSpan> {
    private View mView;
    private int mTextDpSize = 15;

    public NoteNumLineSpanRender(View view){
        mView = view;
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return Style.isLineStyle(paragraph.getLineStyle(), Style.NumList);
    }

    @Override
    protected NoteNumSpan createSpan(int num) {
        int textSize = DisplayUtil.dip2px(mView.getContext(), mTextDpSize);
        return NoteNumSpan.create(textSize, num);
    }
}
