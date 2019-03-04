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

    public void draw(int globalPos, Paragraph paragraph, EditText editText) {
        if (isStyle(paragraph)) {
            Editable editable = editText.getText();
            int start = globalPos;
            int end = globalPos + paragraph.getLength();
            editable.setSpan(createSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    protected abstract boolean isStyle(Paragraph paragraph);

    protected abstract T createSpan();
}
