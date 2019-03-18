package com.jscheng.srich.model;

import android.text.TextUtils;

import com.jscheng.srich.editor.NoteEditorConfig;
import com.jscheng.srich.editor.NoteEditorRender;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/28
 */
public class Paragraph {

    private StringBuilder words;

    private List<Integer> wordStyles;

    private int lineStyle;

    private int indentation;

    private boolean isDirty;

    private String imageUrl;

    public Paragraph() {
        this.wordStyles = new LinkedList<>();
        this.words = new StringBuilder();
        this.lineStyle = 0;
        this.indentation = 0;
        this.isDirty = true;
    }

    @Override
    public Paragraph clone() {
        Paragraph paragraph = new Paragraph();
        paragraph.words.append(words);
        paragraph.wordStyles.addAll(wordStyles);
        paragraph.setImage(imageUrl);
        paragraph.lineStyle = lineStyle;
        paragraph.indentation = indentation;

        return paragraph;
    }

    public int getLength() {
        return words.length();
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void add(char c, Options options) {
        words.append(c);
        wordStyles.add(options.getWordStyle());
        setDirty(true);
    }

    public void add(CharSequence content, Options options) {
        for (int i = 0; i < content.length(); i++) {
            add(content.charAt(i), options);
        }
    }

    public void insert(int pos, char c, Options options) {
        words.insert(pos, c);
        wordStyles.add(pos, options.getWordStyle());
        setDirty(true);
    }

    public void insert(int pos, String content, Options options) {
        words.insert(pos, content);
        for (int i = pos; i < pos + content.length(); i++) {
            wordStyles.add(i, options.getWordStyle());
        }
        setDirty(true);
    }

    public boolean insertPlaceHolder() {
        setDirty(true);
        if (!isPlaceHolder()) {
            words.insert(0, NoteEditorConfig.PlaceHoldChar);
            wordStyles.add(0, 0);
            return true;
        }
        return false;
    }

    public boolean removePlaceHolder() {
        setDirty(true);
        if (isPlaceHolder()) {
            words.delete(0, 1);
            wordStyles.remove(0);
            return true;
        }
        return false;
    }

    public boolean isPlaceHolder() {
       return words.length() > 0 && words.substring(0, 1).equals(NoteEditorConfig.PlaceHoldChar);
    }

    public void remove(int start, int end) {
        words.delete(start, end);
        for (int i = start; i < end; i++) {
            wordStyles.remove(start);
        }
        setDirty(true);
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public void addWords(String words, List<Integer> wordStyles) {
        if (words == null || wordStyles == null) {
            return;
        }
        if (words.isEmpty() || wordStyles.isEmpty()) {
            return;
        }
        if (words.length() != wordStyles.size()) {
            return;
        }
        this.words.append(words);
        this.wordStyles.addAll(wordStyles);
    }

    public String getWords() {
        return words.toString();
    }

    public String getWords(int start, int end) {
        return words.subSequence(start, end).toString();
    }

    public String getWords(int start) {
        return words.substring(start);
    }

    public List<Integer> getWordStyles() {
        return wordStyles;
    }

    public List<Integer> getWordStyles(int start, int end) {
        List<Integer> results = new ArrayList<>();
        if (start == end) {
            if (start > 0) {
                results.add(wordStyles.get(start - 1));
            }
        } else {
            results.addAll(wordStyles.subList(start, end));
        }
        return results;
    }

    public void appendWordStyle(int pos, int flag) {
        int wordStyle = wordStyles.get(pos);
        wordStyle = Style.setWordStyle(wordStyle, true, flag);
        wordStyles.set(pos, wordStyle);
    }

    public void removeWordStyle(int pos, int flag) {
        int wordStyle = wordStyles.get(pos);
        wordStyle = Style.setWordStyle(wordStyle, false, flag);
        wordStyles.set(pos, wordStyle);
    }

    public boolean isNull() {
        return getLength() == 0;
    }

    @Override
    public String toString() {
        if (isImage()) {
            return "[img] " + getImageUrl();
        }
        if (isDividingLine()) {
            return "[dividingline]";
        }
        StringBuilder content = new StringBuilder(words);
        return content.toString();
    }

    public boolean isDividingLine() {
        return Style.isLineStyle(lineStyle, Style.DividingLine);
    }

    public void setDividingLine(boolean dividingLine) {
        lineStyle = Style.setLineStyle(lineStyle, dividingLine, Style.DividingLine);
    }

    public boolean isImage() {
        return Style.isLineStyle(lineStyle, Style.Image);
    }

    public void setImage(String url) {
        imageUrl = url;
        lineStyle = Style.setLineStyle(lineStyle, !TextUtils.isEmpty(url), Style.Image);
    }

    public void setImage(boolean image) {
        lineStyle = Style.setLineStyle(lineStyle, image, Style.Image);
    }

    public boolean isBulletList() {
        return Style.isLineStyle(lineStyle, Style.BulletList);
    }

    public void setBulletList(boolean bullet) {
        lineStyle = Style.setLineStyle(lineStyle, bullet, Style.BulletList);
    }

    public boolean isNumList() {
        return Style.isLineStyle(lineStyle, Style.NumList);
    }

    public void setNumList(boolean numlist) {
        lineStyle = Style.setLineStyle(lineStyle, numlist, Style.NumList);
    }

    public boolean isCheckbox() {
        return Style.isLineStyle(lineStyle, Style.CheckBox);
    }

    public void setCheckbox(boolean checkbox) {
        lineStyle = Style.setLineStyle(lineStyle, checkbox, Style.CheckBox);
    }

    public boolean isUnCheckbox() {
        return Style.isLineStyle(lineStyle, Style.UnCheckBox);
    }

    public void setUnCheckbox(boolean checkbox) {
        lineStyle = Style.setLineStyle(lineStyle, checkbox, Style.UnCheckBox);
    }

    public boolean isHeadStyle() {
        return isBulletList() || isNumList() || isCheckbox() || isUnCheckbox() || getIndentation() > 0;
    }

    public boolean isParagraphStyle() {
        return isImage() || isDividingLine();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void clearHeadStyle() {
        setBulletList(false);
        setNumList(false);
        setCheckbox(false);
        setUnCheckbox(false);
    }

    public void clearParagraphStyle() {
        setImage(false);
        setDividingLine(false);
    }
}
