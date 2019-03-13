package com.jscheng.srich.editor;

import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

import com.jscheng.srich.editor.render.word_render.NoteBackgroundSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteBoldSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteBulletLineSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteCheckBoxSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteDividingSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteImageSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteIndentationSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteItalicSpanRender;
import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteNumSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteStrikethroughSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteSubscriptSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteSuperscriptSpanRender;
import com.jscheng.srich.editor.render.line_render.NoteUncheckBoxSpanRender;
import com.jscheng.srich.editor.render.word_render.NoteUnderlineSpanRender;
import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.model.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/1
 * 负责渲染绘制
 */
public class NoteEditorRender {
    private static final String TAG = "NoteEditorManager";

    private HashMap<Integer, Integer> mNumMap;

    private List<NoteWordSpanRender> mWordSpanRenderList;
    private List<NoteLineSpanRender> mLineSpanRenderList;

    public NoteEditorRender(EditText editText) {
        mWordSpanRenderList = new ArrayList<>();
        mLineSpanRenderList = new ArrayList<>();
        mNumMap = new HashMap<>();

        mLineSpanRenderList.add(new NoteIndentationSpanRender());

        mWordSpanRenderList.add(new NoteBoldSpanRender());
        mWordSpanRenderList.add(new NoteItalicSpanRender());
        mWordSpanRenderList.add(new NoteBackgroundSpanRender());
        mWordSpanRenderList.add(new NoteStrikethroughSpanRender());
        mWordSpanRenderList.add(new NoteSubscriptSpanRender());
        mWordSpanRenderList.add(new NoteSuperscriptSpanRender());
        mWordSpanRenderList.add(new NoteUnderlineSpanRender());

        mLineSpanRenderList.add(new NoteImageSpanRender(editText));
        mLineSpanRenderList.add(new NoteDividingSpanRender(editText));
        mLineSpanRenderList.add(new NoteBulletLineSpanRender());
        mLineSpanRenderList.add(new NoteNumSpanRender());
        mLineSpanRenderList.add(new NoteCheckBoxSpanRender(editText));
        mLineSpanRenderList.add(new NoteUncheckBoxSpanRender(editText));
    }

    public void draw(EditText editText, List<Paragraph> paragraphs, int selectionStart, int selectionEnd) {
        int start = 0;
        int end = 0;
        boolean isDirty = false;
        if (editText.getText() != null) {
            editText.getText().clear();
        }
        if (!paragraphs.isEmpty()) {
            for (int i = 0; i < paragraphs.size() ; i++) {
                Paragraph paragraph = paragraphs.get(i);
                Paragraph lastParagraph = i > 0 ? paragraphs.get(i - 1) : null;
                end = start + paragraph.getLength();
                isDirty = isDirty || paragraph.isDirty();

                int num = getNum(paragraph, lastParagraph);
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
        editable.insert(pos, NoteEditorConfig.EndCode);
    }

    private int getNum(Paragraph paragraph, Paragraph lastParagraph) {
        int num = 0;
        if (lastParagraph == null) { // 首段落
            mNumMap.clear();
        }

        if (paragraph.isNumList()) {
            int indentation = paragraph.getIndentation();
            if (lastParagraph != null && indentation > lastParagraph.getIndentation()) {
                num = 0;
            } else {
                num = mNumMap.containsKey(indentation) ? mNumMap.get(indentation) : 0;
            }
            num += 1;
            mNumMap.put(indentation, num);
        } else if (!paragraph.isBulletList() || lastParagraph == null || paragraph.getIndentation() <= lastParagraph.getIndentation()) {
            mNumMap.clear();
        }
        return num;
    }
}
