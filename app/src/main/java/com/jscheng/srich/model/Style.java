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

    public static final int NumList = 2;

    public static final int BulletList = 4;

    public static final int DividingLine = 8;

    public static final int Image = 16;

    public static boolean isStyle(int style, int flag) {
        return (style & flag) == flag;
    }

    public static int setStyle(int style, boolean b, int flag) {
        return b ? style | flag : (style | flag) ^ flag;
    }
}
