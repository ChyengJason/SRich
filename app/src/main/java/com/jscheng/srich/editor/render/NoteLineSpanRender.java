package com.jscheng.srich.editor.render;

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
        if (isLineStyle(paragraph) && paragraph.isPlaceHolder()) {
            Editable editable = editText.getText();
            int level = paragraph.getIndentation();
            int start = globalPos;
            int end = globalPos + NoteEditorRender.PlaceHoldChar.length();
            editable.setSpan(createSpan(num, level), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else if (isImageStyle(paragraph) && paragraph.isPlaceHolder()) {
            Editable editable = editText.getText();
            int start = globalPos;
            int end = globalPos + paragraph.getLength();
            String url = paragraph.getImageUrl();
            editable.setSpan(createImageSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
    }

    protected boolean isLineStyle(Paragraph paragraph) {
        return false;
    }

    protected boolean isImageStyle(Paragraph paragraph) {
        return false;
    }

    protected T createSpan() {
        return null;
    }

    protected T createSpan(int num, int level) {
        return null;
    }

    protected T createImageSpan(String url) {
        return null;
    }
}
