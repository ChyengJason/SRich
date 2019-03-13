package com.jscheng.srich.converter.decoder;

import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;
import com.jscheng.srich.model.StyleCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public class ParagraphDecoder {

    public static List<Paragraph> decode(String rowContent) {
        String[] contents = rowContent.split(StyleCode.Paragraph);
        List<Paragraph> paragraphs = new ArrayList<>();
        for (String content : contents) {
            Paragraph paragraph = decodeParagraph(content);
            paragraph.setDirty(false);
            paragraphs.add(paragraph);
        }
        return paragraphs;
    }

    private static Paragraph decodeParagraph(String content) {
        Paragraph paragraph = new Paragraph();
        if (decodeParagraphStyle(paragraph, content)) {
            return paragraph;
        }
        content = decodeLineStyle(paragraph, content);
        decodeWordStyle(paragraph, content);
        return paragraph;
    }

    private static boolean decodeParagraphStyle(Paragraph paragraph, String content) {
        if (content.startsWith(StyleCode.ImageBegin)) {
            String url = content.substring(StyleCode.ImageBegin.length(),
                    content.length() - StyleCode.ImageEnd.length());
            paragraph.setImage(url);
            return true;
        }

        if (content.startsWith(StyleCode.DividingLine)) {
            paragraph.setDividingLine(true);
            return true;
        }
        return false;
    }

    private static String decodeLineStyle(Paragraph paragraph, String content) {
        int indentation = 0;
        while (content.startsWith(StyleCode.Indentation)) {
            indentation++;
            content = content.substring(StyleCode.Indentation.length());
        }
        paragraph.setIndentation(indentation);

        if (content.startsWith(StyleCode.Bullet)) {
            paragraph.setBulletList(true);
            return content.substring(StyleCode.Bullet.length());
        }
        if (content.startsWith(StyleCode.NumList)) {
            paragraph.setNumList(true);
            return content.substring(StyleCode.NumList.length());
        }
        if (content.startsWith(StyleCode.CheckBox)) {
            paragraph.setCheckbox(true);
            return content.substring(StyleCode.CheckBox.length());
        }
        if (content.startsWith(StyleCode.UnCheckBox)) {
            paragraph.setUnCheckbox(true);
            return content.substring(StyleCode.UnCheckBox.length());
        }
        return content;
    }

    private static void decodeWordStyle(Paragraph paragraph, String content) {
        WordStyleNode node = new WordStyleNode(0, content);
        List<WordStyleNode> childs = node.splitStyle(StyleCode.BoldBegin, StyleCode.BoldEnd, Style.Bold);
        childs = node.splitStyle(childs, StyleCode.ItalicBegin, StyleCode.ItalicEnd, Style.Italic);
        childs = node.splitStyle(childs, StyleCode.BackgroudColorBegin, StyleCode.BackgroudColorEnd, Style.BackgroudColor);
        childs = node.splitStyle(childs, StyleCode.StrikethroughBegin, StyleCode.StrikethroughEnd, Style.Strikethrough);
        childs = node.splitStyle(childs, StyleCode.SubScriptBegin, StyleCode.SubScriptEnd, Style.SubScript);
        childs = node.splitStyle(childs, StyleCode.SuperScriptBegin, StyleCode.SuperScriptEnd, Style.SuperScript);
        childs = node.splitStyle(childs, StyleCode.UnderLineBegin, StyleCode.UnderLineEnd, Style.UnderLine);

        for (WordStyleNode child: childs) {
            String words = invertEscape(child.getContent());
            List<Integer> wordStyles = new ArrayList<>();
            for (int i = 0; i < words.length(); i++) {
                wordStyles.add(child.getStyle());
            }
            paragraph.addWords(words, wordStyles);
        }
    }

    private static String invertEscape(String words) {
        words = words.replace("\\/", "/")
                .replace("\\<", "<")
                .replace("\\>", ">");
        return words;
    }
}
