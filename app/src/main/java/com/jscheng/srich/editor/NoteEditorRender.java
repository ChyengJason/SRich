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
    public static final String EndCode = "^";

    public void draw(EditText editText, List<Paragraph> paragraphs, int selectionStart, int selectionEnd) {
        int start = 0;
        int end = 0;
        editText.getText().clear();

        if (!paragraphs.isEmpty()) {
            for (int i = 0; i < paragraphs.size() - 1; i++) {
                Paragraph paragraph = paragraphs.get(i);
                end = start + paragraph.getLength();

                drawParagraph(paragraph, start, editText.getText());
                drawEndCode(end, editText.getText());
                start = end + 1;
            }
            Paragraph paragraph = paragraphs.get(paragraphs.size() - 1);
            drawParagraph(paragraph, start, editText.getText());
        }

        editText.setSelection(selectionStart, selectionEnd);
    }

    private void drawParagraph(Paragraph paragraph, int pos, Editable editable) {
        editable.insert(pos, paragraph.getWords());
    }

    private void drawEndCode(int pos, Editable editable) {
        editable.insert(pos, EndCode);
    }

}
