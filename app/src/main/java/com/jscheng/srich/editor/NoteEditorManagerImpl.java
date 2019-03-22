package com.jscheng.srich.editor;

import android.util.Log;

import com.jscheng.srich.image_loader.ImageLoader;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.NoteBuilder;
import com.jscheng.srich.model.NoteSnap;
import com.jscheng.srich.model.Options;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.ParagraphBuilder;
import com.jscheng.srich.model.Style;
import com.jscheng.srich.revoke.NoteRevocationManager;

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
    private List<NoteEditorClickListener> mClickListeners;
    private NoteEditorText mEditorText;
    private NoteEditorRender mRender;
    private NoteRevocationManager mRevocationManager;
    private int mSelectionStart;
    private int mSelectionEnd;

    public NoteEditorManagerImpl(NoteEditorText editorText) {
        mNote = new NoteBuilder().build();
        mRender = new NoteEditorRender(editorText);
        mOptions = new Options();
        mSelectionListeners = new ArrayList<>();
        mClickListeners = new ArrayList<>();
        mEditorText = editorText;
        mRevocationManager = new NoteRevocationManager();
    }

    public void apply(Note note) {
        mNote = note;
        mSelectionStart = mSelectionEnd = 0;
        requestDraw();
    }

    public void apply(NoteSnap noteSnap) {
        mNote.setParagraphs(noteSnap.getParagraphs());
        mSelectionStart = noteSnap.getSelectionStart();
        mSelectionEnd = noteSnap.getSelectionEnd();
        requestDraw();
    }

    public void apply(Note note, int selectionStart, int selectionEnd) {
        mNote = note;
        mSelectionStart = selectionStart;
        mSelectionEnd = selectionEnd;
        requestDraw();
    }

    public void addSelectionChangeListener(NoteEditorSelectionListener listener) {
        if (!mSelectionListeners.contains(listener)) {
            mSelectionListeners.add(listener);
        }
        notifySelectionChangeListener(mSelectionStart, mSelectionEnd, mOptions);
    }

    public void removeSelectionChangeListener(NoteEditorSelectionListener listener) {
        mSelectionListeners.remove(listener);
    }

    private void notifySelectionChangeListener(int start, int end, Options options) {
        if (mSelectionListeners != null) {
            for (NoteEditorSelectionListener listener : mSelectionListeners) {
                listener.onStyleChange(start, end, options);
            }
        }
    }

    public void addClickListener(NoteEditorClickListener listener) {
        mClickListeners.add(listener);
    }

    private void notifyClickImageListener(Paragraph paragraph) {
        int index = 0;
        List<String> list = new ArrayList<>();
        for (Paragraph item : mNote.getParagraphs()) {
            if (item.isImage()) {
                list.add(item.getImageUrl());
                if (item == paragraph) {
                    index = list.size() - 1;
                }
            }
        }
        for (NoteEditorClickListener listener: mClickListeners) {
            listener.onClickImage(list, index);
        }
    }

    public void inputColor(boolean isSelected, boolean draw) {
        mOptions.setColor(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.BackgroudColor);
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputUnderline(boolean isSelected, boolean draw) {
        mOptions.setUnderline(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.UnderLine);
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputItalic(boolean isSelected, boolean draw) {
        mOptions.setItalic(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Italic);
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputBold(boolean isSelected, boolean draw) {
        mOptions.setBold(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Bold);
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputSuperscript(boolean isSelected, boolean draw) {
        mOptions.setSuperScript(isSelected);
        if (isSelected) {
            mOptions.setSubScript(false);
        }
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.SuperScript);
            if (isSelected) {
                applySelectionWordStyle(mSelectionStart, mSelectionEnd, false, Style.SubScript);
            }
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputSubscript(boolean isSelected, boolean draw) {
        mOptions.setSubScript(isSelected);
        if (isSelected) {
            mOptions.setSuperScript(false);
        }
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.SubScript);
            if (isSelected) {
                applySelectionWordStyle(mSelectionStart, mSelectionEnd, false, Style.SuperScript);
            }
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputStrikeThrough(boolean isSelected, boolean draw) {
        mOptions.setStrikethrough(isSelected);
        if (mSelectionEnd > mSelectionStart) {
            beginRetrokeAction(false);
            applySelectionWordStyle(mSelectionStart, mSelectionEnd, isSelected, Style.Strikethrough);
            if (draw) { requestDraw(); }
            endRetrokeAction();
        }
    }

    public void inputEnter() {
        beginRetrokeAction(false);
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
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputParagraph(String content) {
        beginRetrokeAction(false);
        Log.e(TAG, "inputParagraph: " + content );
        if (content.isEmpty()) { return; }
        // 删除区间
        deleteSelection(mSelectionStart, mSelectionEnd);

        int pos = mSelectionStart;
        // 获取段落
        Paragraph paragraph = getParagraph(pos);
        if (paragraph == null) {
            paragraph = createParagraph(0);
        } else if (paragraph.isParagraphStyle()) {
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
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputDividingLine() {
        beginRetrokeAction(false);
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
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputBulletList(boolean isSelected) {
        beginRetrokeAction(false);
        mOptions.setBulletList(isSelected);

        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        if (paragraphs.isEmpty()) {
            Paragraph newParagraph = createParagraph(0);
            paragraphs.add(newParagraph);
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else {
            for (Paragraph paragraph : paragraphs) {
                paragraph.setBulletList(isSelected);
                checkPlaceHolder(paragraph);
            }
        }
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputImage(String url) {
        beginRetrokeAction(false);
        int pos = mSelectionStart;
        ImageLoader.with(mEditorText.getContext()).load(url);

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
        endRetrokeAction();
    }

    public void inputNumList(boolean isSelected) {
        beginRetrokeAction(false);
        mOptions.setNumList(isSelected);

        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        if (paragraphs.isEmpty()) {
            Paragraph newParagraph = createParagraph(0);
            paragraphs.add(newParagraph);
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else {
            for (Paragraph paragraph : paragraphs) {
                paragraph.setNumList(isSelected);
                checkPlaceHolder(paragraph);
            }
        }
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputUnCheckBox(boolean isSelected) {
        beginRetrokeAction(false);
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
                checkPlaceHolder(paragraph);
            }
        }
        mNote.setDirty(true);
        endRetrokeAction();
    }

    public void inputIndentation() {
        beginRetrokeAction(false);
        int indentatin = Math.min(3, mOptions.getIndentation() + 1);
        mOptions.setIndentation(indentatin);
        applySelectionIndentation();
        endRetrokeAction();
    }

    private boolean applySelectionWordStyle(int start, int end, boolean isAppend, int flag) {
        if (start == end) {
            return false;
        }
        int startPos = 0;
        int endPos = 0;

        for (Paragraph paragraph : mNote.getParagraphs()) {
            endPos = startPos + paragraph.getLength();
            int rangeLeft = Math.max(start, startPos);
            int rangeRight = Math.min(end, endPos);
            // 有交集
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

    public void inputReduceIndentation() {
        beginRetrokeAction(false);
        int indentatin = Math.min(0, mOptions.getIndentation() - 1);
        mOptions.setIndentation(indentatin);
        applySelectionReduceIndentation();
        endRetrokeAction();
    }

    public void applySelectionIndentation() {
        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        for (Paragraph paragraph: paragraphs) {
            if (paragraph.isParagraphStyle()) {
                continue;
            }
            int indentation = Math.min(3, paragraph.getIndentation() + 1);
            paragraph.setIndentation(indentation);
            checkPlaceHolder(paragraph);
            paragraph.setDirty(true);
        }
        mNote.setDirty(true);
    }

    public void applySelectionReduceIndentation() {
        List<Paragraph> paragraphs = getParagraphs(mSelectionStart, mSelectionEnd);
        for (Paragraph paragraph: paragraphs) {
            if (paragraph.isParagraphStyle()) {
                continue;
            }
            int indentation = Math.max(0, paragraph.getIndentation() - 1);
            paragraph.setIndentation(indentation);
            paragraph.setDirty(true);
            checkPlaceHolder(paragraph);
        }
        mNote.setDirty(true);
    }

    public void inputDeleteSelection() {
        if (mSelectionStart < mSelectionEnd) {
            beginRetrokeAction(false);
            deleteSelection(mSelectionStart, mSelectionEnd);
            endRetrokeAction();
        }
    }

    public void inputDeleteSelection(int num) {
        if (mSelectionStart >= num) {
            beginRetrokeAction(num == 1);
            deleteSelection(mSelectionStart - num, mSelectionStart);
            endRetrokeAction();
        }
    }

    public void inputDelete() {
        if (mSelectionStart >= 1) {
            beginRetrokeAction(true);
            deleteSelection(mSelectionStart - 1, mSelectionStart);
            endRetrokeAction();
        }
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

            int delParaStartPos = start - startPos;
            int delParaEndPos = end - startPos;
            if (delParaEndPos > item.getLength()) {
                delParaEndPos = item.getLength();
            }

            if (start >= startPos && start <= endPos) {
                // 第一段
                firstParagraph = item;
                item.remove(delParaStartPos, delParaEndPos);
                if (delParaStartPos <= 0 && delParaEndPos >= 1) {
                    item.clearHeadStyle();
                    item.clearParagraphStyle();
                }
                item.setDirty(true);
            } else if (start <= startPos && end >= endPos) {
                // 中间段
                iter.remove();

            } else if (end >= startPos && end <= endPos) {
                // 末尾段
                int demainParaStartPos = end - startPos;
                int demainParaEndPos = item.getLength();
                if (item.isParagraphStyle()) {
                    break;
                }
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

    private void checkPlaceHolder(Paragraph paragraph) {
        if (paragraph.isHeadStyle() && paragraph.insertPlaceHolder()) {
            setSeletion(mSelectionStart + 1, mSelectionEnd + 1);
        } else if (!paragraph.isHeadStyle() && paragraph.removePlaceHolder()) {
            setSeletion(mSelectionStart - 1, mSelectionEnd - 1);
        }
    }

    private Paragraph createParagraph(int index) {
        return createParagraph(index, mOptions.getIndentation(), mOptions.getLineStyle());
    }

    private Paragraph createParagraph(int index, int indentation, int lineStyle) {
        Paragraph newParagraph = new ParagraphBuilder()
                .lineStyle(lineStyle)
                .indentation(indentation)
                .image(false)
                .dividingLine(false)
                .build();
        mNote.getParagraphs().add(index, newParagraph);
        return newParagraph;
    }

    private Paragraph createDividingParagraph(int index) {
        Paragraph newParagraph = new ParagraphBuilder()
                .dividingLine(true)
                .image(false)
                .build();
        mNote.getParagraphs().add(index, newParagraph);
        return newParagraph;
    }

    private Paragraph createImageParagraph(int index, String url) {
        Paragraph newParagraph = new ParagraphBuilder()
                .image(url)
                .build();
        mNote.getParagraphs().add(index, newParagraph);
        return newParagraph;
    }

    private void setSeletion(int globalPos) {
        mSelectionStart = globalPos;
        mSelectionEnd = globalPos;
    }

    private void setSeletion(int globalStart, int globalEnd) {
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
        boolean canRestroke = mRevocationManager.isCanRevoke();
        boolean canRecover = mRevocationManager.isCanRecover();

        return Options.getSameStyle(canRestroke, canRecover, indentation, lineStyle, wordStyles);
    }

    public boolean onSpanTouchUp(int globalPos) {
        Paragraph paragraph = getParagraph(globalPos);
        int begin = getParagraphBegin(paragraph);
        if (globalPos == begin) {
            if (paragraph.isCheckbox()) {
                clickCheckBox(paragraph);
                return true;
            } else if (paragraph.isUnCheckbox()) {
                clickUncheckBox(paragraph);
                return true;
            }
        }
        if ( globalPos <= begin + 1) {
            if (paragraph.isImage()) {
                clickImage(paragraph);
                return false;
            }
        }
        return false;
    }

    private void clickCheckBox(Paragraph paragraph) {
        paragraph.setCheckbox(false);
        paragraph.setUnCheckbox(true);
        mNote.setDirty(true);
        requestDraw();
    }

    private void clickUncheckBox(Paragraph paragraph) {
        paragraph.setUnCheckbox(false);
        paragraph.setCheckbox(true);
        mNote.setDirty(true);
        requestDraw();
    }

    private void clickImage(Paragraph paragraph) {
        notifyClickImageListener(paragraph);
    }

    public void requestDraw() {
        checkLastParagraph();
        mEditorText.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, toString());
                mRender.draw(mEditorText, mNote.getParagraphs(), mSelectionStart, mSelectionEnd);
            }
        });
    }

    public NoteSnap recover() {
        return mRevocationManager.recover(mNote, mSelectionStart, mSelectionEnd);
    }

    public NoteSnap revoke() {
        return mRevocationManager.revoke(mNote, mSelectionStart, mSelectionEnd);
    }

    /**
     * 保证最后一段为空段落
     */
    private void checkLastParagraph() {
        List<Paragraph> paragraphs = mNote.getParagraphs();
        if (paragraphs.isEmpty() || !paragraphs.get(paragraphs.size() - 1).isNull()) {
            Paragraph newParagraph = new ParagraphBuilder().build();
            mNote.getParagraphs().add(newParagraph);
        }
    }

    private void beginRetrokeAction(boolean isContinous) {
        mRevocationManager.beginAction(mNote, mSelectionStart, mSelectionEnd, isContinous);
    }

    private void endRetrokeAction() {
        mRevocationManager.endAction();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Paragraph> paragraphs = mNote.getParagraphs();
        stringBuilder.append(" count: ")
                .append(paragraphs.size())
                .append(" selection: ( ")
                .append(mSelectionStart)
                .append(" , ")
                .append(mSelectionEnd)
                .append(" )");

        for (Paragraph paragraph: paragraphs) {
            int startPos = getParagraphBegin(paragraph);
            int endPos = getParagraphEnd(paragraph);
            stringBuilder.append(paragraph.getLineStyle())
                    .append(" [ ")
                    .append(startPos)
                    .append("->")
                    .append(endPos)
                    .append(" ] ")
                    .append(paragraph.toString());
        }
        return stringBuilder.toString();
    }

    public String getSelectionText() {
        return "test \n test";
    }
}
