package com.jscheng.srich.editor;
import android.text.Editable;
import android.util.Log;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.Options;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/27
 * SPAN_EXCLUSIVE_EXCLUSIVE // 在Span前后输入的字符都不应用Span效果
 * SPAN_EXCLUSIVE_INCLUSIVE // 在Span前面输入的字符不应用Span效果，后面输入的字符应用Span效果
 * SPAN_INCLUSIVE_EXCLUSIVE // 在Span前面输入的字符应用Span效果，后面输入的字符不应用Span效果
 * SPAN_INCLUSIVE_INCLUSIVE // 在Span前后输入的字符都应用Span效果
 */
public class NoteEditorManager {
    private final static String TAG = "NoteEditorManager";
    private Note mNote;
    private Options mOptions;
    private List<OnSelectionChangeListener> mSelectionListeners;
    private NoteEditorText mEditorText;
    private NoteEditorRender mRender;

    public NoteEditorManager(NoteEditorText editorText) {
        mNote = new Note();
        mRender = new NoteEditorRender();
        mOptions = new Options();
        mSelectionListeners = new ArrayList<>();
        mEditorText = editorText;
    }

    public void addSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionListeners.add(listener);
    }

    public void notifySelectionChangeListener(int start, int end, Options options) {
        if (mSelectionListeners != null) {
            for (OnSelectionChangeListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void commandColor(boolean isSelected) {

    }

    public void commandUnderline(boolean isSelected) {

    }

    public void commandItalic(boolean isSelected) {

    }

    public void commandBold(boolean isSelected) {

    }

    public void commandSuperscript(boolean isSelected) {

    }

    public void commandSubscript(boolean isSelected) {

    }

    public void commandStrikeThrough(boolean isSelected) {

    }

    public void commandDividingLine() {

    }

    public void commandBulletList(boolean isSelected) {

    }

    public void commandNumList(boolean isSelected) {

    }

    public void commandInput(CharSequence text) {
        int start = mEditorText.getSelectionStart();
        int end = mEditorText.getSelectionEnd();
        List<Paragraph> selectParagraphs = getParagraph(start, end);
        Paragraph paragraph;
        if (selectParagraphs.isEmpty()) {
            paragraph = newParagraph(mOptions, start, end);
        } else {
            paragraph = selectParagraphs.get(0);
        }
        for (int i = 0 ; i< text.length(); i++) {
            paragraph.add(text.charAt(i), mOptions);
        }
        Editable editable = mEditorText.getEditableText();
        mRender.draw(editable, mNote.getParagraphs());
    }

    public void commandDelete() {

    }

    public void commandPaste(String content) {

    }

    public List<Paragraph> getParagraph(int start, int end) {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        List<Paragraph> selects = new ArrayList<>();
        int startPos = 0;
        int endPos = 0;
        for (Paragraph paragraph: paragraphs) {
            endPos = paragraph.getLength();
            if (start <= startPos && endPos <= end) {
                selects.add(paragraph);
            }
            startPos = endPos;
        }
        return selects;
    }

    public Paragraph newParagraph(Options options, int start, int end) {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        Paragraph paragraph = new Paragraph();
        int lineStyle = options.getLineStyle();
        int indentation = options.getIndentation();

        paragraph.setLineStyle(lineStyle);
        paragraph.setIndentation(indentation);
        paragraph.setDirty(true);

        paragraphs.add(paragraph);
        return paragraph;
    }
}
