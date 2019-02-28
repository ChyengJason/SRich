package com.jscheng.srich.editor;

import android.text.Editable;

import com.jscheng.srich.editor.spans.NoteBackgroundSpan;
import com.jscheng.srich.editor.spans.NoteBoldSpan;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;
import com.jscheng.srich.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public class NoteEditorManager {
    private Note mNote;
    private NoteEditorOptions mEditorOptions;
    private NoteEditorSelection mEditorSelection;
    private List<OnSelectionChangeListener> mSelectionListeners;
    private NoteEditorText mEditorText;

    public NoteEditorManager(NoteEditorText editorText) {
        mEditorOptions = new NoteEditorOptions();
        mSelectionListeners = new ArrayList<>();
        mEditorSelection = new NoteEditorSelection(editorText);
        mEditorText = editorText;
    }

    public void addSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionListeners.add(listener);
    }

    public void notifySelectionChangeListener(int start, int end, NoteEditorOptions options) {
        if (mSelectionListeners != null) {
            for (OnSelectionChangeListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void onTextChanged(Editable editable, int startTextChangePos, int endTextChangePos) {
        NoteEditableExecutor.insert(mEditorOptions, editable, startTextChangePos, endTextChangePos);
    }

    public void onSelectionChanged(Editable editableText, int selStart, int selEnd) {
        mEditorOptions = mEditorSelection.detect(selStart, selEnd);
        notifySelectionChangeListener(selStart, selEnd, mEditorOptions);
    }

    public void commandColor(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteBackgroundSpan.class);
        mEditorOptions.setColor(isSelected);
    }

    public void commandUnderline(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteUnderLineSpan.class);
        mEditorOptions.setUnderline(isSelected);
    }

    public void commandItalic(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteItalicSpan.class);
        mEditorOptions.setItalic(isSelected);
    }

    public void commandBold(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteBoldSpan.class);
        mEditorOptions.setBold(isSelected);
    }

    public void commandSuperscript(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteSuperscriptSpan.class);
        mEditorOptions.setSuperScript(isSelected);
    }

    public void commandSubscript(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteSubscriptSpan.class);
        mEditorOptions.setSubScript(isSelected);
    }

    public void commandStrikeThrough(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteStrikethroughSpan.class);
        mEditorOptions.setStrikethrough(isSelected);
    }

    public void commandDividingLine() {
        deleteIntervleSelected();
        NoteEditableExecutor.addDividingLine(mEditorText.getMeasuredWidth(), mEditorText.getEditableText(), mEditorText.getSelectionStart());
    }

    public void commandBulletList(boolean isSelected) {
        deleteIntervleSelected();
        if (isSelected) {
            NoteEditableExecutor.addBulletList(mEditorText.getEditableText(), mEditorText.getSelectionStart());
        } else {
            NoteEditableExecutor.removeBulletList(mEditorText.getEditableText(), mEditorText.getSelectionStart());
        }
    }

    private <T> void applyIntervelSelectedStyle(boolean isSelected, Class<T> styleSpanCls) {
        int start = mEditorText.getSelectionStart();
        int end = mEditorText.getSelectionEnd();
        Editable editable = mEditorText.getEditableText();

        if (mEditorSelection.isIntervelSelected()) { // 区间选择
            if (isSelected) {
                NoteEditableExecutor.add(styleSpanCls, editable, start, end);
            } else {
                NoteEditableExecutor.remove(styleSpanCls, editable, start, end);
            }
        } else { // 非区间选择
            if (!isSelected) {
                NoteEditableExecutor.seperate(styleSpanCls, editable, start);
            }
        }
    }

    private void deleteIntervleSelected() {
        if (mEditorSelection.isIntervelSelected()) {
            Editable editable = mEditorText.getEditableText();
            int start = mEditorText.getSelectionStart();
            int end = mEditorText.getSelectionEnd();
            editable.delete(start, end);
        }
    }

}
