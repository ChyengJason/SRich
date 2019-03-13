package com.jscheng.srich.converter.encoder;

import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;
import com.jscheng.srich.model.StyleCode;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public class ParagraphEncoder {

    public static String encode(List<Paragraph> paragraphs) {
        StringBuilder content = new StringBuilder();
        for (Paragraph paragraph : paragraphs) {
            encode(content, paragraph);
        }
        return content.toString();
    }

    public static String encode(StringBuilder content, Paragraph paragraph) {
        if (encodeParagraphStyle(content, paragraph)) {
            content.append(StyleCode.Paragraph);
        } else {
            encodeLineStyle(content, paragraph);
            encodeWordStyle(content, paragraph);
            content.append(StyleCode.Paragraph);
        }
        return content.toString();
    }

    private static boolean encodeParagraphStyle(StringBuilder content, Paragraph paragraph) {
        if (paragraph.isImage()) {
            content.append(StyleCode.ImageBegin);
            content.append(paragraph.getImageUrl());
            content.append(StyleCode.ImageEnd);
            return true;
        } else if (paragraph.isDividingLine()) {
            content.append(StyleCode.DividingLine);
            return true;
        }
        return false;
    }

    private static void encodeLineStyle(StringBuilder content, Paragraph paragraph) {
        for (int i = 0; i < paragraph.getIndentation(); i++) {
            content.append(StyleCode.Indentation);
        }
        if (paragraph.isNumList()){
            content.append(StyleCode.NumList);
        } else if (paragraph.isBulletList()) {
            content.append(StyleCode.Bullet);
        } else if (paragraph.isCheckbox()) {
            content.append(StyleCode.CheckBox);
        } else if (paragraph.isUnCheckbox()) {
            content.append(StyleCode.UnCheckBox);
        }
    }

    private static void encodeWordStyle(StringBuilder stringBuilder, Paragraph paragraph) {
        int start = 0;

        List<Integer> wordStyles = paragraph.getWordStyles();
        StringBuilder content = new StringBuilder();

        int lastStyle = 0;
        for (int i = start; i < paragraph.getWords().length(); i++) {
            int wordStyle = wordStyles.get(i);
            checkWordStyle(wordStyle, lastStyle, content);
            String escapeWord = escape(paragraph.getWords(i, i + 1));
            content.append(escapeWord);
            lastStyle = wordStyle;
        }
        checkWordStyle(0, lastStyle, content);
        stringBuilder.append(content);
    }

    private static void checkWordStyle(int wordStyle, int lastStyle, StringBuilder content) {
        checkWordStyle(wordStyle, lastStyle, Style.Bold, StyleCode.BoldBegin, StyleCode.BoldEnd, content);
        checkWordStyle(wordStyle, lastStyle, Style.Italic, StyleCode.ItalicBegin, StyleCode.ItalicEnd, content);
        checkWordStyle(wordStyle, lastStyle, Style.BackgroudColor, StyleCode.BackgroudColorBegin, StyleCode.BackgroudColorEnd, content);
        checkWordStyle(wordStyle, lastStyle, Style.SubScript, StyleCode.SubScriptBegin, StyleCode.SubScriptEnd, content);
        checkWordStyle(wordStyle, lastStyle, Style.SuperScript, StyleCode.SuperScriptBegin, StyleCode.SubScriptBegin, content);
        checkWordStyle(wordStyle, lastStyle, Style.Strikethrough, StyleCode.StrikethroughBegin, StyleCode.StrikethroughEnd, content);
    }

    private static void checkWordStyle(int currentWordStyle, int lastWordStyle,
                                       int styleFlag, String beginCode, String endCode,
                                       StringBuilder content) {
        if (Style.isWordStyle(currentWordStyle, styleFlag) && !Style.isWordStyle(lastWordStyle, styleFlag)) {
            content.append(beginCode);
        }
        if (!Style.isWordStyle(currentWordStyle, styleFlag) && Style.isWordStyle(lastWordStyle, styleFlag)) {
            content.append(endCode);
        }
    }

    private static String escape(String words) {
        words = words.replace("/", "\\/")
                .replace("<", "\\<")
                .replace(">", "\\>");
        return words;
    }

}
