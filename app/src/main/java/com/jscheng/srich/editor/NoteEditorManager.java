package com.jscheng.srich.editor;
import android.util.Log;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.Options;
import com.jscheng.srich.model.Paragraph;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
        deleteSelctionParagraphs();
        if (draw) { requestDraw(); }
    }

    public void commandDelete(int num, boolean draw) {
        deleteSelctionParagraphs(mSelectionStart - num, mSelectionStart);
        if (draw) { requestDraw(); }
    }

    public void commandPaste(String content, boolean draw) {

    }

    public void commandEnter(boolean draw) {
        // 删除区间
        deleteSelctionParagraphs();

        int pos = mSelectionStart;
        // 获取段落
        Paragraph lastParagraph = getParagraph(pos);
        if (lastParagraph == null) {
            lastParagraph = createParagraph(0);
        }

        int index = getParagraphIndex(lastParagraph);

        // 分割成两部分
        int cutPos = pos - getParagraphBegin(lastParagraph);
        int newLineStyle = lastParagraph.getLineStyle();
        int newIndentation = lastParagraph.getIndentation();
        String newWords = lastParagraph.getWords(cutPos, lastParagraph.getLength());
        List<Integer> newWordStyles = new LinkedList<>(
                lastParagraph.getWordStyles(cutPos, lastParagraph.getLength()));

        // 删除旧段落后半部分
        lastParagraph.remove(cutPos, lastParagraph.getLength());

        // 加入新段落
        Paragraph newParagraph = createParagraph(index + 1, newIndentation, newLineStyle);
        newParagraph.setWords(newWords, newWordStyles);

        // 计算新的选择区
        int newPos = getParagraphBegin(newParagraph);
        setSeletion(newPos);

        if (draw) { requestDraw(); }
    }

    public void commandInput(char c, boolean draw) {
        // 删除区间
        deleteSelctionParagraphs();

        int pos = mSelectionStart;
        // 获取段落
        Paragraph paragraph = getParagraph(pos);
        if (paragraph == null) {
            paragraph = createParagraph(0);
        }

        // 计算插入位置
        int begin = getParagraphBegin(paragraph);
        int insertPos = pos - begin;

        // 插入位置
        paragraph.insert(insertPos, c, mOptions);

        // 调整选择区
        setSeletion(pos + 1);
        if (draw) {
            requestDraw();
        }
    }

    public void deleteSelctionParagraphs() {
        deleteSelctionParagraphs(mSelectionStart, mSelectionEnd);
    }

    public void deleteSelctionParagraphs(int start, int end) {
        if (start < 0 || start >= end) {
            return;
        }
        int startPos = 0;
        int endPos;

        Iterator<Paragraph> iter = mNote.getParagraphs().iterator();
        while(iter.hasNext()){
            Paragraph paragraph = iter.next();
            endPos = startPos + paragraph.getLength();
            if (startPos >= start && endPos <= end) {
                iter.remove();
            } else if (startPos <= start && start <= endPos) {
                int startCutPos = start - startPos;
                int endCutPos = end - startPos;
                if (endCutPos > paragraph.getLength()) {
                    endCutPos = paragraph.getLength();
                }
                paragraph.remove(startCutPos, endCutPos);
                paragraph.setDirty(true);
                continue;
            } else if (startPos <= end && end <= endPos) {
                int startCutPos = 0;
                int endCutPos = end - startPos;
                if (endCutPos > paragraph.getLength()) {
                    endCutPos = paragraph.getLength();
                }
                paragraph.remove(startCutPos, endCutPos);
                paragraph.setDirty(true);
                continue;
            }
            startPos = endPos + 1;
        }
        setSeletion(start);
    }

    public Paragraph getParagraph(int globalPos) {
        int startPos = 0;
        int endPos;
        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            if (globalPos >= startPos && globalPos <= endPos) {
                return paragraph;
            }
            startPos = endPos + 1;
        }
        return null;
    }

    private int getParagraphIndex(Paragraph paragraph) {
        return mNote.getParagraphs().indexOf(paragraph);
    }

    public int getParagraphBegin(Paragraph aim) {
        int startPos = 0;
        int endPos;
        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            if (aim == paragraph) {
                return startPos;
            }
            startPos = endPos + 1;
        }
        return startPos;
    }

    public int getParagraphEnd(Paragraph aim) {
        int begin = getParagraphBegin(aim);
        return begin + aim.getLength();
    }

    public Paragraph createParagraph(int index) {
        return createParagraph(index, mOptions.getIndentation(), mOptions.getLineStyle());
    }

    public Paragraph createParagraph(int index, int indenteation, int lineStyle) {
        Paragraph paragraph = new Paragraph();
        paragraph.setDirty(true);
        paragraph.setIndentation(indenteation);
        paragraph.setLineStyle(lineStyle);
        paragraph.setDividingLine(false);

        mNote.getParagraphs().add(index, paragraph);
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

    public void requestDraw() {
        mRender.draw(mEditorText, mNote.getParagraphs(), mSelectionStart, mSelectionEnd);
        print();
    }

    public void print() {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        Log.e(TAG, " count: " + paragraphs.size());
        for (Paragraph paragraph: paragraphs) {
            int startPos = getParagraphBegin(paragraph);
            int endPos = getParagraphEnd(paragraph);
            Log.e(TAG, "[ " + startPos + "->" + endPos + " ] " + paragraph.toString());
        }
        Log.e(TAG, "selection: ( " + mSelectionStart + " , " + mSelectionEnd + " )" );
    }
}
