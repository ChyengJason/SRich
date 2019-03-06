package com.jscheng.srich.model;

/**
 * Created By Chengjunsen on 2019/3/1
 */
public class Style {
    public static final int Bold = 1;

    public static final int Italic = 2;

    public static final int UnderLine = 4;

    public static final int Strikethrough = 8;

    public static final int BackgroudColor = 16;

    public static final int SuperScript = 32;

    public static final int SubScript = 64;

    public static final int CheckBox = 1;

    public static final int UnCheckBox = 2;

    public static final int NumList = 3;

    public static final int BulletList = 4;

    public static final int DividingLine = 5;

    public static final int Image = 6;

    public static boolean isWordStyle(int style, int flag) {
        return (style & flag) == flag;
    }

    public static int setWordStyle(int style, boolean b, int flag) {
        return b ? style | flag : (style | flag) ^ flag;
    }

    public static int setLineStyle(int style, boolean b, int flag) {
        style = b ? flag : (style == flag ? 0 : style);
        return style;
    }

    public static boolean isLineStyle(int style, int flag) {
        return style == flag;
    }

    public static int clearLineStyle(int style) {
        return 0;
    }

    public static int clearWordStyle(int style) {
        return 0;
    }
}
