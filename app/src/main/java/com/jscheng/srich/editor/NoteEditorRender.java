package com.jscheng.srich.editor;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.jscheng.srich.editor.spanRender.NoteBackgroundSpanRender;
import com.jscheng.srich.editor.spanRender.NoteBoldWordSpanRender;
import com.jscheng.srich.editor.spanRender.NoteBulletLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteCheckBoxLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteDividingLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteItalicSpanRender;
import com.jscheng.srich.editor.spanRender.NoteLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteNumLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteStrikethroughWordSpanRender;
import com.jscheng.srich.editor.spanRender.NoteSubscriptSpanRender;
import com.jscheng.srich.editor.spanRender.NoteSuperscriptSpanRender;
import com.jscheng.srich.editor.spanRender.NoteUncheckBoxLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteUnderlineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteWordSpanRender;
import com.jscheng.srich.model.Paragraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/1
 * 负责渲染绘制
 */
public class NoteEditorRender {
    private static final String TAG = "NoteEditorManager";
    public static final String EndCode = "\n";
    public static final char EndCodeChar = '\n';
    public static final String PlaceHoldChar = "*";

    private List<NoteWordSpanRender> mWordSpanRenderList;
    private List<NoteLineSpanRender> mLineSpanRenderList;

    public NoteEditorRender(EditText editText) {
        mWordSpanRenderList = new ArrayList<>();
        mLineSpanRenderList = new ArrayList<>();

        mWordSpanRenderList.add(new NoteBoldWordSpanRender());
        mWordSpanRenderList.add(new NoteItalicSpanRender());
        mWordSpanRenderList.add(new NoteBackgroundSpanRender());
        mWordSpanRenderList.add(new NoteStrikethroughWordSpanRender());
        mWordSpanRenderList.add(new NoteSubscriptSpanRender());
        mWordSpanRenderList.add(new NoteSuperscriptSpanRender());
        mWordSpanRenderList.add(new NoteUnderlineSpanRender());

        mLineSpanRenderList.add(new NoteDividingLineSpanRender(editText));
        mLineSpanRenderList.add(new NoteBulletLineSpanRender());
        mLineSpanRenderList.add(new NoteNumLineSpanRender(editText));
        mLineSpanRenderList.add(new NoteCheckBoxLineSpanRender(editText));
        mLineSpanRenderList.add(new NoteUncheckBoxLineSpanRender(editText));
    }

    public void draw(EditText editText, List<Paragraph> paragraphs, int selectionStart, int selectionEnd) {
        int start = 0;
        int end = 0;
        int num = 0;
        boolean isDirty = false;
        if (editText.getText() != null) {
            editText.getText().clear();
        }
        if (!paragraphs.isEmpty()) {
            for (int i = 0; i < paragraphs.size() ; i++) {
                Paragraph paragraph = paragraphs.get(i);
                end = start + paragraph.getLength();
                isDirty = isDirty || paragraph.isDirty();
                num = paragraph.isNumList() ? num + 1 : 0;
                drawParagraph(paragraph, start, num, editText);
                if (i < paragraphs.size() - 1) {
                    drawEndCode(end, editText.getText());
                }
                start = end + 1;
            }
        }
        editText.setSelection(selectionStart, selectionEnd);
    }

    private void drawParagraph(Paragraph paragraph, int globalPos, int num, EditText text) {
        int pos = globalPos;
        drawWords(paragraph, pos, text);
        drawLineStyle(paragraph, pos, num, text);
        drawCodeStyle(paragraph, pos, text);
        paragraph.setDirty(false);
    }

    private void drawWords(Paragraph paragraph, int pos, EditText text) {
        Log.e(TAG, "drawWords: " + pos + " " + paragraph.getWords());
        Editable editable = text.getText();
        if (paragraph.getLength() > 0) {
            editable.insert(pos, paragraph.getWords());
        }
    }

    private void drawLineStyle(Paragraph paragraph, int pos, int num, EditText text) {
        for (NoteLineSpanRender spanRender: mLineSpanRenderList) {
            spanRender.draw(pos, num, paragraph, text);
        }
    }

    private void drawCodeStyle(Paragraph paragraph, int pos, EditText text) {
        for (NoteWordSpanRender wordSpanRender: mWordSpanRenderList) {
            wordSpanRender.draw(pos, paragraph, text);
        }
    }

    private void drawEndCode(int pos, Editable editable) {
        editable.insert(pos, EndCode);
    }

}
