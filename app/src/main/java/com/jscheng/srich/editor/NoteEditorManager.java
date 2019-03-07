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
        mRender = new NoteEditorRender(editorText);
        mOptions = new Options();
        mSelectionListeners = new ArrayList<>();
        mEditorText = editorText;
    }

    public void addSelectionChangeListener(OnSelectionChangeListener listener) {
        mSelectionListeners.add(listener);
        notifySelectionChangeListener(mSelectionStart, mSelectionEnd, mOptions);
    }

    public void notifySelectionChangeListener(int start, int end, Options options) {
        if (mSelectionListeners != null) {
            for (OnSelectionChangeListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void commandColor(boolean isSelected, boolean draw) {
        mOptions.setColor(isSelected);
    }

    public void commandUnderline(boolean isSelected, boolean draw) {
        mOptions.setUnderline(isSelected);
    }

    public void commandItalic(boolean isSelected, boolean draw) {
        mOptions.setItalic(isSelected);
    }

    public void commandBold(boolean isSelected, boolean draw) {
        mOptions.setBold(isSelected);
    }

    public void commandSuperscript(boolean isSelected, boolean draw) {
        mOptions.setSuperScript(isSelected);
    }

    public void commandSubscript(boolean isSelected, boolean draw) {
        mOptions.setSubScript(isSelected);
    }

    public void commandStrikeThrough(boolean isSelected, boolean draw) {
        mOptions.setStrikethrough(isSelected);
    }

    public void commandDividingLine(boolean draw) {
        inputDividingLine();
        if (draw) { requestDraw(); }
    }

    public void commandBulletList(boolean isSelected, boolean draw) {
        mOptions.setBulletList(true);
        inputBulletList(isSelected);
        if (draw) { requestDraw(); }
    }

    public void commandNumList(boolean isSelected, boolean draw) {
        mOptions.setNumList(isSelected);
        inputNumList(isSelected);
        if (draw) { requestDraw(); }
    }

    public void commandCheckBox(boolean isSelected, boolean draw) {
        if (isSelected) {
            mOptions.setUnCheckBox(true);
            mOptions.setCheckBox(false);
        } else {
            mOptions.setUnCheckBox(false);
            mOptions.setCheckBox(false);
        }
        inputUnCheckBox(isSelected);
        if (draw) { requestDraw(); }
    }

    public void commandDeleteSelection(boolean draw) {
        deleteSelection(mSelectionStart, mSelectionEnd);
        if (draw) { requestDraw(); }
    }

    public void commandDelete(boolean draw) {
        if (mSelectionStart == mSelectionEnd) {
            deleteSelection(mSelectionStart - 1, mSelectionStart);
        } else {
            deleteSelection(mSelectionStart, mSelectionEnd);
        }
        deleteSelection();
        if (draw) { requestDraw(); }
    }

    public void commandDelete(int num, boolean draw) {
        deleteSelection(mSelectionStart - num, mSelectionStart);
        if (draw) { requestDraw(); }
    }

    public void commandPaste(String content, boolean draw) {
        commandInput(content, draw);
    }

    public void commandEnter(boolean draw) {
        inputEnter();
        if (draw) { requestDraw(); }
    }

    public void commandInput(CharSequence content, boolean draw) {
        if (content.length() > 0) {
            StringBuilder contentNoEnter = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == NoteEditorRender.EndCodeChar) {
                    inputParagraph(contentNoEnter.toString());
                    inputEnter();
                    contentNoEnter.delete(0, contentNoEnter.length());
                } else {
                    contentNoEnter.append(c);
                }
            }
            if (contentNoEnter.length() > 0) {
                inputParagraph(contentNoEnter.toString());
            }
        }
        if (draw) { requestDraw(); }
    }

    private void inputEnter() {
        // 删除区间
        deleteSelection();

        int pos = mSelectionStart;
        // 获取段落
        Paragraph lastParagraph = getParagraph(pos);
        if (lastParagraph == null) {
            lastParagraph = createParagraph(0);
        }

        int lastBeginPos = getParagraphBegin(lastParagraph);
        int lastEndPos = getParagraphEnd(lastParagraph);
        int lastIndex = getParagraphIndex(lastParagraph);
        int newLineStyle = lastParagraph.getLineStyle();
        int newIndentation = lastParagraph.getIndentation();

        Paragraph newParagraph;
        if (lastBeginPos == mSelectionStart) {
            // 插入一个新段落
            createParagraph(lastIndex, newIndentation, 0);
            newParagraph = lastParagraph;
            // 计算新的选择区
            int newPos = getParagraphBegin(newParagraph);
            setSeletion(newPos);
        } else if (lastEndPos == mSelectionStart) {
            // 直接创建新段落
            newParagraph = createParagraph(lastIndex + 1, newIndentation, newLineStyle);
            // 计算新的选择区
            int newPos = getParagraphBeginWithHead(newParagraph);
            setSeletion(newPos);
        } else {
            // 分割成两部分
            int cutPos = pos - getParagraphBegin(lastParagraph);
            String newWords = lastParagraph.getWords(cutPos, lastParagraph.getLength());
            List<Integer> newWordStyles = new LinkedList<>(
                    lastParagraph.getWordStyles(cutPos, lastParagraph.getLength()));
            // 删除旧段落后半部分
            lastParagraph.remove(cutPos, lastParagraph.getLength());
            // 加入新段落
            newParagraph = createParagraph(lastIndex + 1, newIndentation, newLineStyle);
            newParagraph.addWords(newWords, newWordStyles);
            // 计算新的选择区
            int newPos = getParagraphBeginWithHead(newParagraph);
            setSeletion(newPos);
        }
    }

    private void inputParagraph(String content) {
        Log.e(TAG, "inputParagraph: " + content );
        if (content.isEmpty()) { return; }
        // 删除区间
        deleteSelection();

        int pos = mSelectionStart;
        // 获取段落
        Paragraph paragraph = getParagraph(pos);
        if (paragraph == null) {
            paragraph = createParagraph(0);
        } else if (paragraph.isDividingLine() || paragraph.isImage()) {
            int index = getParagraphIndex(paragraph);
            int paragraphBegin = getParagraphBegin(paragraph);
            if (paragraphBegin != pos) {
                index += 1;
            }
            paragraph = createParagraph(index, paragraph.getIndentation(), paragraph.getLineStyle());
            pos = getParagraphBeginWithHead(paragraph);
        }

        // 计算插入位置
        int begin = getParagraphBegin(paragraph);
        int insertPos = pos - begin;

        // 插入位置
        paragraph.insert(insertPos, content, mOptions);

        // 调整选择区
        setSeletion(pos + content.length());
    }

    private void inputDividingLine() {
        // 删除区间
        deleteSelection();

        int pos = mSelectionStart;
        Paragraph paragraph = getParagraph(pos);
        Paragraph dividingParagraph;
        if (paragraph == null) {
            dividingParagraph = createDividingParagraph(0);
        } else {
            int index = getParagraphIndex(paragraph);
            dividingParagraph = createDividingParagraph(index + 1);
        }
        pos = getParagraphEnd(dividingParagraph);
        setSeletion(pos);
    }

    private void inputBulletList(boolean isSelected) {
        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        if (paragraphs.isEmpty()) {
            Paragraph newParagraph = createParagraph(0);
            paragraphs.add(newParagraph);
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else {
            for (Paragraph paragraph : paragraphs) {
                paragraph.setBulletList(isSelected);
                if (isSelected) {
                    if (paragraph.insertPlaceHolder()) {
                        setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
                    }
                } else {
                    if (paragraph.removePlaceHolder()) {
                        setSeletion(mSelectionStart - 1, mSelectionEnd - 1);
                    }
                }
            }
        }
    }

    private void inputNumList(boolean isSelected) {
        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        if (paragraphs.isEmpty()) {
            Paragraph newParagraph = createParagraph(0);
            paragraphs.add(newParagraph);
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else {
            for (Paragraph paragraph : paragraphs) {
                paragraph.setNumList(isSelected);
                if (isSelected) {
                    if (paragraph.insertPlaceHolder()) {
                        setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
                    }
                } else {
                    if (paragraph.removePlaceHolder()) {
                        setSeletion(mSelectionStart - 1, mSelectionEnd - 1);
                    }
                }
            }
        }
    }

    private void inputUnCheckBox(boolean isSelected) {
        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        if (paragraphs.isEmpty()) {
            Paragraph newParagraph = createParagraph(0);
            paragraphs.add(newParagraph);
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else {
            for (Paragraph paragraph : paragraphs) {
                paragraph.setUnCheckbox(isSelected);
                paragraph.setCheckbox(false);
                if (isSelected) {
                    if (paragraph.insertPlaceHolder()) {
                        setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
                    }
                } else {
                    if (paragraph.removePlaceHolder()) {
                        setSeletion(mSelectionStart - 1, mSelectionEnd - 1);
                    }
                }
            }
        }

    }

    private void deleteSelection() {
        deleteSelection(mSelectionStart, mSelectionEnd);
    }

    private void deleteSelection(int start, int end) {
        if (start < 0 || start >= end) {
            return;
        }
        Paragraph firstParagraph = null;
        Iterator<Paragraph> iter = mNote.getParagraphs().iterator();

        int startPos = 0;
        int endPos = 0;
        while (iter.hasNext()) {
            Paragraph item = iter.next();
            endPos = startPos + item.getLength();
            if (start >= startPos && start <= endPos) { // first paragraph
                firstParagraph = item;
                int delParaStartPos = start - startPos;
                int delParaEndPos = end - startPos;
                if (delParaEndPos > item.getLength()) {
                    delParaEndPos = item.getLength();
                }
                item.remove(delParaStartPos, delParaEndPos);
                if (delParaStartPos <= 0 && delParaEndPos >= 1) {
                    item.clearHeadStyle();
                }
            } else if (start <= startPos && end >= endPos) { // middle paragraph
                iter.remove();

            } else if (end >= startPos && end <= endPos) { // last paragraph
                int demainParaStartPos = end - startPos;
                int demainParaEndPos = item.getLength();
                if (item.isHeadStyle() && demainParaStartPos <= 0 && demainParaEndPos >= 1) {
                    item.clearHeadStyle();
                    demainParaStartPos += 1;
                }
                if (demainParaStartPos < demainParaEndPos) {
                    String demainContent = item.getWords(demainParaStartPos, demainParaEndPos);
                    List<Integer> demainStyles = item.getWordStyles(demainParaStartPos, demainParaEndPos);
                    firstParagraph.addWords(demainContent, demainStyles);
                }
                iter.remove();
                break;
            }
            startPos = endPos + 1;
        }
        setSeletion(start);
    }

    private Paragraph getParagraph(int globalPos) {
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

    private List<Paragraph> getParagraphs(int globalStart, int globalEnd) {
        List<Paragraph> selectedParagraphs = new ArrayList<>();
        int startPos = 0;
        int endPos = 0;
        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            int rangeLeft = Math.max(globalStart, startPos);
            int rangeRight = Math.min(globalEnd, endPos);
            if (rangeLeft <= rangeRight) {
                // 交集
                selectedParagraphs.add(paragraph);
            }
            startPos = endPos + 1;
        }
        return selectedParagraphs;
    }

    private int getParagraphIndex(Paragraph paragraph) {
        return mNote.getParagraphs().indexOf(paragraph);
    }

    private int getParagraphBeginWithHead(Paragraph aim) {
        int pos = getParagraphBegin(aim);
        if (aim.isHeadStyle()) {
            pos++;
        }
        return pos;
    }

    private int getParagraphBegin(Paragraph aim) {
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

    private int getParagraphEnd(Paragraph aim) {
        int begin = getParagraphBegin(aim);
        return begin + aim.getLength();
    }

    private Paragraph createParagraph(int index) {
        return createParagraph(index, mOptions.getIndentation(), mOptions.getLineStyle());
    }

    private Paragraph createParagraph(int index, int indenteation, int lineStyle) {
        Paragraph paragraph = new Paragraph();
        paragraph.setDirty(true);
        paragraph.setIndentation(indenteation);
        paragraph.setLineStyle(lineStyle);
        paragraph.setDividingLine(false);
        paragraph.setImage(false);
        if (paragraph.isCheckbox()) {
            paragraph.setUnCheckbox(true);
        }
        if (paragraph.isHeadStyle()) {
            paragraph.insertPlaceHolder();
        }
        mNote.getParagraphs().add(index, paragraph);
        return paragraph;
    }

    private Paragraph createDividingParagraph(int index) {
        Paragraph paragraph = new Paragraph();
        paragraph.setDirty(true);
        paragraph.setLineStyle(mOptions.getLineStyle());
        paragraph.setIndentation(0);
        paragraph.setDividingLine(true);
        paragraph.setImage(false);
        paragraph.insertPlaceHolder();

        mNote.getParagraphs().add(index, paragraph);
        return paragraph;
    }

    private Paragraph createImageParagraph(int index) {
        return null;
    }

    public void setSeletion(int globalPos) {
        mSelectionStart = globalPos;
        mSelectionEnd = globalPos;
    }

    public void setSeletion(int globalStart, int globalEnd) {
        mSelectionStart = globalStart;
        mSelectionEnd = globalEnd;
    }

    public void onSelectionChanged(int selStart, int selEnd) {
        setSeletion(selStart, selEnd);
        mOptions = detectStyle(selStart, selEnd);
        notifySelectionChangeListener(selStart, selEnd, mOptions);
    }

    /**
     * 获取对应位置的样式：wordStyle 和 lineStyle
     * @param start
     * @param end
     * @return
     */
    private Options detectStyle(int start, int end) {
        int startPos = 0;
        int endPos;
        int lineStyle = -1;
        int indentation = -1;
        List<Integer> wordStyles = new ArrayList<>();

        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            int rangeLeft = Math.max(start, startPos);
            int rangeRight = Math.min(end, endPos);
            if (rangeLeft <= rangeRight) {
                // 计算交集的区间
                int rangeLeftPos = rangeLeft - startPos;
                int rangeRightPos = rangeRight - startPos;
                Log.e(TAG, "detectStyle: " + rangeLeftPos + " -> " + rangeRightPos );
                List<Integer> rangeWordStyles = paragraph.getWordStyles(rangeLeftPos, rangeRightPos);
                wordStyles.addAll(rangeWordStyles);
                if (lineStyle == -1) {
                    lineStyle = paragraph.getLineStyle();
                }
                if (indentation == -1) {
                    indentation = paragraph.getIndentation();
                }
            }
            startPos = endPos + 1;
        }
        lineStyle = Math.max(0, lineStyle);
        indentation = Math.max(0, indentation);
        return Options.getSameStyle(indentation, lineStyle, wordStyles);
    }

    /**
     * checkbox uncheckbox image uri
     * @param globalPos
     * @return
     */
    public boolean onSpanTouched(int globalPos) {
        Paragraph paragraph = getParagraph(globalPos);
        if (getParagraphBegin(paragraph) == globalPos ){
            return clickLineStyle(paragraph);
        }
        return false;
    }

    private boolean clickLineStyle(Paragraph paragraph) {
        if (paragraph.isCheckbox()) {
            return clickCheckBox(paragraph);
        } else if (paragraph.isUnCheckbox()) {
            return clickUncheckBox(paragraph);
        }
        return false;
    }

    private boolean clickCheckBox(Paragraph paragraph) {
        paragraph.setCheckbox(false);
        paragraph.setUnCheckbox(true);
        requestDraw();
        return true;
    }

    private boolean clickUncheckBox(Paragraph paragraph) {
        paragraph.setUnCheckbox(false);
        paragraph.setCheckbox(true);
        requestDraw();
        return true;
    }


    public void requestDraw() {
        print();
        mRender.draw(mEditorText, mNote.getParagraphs(), mSelectionStart, mSelectionEnd);
    }

    public void print() {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        Log.e(TAG, " count: " + paragraphs.size() +
                " selection: ( " + mSelectionStart + " , " + mSelectionEnd + " )");
        for (Paragraph paragraph: paragraphs) {
            int startPos = getParagraphBegin(paragraph);
            int endPos = getParagraphEnd(paragraph);
            Log.e(TAG, paragraph.getLineStyle() + " [ " + startPos + "->" + endPos + " ] " + paragraph.toString());
        }
    }

    public String getSelectionText() {
        return "test \n test";
    }
}
