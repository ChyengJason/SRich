package com.jscheng.srich.editor.spanRender;

import android.text.Editable;
import android.text.Spanned;
import android.widget.EditText;

import com.jscheng.srich.editor.NoteEditorRender;
import com.jscheng.srich.model.Paragraph;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public abstract class NoteLineSpanRender<T> {
    public void draw(int globalPos, int num, Paragraph paragraph, EditText editText) {
        if (isParagraphStyle(paragraph)) {
            Editable editable = editText.getText();
            int start = globalPos;
            int end = globalPos + paragraph.getLength();
            editable.setSpan(createSpan(num), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (isLineStyle(paragraph) && paragraph.isPlaceHolder()) {
            Editable editable = editText.getText();
            int start = globalPos;
            int end = globalPos + NoteEditorRender.PlaceHoldChar.length();
            editable.setSpan(createSpan(num), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (isDentationStyle(paragraph)) {
            Editable editable = editText.getText();
            int start = globalPos;
            int end = globalPos + paragraph.getLength();
            int level = paragraph.getIndentation();
            editable.setSpan(createSpan(level), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    protected boolean isParagraphStyle(Paragraph paragraph) {
        return false;
    }

    protected boolean isLineStyle(Paragraph paragraph) {
        return false;
    }

    protected boolean isDentationStyle(Paragraph paragraph) {
        return false;
    }

    protected abstract T createSpan(int num);
}
