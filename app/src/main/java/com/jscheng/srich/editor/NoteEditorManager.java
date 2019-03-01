package com.jscheng.srich.editor;
import android.text.Editable;
import android.util.Log;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.Options;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

import java.util.ArrayList;
import java.util.List;

import static com.jscheng.srich.editor.NoteEditorRender.StopCode;

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
    private int mSelectionStart;
    private int mSelectionEnd;

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

    public void commandColor(boolean isSelected, boolean draw) {

    }

    public void commandUnderline(boolean isSelected, boolean draw) {

    }

    public void commandItalic(boolean isSelected, boolean draw) {

    }

    public void commandBold(boolean isSelected, boolean draw) {

    }

    public void commandSuperscript(boolean isSelected, boolean draw) {

    }

    public void commandSubscript(boolean isSelected, boolean draw) {

    }

    public void commandStrikeThrough(boolean isSelected, boolean draw) {

    }

    public void commandDividingLine(boolean draw) {

    }

    public void commandBulletList(boolean isSelected, boolean draw) {

    }

    public void commandNumList(boolean isSelected, boolean draw) {

    }


    public void commandDelete(boolean draw) {

    }

    public void commandPaste(String content, boolean draw) {

    }

    public void commandEnter(boolean draw) {
        // 合并掉区间内容
        mergeSelectionParagraphs();

        Paragraph paragraph = getParagraph(mSelectionStart);
        if (paragraph == null ) {
            paragraph = newParagraph(mSelectionStart);
            paragraph.add(StopCode, mOptions); // 结束符号
            paragraph = newParagraph(mSelectionStart);
        } else if (isIndependentParagraph(paragraph)) {
            paragraph.add(StopCode, mOptions); // 结束符号
            int pos = getParagraphPosition(paragraph) + paragraph.getLength();
            paragraph = newParagraph(pos);
        } else {
            paragraph.add(StopCode, mOptions); // 结束符号
            paragraph = newParagraph(mSelectionStart);
        }

        int pos = getParagraphPosition(paragraph);
        mSelectionStart = mSelectionEnd = pos;
        if (draw) {
            requestDraw();
        }
    }

    public void commandInput(char c, boolean draw) {
        // 合并掉区间内容
        mergeSelectionParagraphs();

        Paragraph paragraph = getParagraph(mSelectionStart);
        if (paragraph == null ) {
            paragraph = newParagraph(mSelectionStart);
        } else if (isIndependentParagraph(paragraph)) {
            paragraph.add(StopCode, mOptions); // 结束符号
            int pos = getParagraphPosition(paragraph) + paragraph.getLength();
            paragraph = newParagraph(pos);
        }

        int pos = getParagraphPosition(paragraph);
        paragraph.add(c, mOptions);
        mSelectionStart = mSelectionEnd = pos + 1;

        if (draw) {
            requestDraw();
        }
    }

    /**
     * 合并选择区间
     */
    private void mergeSelectionParagraphs() {
        if (mSelectionStart == mSelectionEnd) {
            return;
        }
        List<Paragraph> intervalParagraphs = getParagraph(mSelectionStart, mSelectionEnd);

        if (intervalParagraphs.size() == 1) {
            Paragraph paragraph = intervalParagraphs.get(0);
            int paragraphPos = getParagraphPosition(paragraph);
            int start = mSelectionStart - paragraphPos;
            int end = mSelectionEnd - paragraphPos;
            paragraph.remove(start, end);
            paragraph.setDirty(true);
            setSeletion(mSelectionStart);

        } else if (intervalParagraphs.size() > 1) {
            Paragraph firstParagraph = intervalParagraphs.get(0);
            Paragraph lastParagraph = intervalParagraphs.get(intervalParagraphs.size() - 1);

            for (int i = 1; i < intervalParagraphs.size() - 1; i++) {
                mNote.getParagraphs().remove(intervalParagraphs.get(i));
            }

            int firstParagraphPos = getParagraphPosition(firstParagraph);
            firstParagraph.remove(mSelectionStart - firstParagraphPos, firstParagraph.getLength());
            firstParagraph.setDirty(true);

            int lastParagraphPos = getParagraphPosition(lastParagraph);
            lastParagraph.remove(lastParagraphPos, mSelectionEnd - lastParagraphPos);
            lastParagraph.setDirty(true);
            setSeletion(mSelectionStart);
        }
    }

    public List<Paragraph> getParagraph(int globalStart, int globalEnd) {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        List<Paragraph> selects = new ArrayList<>();
        int paragraphStart = 0;
        int paragraphEnd = 0;
        for (Paragraph paragraph: paragraphs) {
            paragraphEnd = paragraph.getLength();
            if (globalStart <= paragraphStart && paragraphEnd <= globalEnd) {
                selects.add(paragraph);
            }
            paragraphStart = paragraphEnd;
        }
        return selects;
    }

    public Paragraph getParagraph(int globalPos) {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        int startPos = 0;
        int endPos = 0;
        for (Paragraph paragraph: paragraphs) {
            endPos = paragraph.getLength();
            if (globalPos <= startPos && endPos <= globalPos) {
                return paragraph;
            }
            startPos = endPos;
        }
        return null;
    }

    /**
     * 将全局位置转对应的 paragraph 位置
     * @param paragraph
     * @return
     */
    public int getParagraphPosition(Paragraph paragraph) {
        int pos = 0;
        for (Paragraph item : mNote.getParagraphs()) {
            if (item == paragraph) {
                return pos;
            }
            pos += item.getLength();
        }
        return -1;
    }

    //todo
    public Paragraph newParagraph(int globalPos) {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        Paragraph paragraph = new Paragraph();
        int lineStyle = mOptions.getLineStyle();
        int indentation = mOptions.getIndentation();

        paragraph.setLineStyle(lineStyle);
        paragraph.setIndentation(indentation);
        paragraph.setDirty(true);

        paragraphs.add(paragraph);

        return paragraph;
    }

    public void setSeletion(int globalPos) {
        mSelectionStart = globalPos;
        mSelectionEnd = globalPos;
    }

    public void setSeletion(int globalStart, int globalEnd) {
        mSelectionStart = globalStart;
        mSelectionEnd = globalEnd;
    }

    private boolean isIndependentParagraph(Paragraph paragraph) {
        if (paragraph == null) {
            return false;
        }
        return paragraph.isDividingLine() || paragraph.isImage();
    }

    public void requestDraw() {
        mRender.draw(mEditorText, mNote.getParagraphs(), mSelectionStart, mSelectionEnd);
    }
}
