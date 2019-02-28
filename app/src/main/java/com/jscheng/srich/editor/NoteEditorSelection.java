package com.jscheng.srich.editor;

import android.text.Editable;
import android.widget.EditText;

import com.jscheng.srich.editor.spans.NoteBackgroundSpan;
import com.jscheng.srich.editor.spans.NoteBoldSpan;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;

/**
 * Created By Chengjunsen on 2019/2/28
 */
public class NoteEditorSelection {

    private NoteEditorText mEditorText;

    public NoteEditorSelection(NoteEditorText mEditorText) {
        this.mEditorText = mEditorText;
    }
    /**
     * 检测 start 到 end 位置相同样式
     * @param start
     * @param end
     * @return
     */
    public NoteEditorOptions detect(int start, int end) {
        NoteEditorOptions noteEditorOptions = new NoteEditorOptions();
        if (start < 0 || start > end) {
            return noteEditorOptions;
        }
        if (start == end && start > 0) {
            start--;
        }
        boolean isBold = isSameStyle(start, end, NoteBoldSpan.class);
        noteEditorOptions.setBold(isBold);

        boolean isItalic = isSameStyle(start, end, NoteItalicSpan.class);
        noteEditorOptions.setItalic(isItalic);

        boolean isUnderLine = isSameStyle(start, end, NoteUnderLineSpan.class);
        noteEditorOptions.setUnderline(isUnderLine);

        boolean isColor = isSameStyle(start, end, NoteBackgroundSpan.class);
        noteEditorOptions.setColor(isColor);

        boolean isSubscript = isSameStyle(start, end, NoteSubscriptSpan.class);
        noteEditorOptions.setSubScript(isSubscript);

        boolean isSuperscript = isSameStyle(start, end, NoteSuperscriptSpan.class);
        noteEditorOptions.setSuperScript(isSuperscript);

        boolean isStrikethrough = isSameStyle(start, end, NoteStrikethroughSpan.class);
        noteEditorOptions.setStrikethrough(isStrikethrough);

        return noteEditorOptions;
    }

    private <T> boolean isSameStyle(int start, int end, Class<T> spanClass) {
        Editable editable = mEditorText.getEditableText();
        T[] spans = editable.getSpans(start, end, spanClass);
        if (spans.length == 0) {
            return false;
        }
        int spanstart = editable.getSpanStart(spans[0]);
        int spanend = editable.getSpanEnd(spans[spans.length - 1]);
        return spanstart <= start && end <= spanend;
    }

    public boolean isIntervelSelected() {
        return mEditorText.getSelectionStart() != mEditorText.getSelectionEnd();
    }
}
