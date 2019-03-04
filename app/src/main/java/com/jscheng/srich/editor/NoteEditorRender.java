package com.jscheng.srich.editor;

import android.text.Editable;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.widget.EditText;

import com.jscheng.srich.editor.spanRender.NoteBackgroundSpanRender;
import com.jscheng.srich.editor.spanRender.NoteBoldWordSpanRender;
import com.jscheng.srich.editor.spanRender.NoteDividingLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteItalicSpanRender;
import com.jscheng.srich.editor.spanRender.NoteLineSpanRender;
import com.jscheng.srich.editor.spanRender.NoteStrikethroughWordSpanRender;
import com.jscheng.srich.editor.spanRender.NoteSubscriptSpanRender;
import com.jscheng.srich.editor.spanRender.NoteSuperscriptSpanRender;
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
    }

    public void draw(EditText editText, List<Paragraph> paragraphs, int selectionStart, int selectionEnd) {
        int start = 0;
        int end = 0;
        boolean isDirty = false;
        boolean isClear = false;
        if (!paragraphs.isEmpty()) {
            for (int i = 0; i < paragraphs.size() ; i++) {
                Paragraph paragraph = paragraphs.get(i);
                end = start + paragraph.getLength();
                isDirty = isDirty || paragraph.isDirty();
                if (isDirty ) {
                    if (!isClear) {
                        isClear = clearLaterParagraphs(start, editText);
                    }
                    drawParagraph(paragraph, start, editText);
                    if (i < paragraphs.size() - 1) {
                        drawEndCode(end, editText.getText());
                    }
                }
                start = end + 1;
            }
        }
        editText.setSelection(selectionStart, selectionEnd);
    }

    /**
     * TODO: 后续改进
     * @param start
     * @param editText
     * @return
     */
    private boolean clearLaterParagraphs(int start, EditText editText) {
        Editable editable = editText.getText();
        Log.e(TAG, "clearLaterParagraphs: " + start + " -> " + editable.length());
        CharacterStyle[] styles = editable.getSpans(start, editable.length(), CharacterStyle.class);
        for (CharacterStyle style: styles) {
            editable.removeSpan(style);
        }
        editable.delete(start, editable.length());
        return true;
    }

    private void drawParagraph(Paragraph paragraph, int globalPos, EditText text) {
        int pos = globalPos;
        pos = drawLineStyle(paragraph, pos, text);
        drawCodeStyle(paragraph, pos, text);
        paragraph.setDirty(false);
    }

    private int drawLineStyle(Paragraph paragraph, int globalPos, EditText text) {
        int pos = globalPos;
        for (NoteLineSpanRender spanRender: mLineSpanRenderList) {
            pos = spanRender.draw(pos, paragraph, text);
        }
        return pos;
    }

    private void drawCodeStyle(Paragraph paragraph, int pos, EditText text) {
        Editable editable = text.getText();
        editable.insert(pos, paragraph.getWords());

        for (NoteWordSpanRender wordSpanRender: mWordSpanRenderList) {
            wordSpanRender.draw(pos, paragraph, text);
        }
    }

    private void drawEndCode(int pos, Editable editable) {
        editable.insert(pos, EndCode);
    }

}
