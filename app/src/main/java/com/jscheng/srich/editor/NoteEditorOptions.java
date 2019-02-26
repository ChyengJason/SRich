package com.jscheng.srich.editor;

import android.widget.TextView;

/**
 * Created By Chengjunsen on 2019/2/26
 */
public class NoteEditorOptions {
    private boolean isBold;

    private boolean isItalic;

    private boolean isUnderline;

    private boolean isCheckBox;

    private boolean isColor;

    private int numList;

    private boolean isBulletList;

    private int indentation;

    private boolean isSuperScript;

    private boolean isSubScript;

    private  boolean isStrikethrough;

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean italic) {
        isItalic = italic;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean underline) {
        isUnderline = underline;
    }


    public boolean isCheckBox() {
        return isCheckBox;
    }

    public void setCheckBox(boolean checkBox) {
        isCheckBox = checkBox;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }

    public int getNumList() {
        return numList;
    }

    public void setNumList(int numList) {
        this.numList = numList;
    }

    public boolean isBulletList() {
        return isBulletList;
    }

    public void setBulletList(boolean bulletList) {
        isBulletList = bulletList;
    }

    public int getIndentation() {
        return indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public boolean isSuperScript() {
        return isSuperScript;
    }

    public void setSuperScript(boolean superScript) {
        isSuperScript = superScript;
    }

    public boolean isSubScript() {
        return isSubScript;
    }

    public void setSubScript(boolean subScript) {
        isSubScript = subScript;
    }

    public boolean isStrikethrough() {
        return isStrikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        isStrikethrough = strikethrough;
    }

}
