package com.jscheng.srich.model;

import java.util.List; /**
 * Created By Chengjunsen on 2019/3/13
 */
public class ParagraphBuilder {

    private Paragraph paragraph;

    public ParagraphBuilder() {
        paragraph = new Paragraph();
        paragraph.setDirty(true);
    }

    public ParagraphBuilder lineStyle(int lineStyle) {
        paragraph.setLineStyle(lineStyle);
        if (paragraph.isCheckbox()) {
            paragraph.setUnCheckbox(true);
        }
        return this;
    }

    public Paragraph build() {
        if (paragraph.isHeadStyle() || paragraph.isParagraphStyle()) {
            paragraph.insertPlaceHolder();
        } else {
            paragraph.removePlaceHolder();
        }
        return paragraph;
    }

    public ParagraphBuilder indentation(int indentation) {
        paragraph.setIndentation(indentation);
        return this;
    }

    public ParagraphBuilder dividingLine(boolean dividingLine) {
        paragraph.setDividingLine(dividingLine);
        return this;
    }

    public ParagraphBuilder image(String url) {
        paragraph.setImage(url);
        paragraph.setImage(true);
        return this;
    }

    public ParagraphBuilder image(boolean b) {
        paragraph.setImage(b);
        return this;
    }

    public ParagraphBuilder bullet(boolean b) {
        paragraph.setBulletList(b);
        return this;
    }

    public ParagraphBuilder addWords(String words, List<Integer> wordStyles) {
        paragraph.addWords(words, wordStyles);
        return this;
    }

    public ParagraphBuilder numList(boolean b) {
        paragraph.setNumList(b);
        return this;
    }

    public ParagraphBuilder uncheckBox(boolean b) {
        paragraph.setUnCheckbox(b);
        return this;
    }

    public ParagraphBuilder checkbox(boolean b) {
        paragraph.setCheckbox(b);
        return this;
    }
}
