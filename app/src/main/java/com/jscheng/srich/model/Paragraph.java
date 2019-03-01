package com.jscheng.srich.model;

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

    private boolean isDividingLine;

    private boolean isImage;

    private boolean isDirty;

    public Paragraph() {
        this.wordStyles = new LinkedList<>();
        this.words = new StringBuilder();
        this.lineStyle = 0;
        this.indentation = 0;
        this.isDividingLine = false;
        this.isImage = false;
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
    }

    public void add(CharSequence content, Options options) {
        for (int i = 0; i < content.length(); i++) {
            add(content.charAt(i), options);
        }
    }

    public void insert(int pos, char c, Options options) {
        words.insert(pos, c);
        wordStyles.add(pos, options.getWordStyle());
    }

    public void remove(int start, int end) {
        words.delete(start, end);
        for (int i = start; i < end; i++) {
            wordStyles.remove(start);
        }
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

    public StringBuilder getWords() {
        return words;
    }

    public boolean isNull() {
        return getLength() == 0;
    }

    public boolean isDividingLine() {
        return isDividingLine;
    }

    public void setDividingLine(boolean dividingLine) {
        isDividingLine = dividingLine;
    }

    public boolean isImage() {
        return isImage;
    }
}
