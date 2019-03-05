package com.jscheng.srich.model;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/26
 */
public class Options {

    private int wordStyle;

    private int lineStyle;

    private int indentation;

    public Options() {
        this.wordStyle = 0;
        this.lineStyle = 0;
        this.indentation = 0;
    }

    public Options(int wordStyle, int lineStyle, int indentation) {
        this.wordStyle = wordStyle;
        this.lineStyle = lineStyle;
        this.indentation = indentation;
    }

    public boolean isBold() {
        return Style.isStyle(wordStyle, Style.Bold);
    }

    public void setBold(boolean bold) {
        wordStyle = Style.setWordStyle(wordStyle, bold, Style.Bold);
    }

    public boolean isItalic() {
        return Style.isStyle(wordStyle, Style.Italic);
    }

    public void setItalic(boolean italic) {
        wordStyle = Style.setWordStyle(wordStyle, italic, Style.Italic);
    }

    public boolean isUnderline() {
        return Style.isStyle(wordStyle, Style.UnderLine);
    }

    public void setUnderline(boolean underline) {
        wordStyle = Style.setWordStyle(wordStyle, underline, Style.UnderLine);
    }

    public boolean isColor() {
        return Style.isStyle(wordStyle, Style.BackgroudColor);
    }

    public void setColor(boolean color) {
        wordStyle = Style.setWordStyle(wordStyle, color, Style.BackgroudColor);
    }

    public boolean isSuperScript() {
        return Style.isStyle(wordStyle, Style.SuperScript);
    }

    public void setSuperScript(boolean superScript) {
        wordStyle = Style.setWordStyle(wordStyle, superScript, Style.SuperScript);
    }

    public boolean isSubScript() {
        return Style.isStyle(wordStyle, Style.SubScript);
    }

    public void setSubScript(boolean subScript) {
        wordStyle = Style.setWordStyle(wordStyle, subScript, Style.SubScript);
    }

    public boolean isStrikethrough() {
        return Style.isStyle(wordStyle, Style.Strikethrough);
    }

    public void setStrikethrough(boolean strikethrough) {
        wordStyle = Style.setWordStyle(wordStyle, strikethrough, Style.Strikethrough);
    }

    public boolean isCheckBox() {
        return Style.isStyle(lineStyle, Style.CheckBox);
    }

    public void setCheckBox(boolean checkBox) {
        lineStyle = Style.setLineStyle(lineStyle, checkBox, Style.CheckBox);
    }

    public boolean isNumList() {
        return Style.isStyle(lineStyle, Style.NumList);
    }

    public void setNumList(boolean numList) {
        lineStyle = Style.setLineStyle(lineStyle, numList, Style.NumList);
    }

    public boolean isBulletList() {
        return Style.isStyle(lineStyle, Style.BulletList);
    }

    public void setBulletList(boolean bulletList) {
        lineStyle = Style.setLineStyle(lineStyle, bulletList, Style.BulletList);
    }

    public boolean isDividingLine() {
        return Style.isStyle(lineStyle, Style.DividingLine);
    }

    public void setDividingLine(boolean dividingLine) {
        lineStyle = Style.setLineStyle(lineStyle, dividingLine, Style.DividingLine);
    }

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public int getWordStyle() {
        return wordStyle;
    }

    public void setWordStyle(int wordStyle) {
        this.wordStyle = wordStyle;
    }

    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
    }

    public static Options getSameStyle(int indentation, int lineStyle, List<Integer> wordStyles) {
        Options options = new Options();
        options.setLineStyle(lineStyle);
        options.setIndentation(indentation);

        if (wordStyles.isEmpty()) {
            return options;
        }

        options.setBold(true);
        options.setItalic(true);
        options.setUnderline(true);
        options.setStrikethrough(true);
        options.setColor(true);
        options.setSuperScript(true);
        options.setSubScript(true);
        for (int word: wordStyles) {
            if (!Style.isStyle(word, Style.Bold)) {
                options.setBold(false);
            }
            if (!Style.isStyle(word, Style.Italic)) {
                options.setItalic(false);
            }
            if (!Style.isStyle(word, Style.UnderLine)) {
                options.setUnderline(false);
            }
            if (!Style.isStyle(word, Style.Strikethrough)) {
                options.setStrikethrough(false);
            }
            if (!Style.isStyle(word, Style.BackgroudColor)) {
                options.setColor(false);
            }
            if (!Style.isStyle(word, Style.SuperScript)) {
                options.setSuperScript(false);
            }
            if (!Style.isStyle(word, Style.SubScript)) {
                options.setSubScript(false);
            }
        }
        return options;
    }
}
