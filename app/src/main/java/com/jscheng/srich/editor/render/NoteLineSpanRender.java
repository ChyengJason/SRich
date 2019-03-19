package com.jscheng.srich.editor.render;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.widget.EditText;

import com.jscheng.srich.editor.NoteEditorConfig;
import com.jscheng.srich.editor.NoteEditorRender;
import com.jscheng.srich.model.Paragraph;

/**
 * 绘制行样式
 * Created By Chengjunsen on 2019/3/4
 */
public abstract class NoteLineSpanRender<T> {

    public void draw(int globalPos, int num, Paragraph paragraph, SpannableStringBuilder builder) {
        if (isLineStyle(paragraph) && paragraph.isPlaceHolder()) {
            String url = paragraph.getImageUrl();
            int level = paragraph.getIndentation();

            T span = createSpan(num, level, url);
            int start = globalPos;
            int end = 0;

            if (LeadingMarginSpan.class.isAssignableFrom(span.getClass())) {
                end = globalPos + paragraph.getLength();
            } else {
                end = globalPos + NoteEditorConfig.PlaceHoldChar.length();
            }
            builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    protected abstract boolean isLineStyle(Paragraph paragraph);

    protected abstract T createSpan(int num, int level, String url);
}
