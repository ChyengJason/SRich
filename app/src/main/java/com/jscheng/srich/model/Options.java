package com.jscheng.srich.model;
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
        wordStyle = Style.setStyle(wordStyle, bold, Style.Bold);
    }

    public boolean isItalic() {
        return Style.isStyle(wordStyle, Style.Italic);
    }

    public void setItalic(boolean italic) {
        wordStyle = Style.setStyle(wordStyle, italic, Style.Italic);
    }

    public boolean isUnderline() {
        return Style.isStyle(wordStyle, Style.UnderLine);
    }

    public void setUnderline(boolean underline) {
        wordStyle = Style.setStyle(wordStyle, underline, Style.UnderLine);
    }

    public boolean isColor() {
        return Style.isStyle(wordStyle, Style.BackgroudColor);
    }

    public void setColor(boolean color) {
        wordStyle = Style.setStyle(wordStyle, color, Style.BackgroudColor);
    }

    public boolean isSuperScript() {
        return Style.isStyle(wordStyle, Style.SuperScript);
    }

    public void setSuperScript(boolean superScript) {
        wordStyle = Style.setStyle(wordStyle, superScript, Style.SuperScript);
    }

    public boolean isSubScript() {
        return Style.isStyle(wordStyle, Style.SubScript);
    }

    public void setSubScript(boolean subScript) {
        wordStyle = Style.setStyle(wordStyle, subScript, Style.SubScript);
    }

    public boolean isStrikethrough() {
        return Style.isStyle(wordStyle, Style.Strikethrough);
    }

    public void setStrikethrough(boolean strikethrough) {
        wordStyle = Style.setStyle(wordStyle, strikethrough, Style.Strikethrough);
    }

    public boolean isCheckBox() {
        return Style.isStyle(lineStyle, Style.CheckBox);
    }

    public void setCheckBox(boolean checkBox) {
        lineStyle = Style.setStyle(lineStyle, checkBox, Style.CheckBox);
    }

    public boolean isNumList() {
        return Style.isStyle(lineStyle, Style.NumList);
    }

    public void setNumList(boolean numList) {
        lineStyle = Style.setStyle(lineStyle, numList, Style.NumList);
    }

    public boolean isBulletList() {
        return Style.isStyle(lineStyle, Style.BulletList);
    }

    public void setBulletList(boolean bulletList) {
        lineStyle = Style.setStyle(lineStyle, bulletList, Style.BulletList);
    }

    public boolean isDividingLine() {
        return Style.isStyle(lineStyle, Style.DividingLine);
    }

    public void setDividingLine(boolean dividingLine) {
        lineStyle = Style.setStyle(lineStyle, dividingLine, Style.DividingLine);
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

}
