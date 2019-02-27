package com.jscheng.srich.editor;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;

import com.jscheng.srich.editor.spans.NoteBackgroundSpan;
import com.jscheng.srich.editor.spans.NoteBoldSpan;
import com.jscheng.srich.editor.spans.NoteColorSpan;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public class NoteEditorStyleApplier {

    public <T> void remove(Class<T> spanClass, Editable editable, int start, int end) {
        T[] styles = editable.getSpans(start, end, spanClass);
        for (int i = 0; i < styles.length; i++) {
            T style = styles[i];
            int spanStart = editable.getSpanStart(style);
            int spanEnd =  editable.getSpanEnd(style);
            if (spanStart < start) {
                addSpan(createSpan(spanClass), editable, spanStart, start);
            }
            if (end < spanEnd) {
                addSpan(createSpan(spanClass), editable, end, spanEnd);
            }
            editable.removeSpan(style);
        }
    }

    public <T> void add(Class<T> spanClass, Editable editable, int start, int end) {
        addSpan(createSpan(spanClass), editable, start, end);
    }

    public void insert(NoteEditorOptions options, Editable editable, int start, int end) {
        if (options.isBold()) {
            addSpan(NoteBoldSpan.create(), editable, start, end);
        }
        if (options.isItalic()) {
            addSpan(NoteItalicSpan.create(), editable, start, end);
        }
        if (options.isUnderline()) {
            addSpan(NoteUnderLineSpan.create(), editable, start, end);
        }
        if (options.isColor()) {
            addSpan(NoteBackgroundSpan.create(), editable, start, end);
        }
        if (options.isSuperScript()) {
            addSpan(NoteSuperscriptSpan.create(), editable, start, end);
        }
        if (options.isSubScript()) {
            addSpan(NoteSubscriptSpan.create(), editable, start, end);
        }
        if (options.isStrikethrough()) {
            addSpan(NoteStrikethroughSpan.create(), editable, start, end);
        }
    }

    private boolean addSpan(Object span, Editable editable, int start, int end) {
        editable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return true;
    }

    private <T> Object createSpan(Class<T> spanClass) {
        if (spanClass == NoteBoldSpan.class) {
            return NoteBoldSpan.create();
        }
        if (spanClass == NoteItalicSpan.class) {
           return NoteItalicSpan.create();
        }
        if (spanClass == NoteUnderLineSpan.class) {
            return NoteUnderLineSpan.create();
        }
        if (spanClass == NoteColorSpan.class) {
            return NoteColorSpan.create(Color.YELLOW);
        }
        if (spanClass == NoteSuperscriptSpan.class) {
            return NoteSuperscriptSpan.create();
        }
        if (spanClass == NoteSubscriptSpan.class) {
            return NoteSubscriptSpan.create();
        }
        if (spanClass == NoteStrikethroughSpan.class) {
            return NoteStrikethroughSpan.create();
        }
        return null;
    }

    public NoteEditorOptions detect(Editable editable, int start, int end) {
        NoteEditorOptions noteEditorOptions = new NoteEditorOptions();
        if (start < 0 || start > end) {
            return noteEditorOptions;
        }
        boolean isBold = isStyle(editable, start, end, NoteBoldSpan.class);
        noteEditorOptions.setBold(isBold);

        boolean isItalic = isStyle(editable, start, end, NoteItalicSpan.class);
        noteEditorOptions.setItalic(isItalic);

        boolean isUnderLine = isStyle(editable, start, end, NoteUnderLineSpan.class);
        noteEditorOptions.setUnderline(isUnderLine);

        boolean isColor = isStyle(editable, start, end, NoteColorSpan.class);
        noteEditorOptions.setColor(isColor);

        boolean isSubscript = isStyle(editable, start, end, NoteSubscriptSpan.class);
        noteEditorOptions.setSubScript(isSubscript);

        boolean isSuperscript = isStyle(editable, start, end, NoteSuperscriptSpan.class);
        noteEditorOptions.setSuperScript(isSuperscript);

        boolean isStrikethrough = isStyle(editable, start, end, NoteStrikethroughSpan.class);
        noteEditorOptions.setStrikethrough(isStrikethrough);

        return noteEditorOptions;
    }

    private <T> boolean isStyle(Editable editable, int start, int end, Class<T> spanClass) {
        T[] spans = editable.getSpans(start, end, spanClass);
        if (spans.length == 0) {
            return false;
        }
        int spanstart = editable.getSpanStart(spans[0]);
        int spanend = editable.getSpanEnd(spans[spans.length - 1]);
        return spanstart <= start && end <= spanend;
    }
}
