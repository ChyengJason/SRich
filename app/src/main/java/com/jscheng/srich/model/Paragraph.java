package com.jscheng.srich.model;

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

    public Paragraph() {
        this.wordStyles = new LinkedList<>();
        this.words = new StringBuilder();
        this.lineStyle = 0;
        this.indentation = 0;
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

    public void insert(int pos, char c, Options options) {
        words.insert(pos, c);
        wordStyles.add(pos, options.getWordStyle());
    }

    public void remove(int pos, int offset) {
        words.delete(pos, pos + offset);
        for (int i = pos; i < pos + offset; i++) {
            wordStyles.remove(pos);
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
}
