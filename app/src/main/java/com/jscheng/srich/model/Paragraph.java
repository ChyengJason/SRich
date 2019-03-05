package com.jscheng.srich.model;

import com.jscheng.srich.editor.NoteEditorRender;

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

    public Paragraph() {
        this.wordStyles = new LinkedList<>();
        this.words = new StringBuilder();
        this.lineStyle = 0;
        this.indentation = 0;
        this.isDirty = true;
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
            words.insert(0, NoteEditorRender.PlaceHoldChar);
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
       return words.length() > 0 && words.substring(0, 1).equals(NoteEditorRender.PlaceHoldChar);
    }

    public void remove(int start, int end) {
        removeWords(start, end);
        removeWordStyles(start, end);
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

    public void setWords(String words, List<Integer> wordStyles) {
        if (words != null && wordStyles != null && !words.isEmpty() && !wordStyles.isEmpty()) {
            this.words = new StringBuilder(words);
            this.wordStyles = new LinkedList<>(wordStyles);
        }
    }

    public void addWords(String words, List<Integer> wordStyles) {
        if (words != null && wordStyles != null && !words.isEmpty() && !wordStyles.isEmpty()) {
            this.words.append(words);
            this.wordStyles.addAll(wordStyles);
        }
    }

    public String getWords() {
        return words.toString();
    }

    public String getWords(int start, int end) {
        return words.subSequence(start, end).toString();
    }

    public char getWord(int pos) {
        return words.charAt(pos);
    }

    private void removeWords(int start, int end) {
        words.delete(start, end);
    }

    private void removeWord(int pos) {
        words.deleteCharAt(pos);
    }

    public List<Integer> getWordStyles() {
        return wordStyles;
    }

    public List<Integer> getWordStyles(int start, int end) {
        return wordStyles.subList(start, end);
    }

    private void removeWordStyles(int start, int end) {
        for (int i = start; i < end; i++) {
            wordStyles.remove(start);
        }
    }

    private void removeWordStyle(int pos) {
        wordStyles.remove(pos);
    }

    public int getWordStyle(int pos) {
        return wordStyles.get(pos);
    }

    public boolean isNull() {
        return getLength() == 0;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder(words);
        return content.toString();
    }

    public boolean isDividingLine() {
        return Style.isStyle(lineStyle, Style.DividingLine);
    }

    public void setDividingLine(boolean dividingLine) {
        lineStyle = Style.setLineStyle(lineStyle, dividingLine, Style.DividingLine);
    }

    public boolean isImage() {
        return Style.isStyle(lineStyle, Style.Image);
    }

    public void setImage(boolean image) {
        lineStyle = Style.setLineStyle(lineStyle, image, Style.Image);
    }

    public boolean isBulletList() {
        return Style.isStyle(lineStyle, Style.BulletList);
    }

    public void setBulletList(boolean bullet) {
        lineStyle = Style.setLineStyle(lineStyle, bullet, Style.BulletList);
    }

    public boolean isNumList() {
        return Style.isStyle(lineStyle, Style.NumList);
    }

    public void setNumList(boolean numlist) {
        lineStyle = Style.setLineStyle(lineStyle, numlist, Style.NumList);
    }

    public boolean isCheckbox() {
        return Style.isStyle(lineStyle, Style.CheckBox);
    }

    public void setCheckbox(boolean numlist) {
        lineStyle = Style.setLineStyle(lineStyle, numlist, Style.CheckBox);
    }

    public boolean isHeadStyle() {
        return isBulletList() || isNumList() || isCheckbox();
    }
}
