package com.jscheng.srich.editor;

import android.text.Editable;

import com.jscheng.srich.model.Paragraph;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/1
 * 负责渲染
 */
public class NoteEditorRender {

    public void draw(Editable editable, List<Paragraph> paragraphs) {
        int start = 0;
        boolean isDirty = false;
        for (int i = 0; i < paragraphs.size(); i++) {
            Paragraph paragraph = paragraphs.get(i);
            isDirty = isDirty || paragraph.isDirty();
            int end = start + paragraph.getLength();
            if (isDirty) {
                drawParagraph(paragraph, start, end, editable);
            }
            start = end;
        }
    }

    private void drawParagraph(Paragraph paragraph, int start, int end, Editable editable) {
        editable.delete(start, end);
        editable.insert(start, paragraph.getWords());
    }

}
