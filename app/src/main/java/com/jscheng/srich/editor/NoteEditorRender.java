package com.jscheng.srich.editor;

import android.text.Editable;
import android.widget.EditText;

import com.jscheng.srich.model.Paragraph;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/1
 * 负责渲染
 */
public class NoteEditorRender {
    public static final char StopCode = '\n';

    public void draw(EditText editText, List<Paragraph> paragraphs, int selectionStart, int selectionEnd) {
        int start = 0;
        boolean isDirty = false;
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraph paragraph = paragraphs.get(i);
            isDirty = isDirty || paragraph.isDirty();
            int end = start + paragraph.getLength();
            if (isDirty) {
                drawParagraph(paragraph, start, end, editText.getText());
                paragraph.setDirty(false);
            }
            start = end;
        }
        editText.setSelection(selectionStart, selectionEnd);
    }

    private void drawParagraph(Paragraph paragraph, int start, int end, Editable editable) {
        editable.delete(start, editable.length());
        editable.insert(start, paragraph.getWords());
    }

}
