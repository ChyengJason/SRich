package com.jscheng.srich.editor;

import android.text.Editable;

import com.jscheng.srich.editor.spans.NoteBoldSpan;
import com.jscheng.srich.editor.spans.NoteColorSpan;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public class NoteEditorStyleManager {
    private NoteEditorOptions mEditorOptions;
    private  NoteEditorStyleApplier mStyleApplier;
    private List<OnSelectionChangeListener> mSelectionListeners;
    private NoteEditorText mEditorText;

    public NoteEditorStyleManager(NoteEditorText editorText) {
        mEditorOptions = new NoteEditorOptions();
        mStyleApplier = new NoteEditorStyleApplier();
        mSelectionListeners = new ArrayList<>();
        mEditorText = editorText;
    }

    public void addSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionListeners.add(listener);
    }

    public void removeSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionListeners.remove(listener);
    }

    public void notifySelectionChangeListener(int start, int end, NoteEditorOptions options) {
        if (mSelectionListeners != null) {
            for (OnSelectionChangeListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void onTextChanged(Editable editable, int startTextChangePos, int endTextChangePos) {
        mStyleApplier.insert(mEditorOptions, editable, startTextChangePos, endTextChangePos);
    }

    public void onSelectionChanged(Editable editableText, int selStart, int selEnd) {
        mEditorOptions = mStyleApplier.detect(editableText, selStart, selEnd);
        notifySelectionChangeListener(selStart, selEnd, mEditorOptions);
    }

    public void commandColor(boolean isSelected) {
        applyIntervelSelectedStyle(isSelected, NoteColorSpan.class);
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

    private <T> void applyIntervelSelectedStyle(boolean isSelected, Class<T> styleSpanCls) {
        if (!isIntervelSelected()) {
            return;
        }

        int start = mEditorText.getSelectionStart();
        int end = mEditorText.getSelectionEnd();
        Editable editable = mEditorText.getEditableText();
        if (isSelected) {
            mStyleApplier.add(styleSpanCls, editable, start, end);
        } else {
            mStyleApplier.remove(styleSpanCls, editable, start, end);
        }
    }

    private boolean isIntervelSelected() {
        return mEditorText.getSelectionStart() != mEditorText.getSelectionEnd();
    }
}
