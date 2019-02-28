package com.jscheng.srich.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/28
 */
public class Paragraph {

    public static final int Bold = 1;

    public static final int Italic = 2;

    public static final int UnderLine = 4;

    public static final int Strikethrough = 8;

    public static final int BackgroudColor = 16;

    public static final int SuperScript = 32;

    public static final int SubScript = 64;

    public static final int DividingLine = 1;

    public static final int CheckBox = 2;

    public static final int NumList = 4;

    public static final int BulletList = 8;

    private int start;

    private int end;

    private StringBuilder words;

    private List<Integer> wordStyles;

    private int lineStyle;

    private int indent;

    public Paragraph() {
        this.start = 0;
        this.end = 0;
        this.wordStyles = new ArrayList<>();
        this.words = new StringBuilder();
        this.lineStyle = 0;
        this.indent = 0;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void add(String words, )
}
