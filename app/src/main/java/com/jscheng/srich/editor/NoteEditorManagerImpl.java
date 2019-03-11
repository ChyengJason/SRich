package com.jscheng.srich.editor;

import android.net.Uri;
import android.util.Log;

import com.jscheng.srich.image_loader.NoteImagePool;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.Options;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public class NoteEditorManagerImpl {
    private final static String TAG = "NoteEditorManager";
    private Note mNote;
    private Options mOptions;
    private List<NoteEditorSelectionListener> mSelectionListeners;
    private NoteEditorText mEditorText;
    private NoteEditorRender mRender;
    private int mSelectionStart;
    private int mSelectionEnd;

    public NoteEditorManagerImpl(NoteEditorText editorText) {
        mNote = new Note();
        mRender = new NoteEditorRender(editorText);
        mOptions = new Options();
        mSelectionListeners = new ArrayList<>();
        mEditorText = editorText;
    }

    public void reset(Note note) {
        mNote = note;
        mSelectionStart = mSelectionEnd = 0;
        requestDraw();
    }

    public void addSelectionChangeListener(NoteEditorSelectionListener listener) {
        mSelectionListeners.add(listener);
        notifySelectionChangeListener(mSelectionStart, mSelectionEnd, mOptions);
    }

    public void notifySelectionChangeListener(int start, int end, Options options) {
        if (mSelectionListeners != null) {
            for (NoteEditorSelectionListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void inputColor(boolean isSelected) {
        mOptions.setColor(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.BackgroudColor);
        }
    }

    public void inputUnderline(boolean isSelected) {
        mOptions.setUnderline(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.UnderLine);
        }
    }

    public void inputItalic(boolean isSelected) {
        mOptions.setItalic(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Italic);
        }
    }

    public void inputBold(boolean isSelected) {
        mOptions.setBold(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Bold);
        }
    }

    public void inputSuperscript(boolean isSelected) {
        mOptions.setSuperScript(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.SuperScript);
        }
    }

    public void inputSubscript(boolean isSelected) {
        mOptions.setSubScript(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.SubScript);
        }
    }

    public void inputStrikeThrough(boolean isSelected) {
        mOptions.setStrikethrough(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            applySelectionStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Strikethrough);
        }
    }


    public void inputEnter() {
        // 删除区间
        deleteSelection(mSelectionStart, mSelectionEnd);

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
            if (lastParagraph.isHeadStyle() && lastEndPos == lastBeginPos + 1) {
                // 相当于删除样式
                lastParagraph.clearHeadStyle();
                lastParagraph.remove(0, 1);
                setSeletion(lastBeginPos);
            } else {
                // 直接创建新段落
                newParagraph = createParagraph(lastIndex + 1, newIndentation, newLineStyle);
                // 计算新的选择区
                int newPos = getParagraphBeginWithHead(newParagraph);
                setSeletion(newPos);
            }
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

    public void inputParagraph(String content) {
        Log.e(TAG, "inputParagraph: " + content );
        if (content.isEmpty()) { return; }
        // 删除区间
        deleteSelection(mSelectionStart, mSelectionEnd);

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

    public void inputDividingLine() {
        // 删除区间
        deleteSelection(mSelectionStart, mSelectionEnd);

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

    public void inputBulletList(boolean isSelected) {
        mOptions.setBulletList(isSelected);

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

    public void inputImage(String url) {
        int pos = mSelectionStart;
        NoteImagePool.getInstance(mEditorText.getContext()).loadBitmap(url);

        // 获取段落
        Paragraph lastParagraph = getParagraph(pos);
        Paragraph newParagraph;
        if (lastParagraph == null) {
            newParagraph = createImageParagraph(0, url);
        }else {
            int index = getParagraphIndex(lastParagraph);
            newParagraph = createImageParagraph(index + 1, url);
        }
        setSeletion(getParagraphEnd(newParagraph));
        mNote.setDirty(true);
    }

    public void inputNumList(boolean isSelected) {
        mOptions.setNumList(isSelected);

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
        mNote.setDirty(true);
    }

    public void inputUnCheckBox(boolean isSelected) {
        if (isSelected) {
            mOptions.setUnCheckBox(true);
            mOptions.setCheckBox(false);
        } else {
            mOptions.setUnCheckBox(false);
            mOptions.setCheckBox(false);
        }

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
        mNote.setDirty(true);
    }

    public boolean applySelectionStyle(int start, int end, boolean isAppend, int flag) {
        if (start == end) {
            return false;
        }
        int startPos = 0;
        int endPos = 0;
        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            int rangeLeft = Math.max(start, startPos);
            int rangeRight = Math.min(end, endPos);
            if (rangeLeft <= rangeRight) {
                int rangeLeftPos = rangeLeft - startPos;
                int rangeRightPos = rangeRight - startPos;
                for (int i = rangeLeftPos; i < rangeRightPos; i++) {
                    if (isAppend) {
                        paragraph.appendWordStyle(i, flag);
                    } else {
                        paragraph.removeWordStyle(i, flag);
                    }
                }
            }
            startPos = endPos + 1;
        }
        mNote.setDirty(true);
        return true;
    }

    public void inputIndentation() {
        int indentatin = Math.min(3, mOptions.getIndentation() + 1);
        mOptions.setIndentation(indentatin);
        applySelectionIndentation(mSelectionStart, mSelectionEnd);
    }

    public void applySelectionIndentation(int start, int end) {
        List<Paragraph> paragraphs = getParagraphs(start, end);
        for (Paragraph paragraph: paragraphs) {
            if (paragraph.isDividingLine() || paragraph.isImage()) {
                continue;
            }
            int indentation = Math.min(3, paragraph.getIndentation() + 1);
            paragraph.setIndentation(indentation);
            paragraph.setDirty(true);
        }
        mNote.setDirty(true);
    }

    public void inputReduceIndentation() {
        int indentatin = Math.min(0, mOptions.getIndentation() - 1);
        mOptions.setIndentation(indentatin);
        applySelectionReduceIndentation(mSelectionStart, mSelectionEnd);
    }

    public void applySelectionReduceIndentation(int start, int end) {
        List<Paragraph> paragraphs = getParagraphs(start, end);
        for (Paragraph paragraph: paragraphs) {
            if (paragraph.isDividingLine() || paragraph.isImage()) {
                continue;
            }
            int indentation = Math.max(0, paragraph.getIndentation() - 1);
            paragraph.setIndentation(indentation);
            paragraph.setDirty(true);
        }
        mNote.setDirty(true);
    }

    public void deleteSelection() {
        if (mSelectionStart == mSelectionEnd) {
            deleteSelection(mSelectionStart - 1, mSelectionStart);
        } else {
            deleteSelection(mSelectionStart, mSelectionEnd);
        }
    }

    public void deleteSelection(int num) {
        deleteSelection(mSelectionStart - num, mSelectionStart);
    }

    public void deleteSelection(int start, int end) {
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
        mNote.setDirty(true);
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

    public List<Paragraph> getParagraphs(int globalStart, int globalEnd) {
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

    public int getParagraphIndex(Paragraph paragraph) {
        return mNote.getParagraphs().indexOf(paragraph);
    }

    public int getParagraphBeginWithHead(Paragraph aim) {
        int pos = getParagraphBegin(aim);
        if (aim.isHeadStyle()) {
            pos++;
        }
        return pos;
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
        paragraph.setImage(false);
        if (paragraph.isCheckbox()) {
            paragraph.setUnCheckbox(true);
        }
        if (paragraph.isHeadStyle()) {
            paragraph.insertPlaceHolder();
        }
        mNote.getParagraphs().add(index, paragraph);
        mNote.setDirty(true);
        return paragraph;
    }

    public Paragraph createDividingParagraph(int index) {
        Paragraph paragraph = new Paragraph();
        paragraph.setDirty(true);
        paragraph.setLineStyle(mOptions.getLineStyle());
        paragraph.setIndentation(0);
        paragraph.setDividingLine(true);
        paragraph.setImage(false);
        paragraph.insertPlaceHolder();

        mNote.getParagraphs().add(index, paragraph);
        mNote.setDirty(true);
        return paragraph;
    }

    public Paragraph createImageParagraph(int index, String url) {
        Paragraph paragraph = new Paragraph();
        paragraph.setDirty(true);
        paragraph.setIndentation(0);
        paragraph.setLineStyle(0);
        paragraph.setImage(url);
        paragraph.insertPlaceHolder();
        mNote.getParagraphs().add(index, paragraph);
        mNote.setDirty(true);
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
    public Options detectStyle(int start, int end) {
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

    public boolean onSpanTouchDown(int globalPos) {
        Paragraph paragraph = getParagraph(globalPos);
        if (getParagraphBegin(paragraph) == globalPos) {
            if (paragraph.isImage()) {
                return true;
            } else if (paragraph.isCheckbox()) {
                clickCheckBox(paragraph);
                return true;
            } else if (paragraph.isUnCheckbox()) {
                clickUncheckBox(paragraph);
                return true;
            }
        }
        return false;
    }

    public boolean onSpanTouchUp(int globalPos, long interval) {
        Paragraph paragraph = getParagraph(globalPos);
        if (getParagraphBegin(paragraph) == globalPos) {
            if (paragraph.isImage()) {
                clickImage(paragraph);
                return true;
            } else if (paragraph.isCheckbox()) {
                return true;
            } else if (paragraph.isUnCheckbox()) {
                return true;
            }
        }
        return false;
    }

    public void clickCheckBox(Paragraph paragraph) {
        paragraph.setCheckbox(false);
        paragraph.setUnCheckbox(true);
        mNote.setDirty(true);
        requestDraw();
    }

    public void clickUncheckBox(Paragraph paragraph) {
        paragraph.setUnCheckbox(false);
        paragraph.setCheckbox(true);
        mNote.setDirty(true);
        requestDraw();
    }

    public void clickImage(Paragraph paragraph) {

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
