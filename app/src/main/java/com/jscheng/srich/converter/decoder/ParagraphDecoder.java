package com.jscheng.srich.converter.decoder;

import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.ParagraphBuilder;
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
            ParagraphBuilder paragraphBuilder = new ParagraphBuilder();
            decodeParagraph(content, paragraphBuilder);
            paragraphs.add(paragraphBuilder.build());
        }
        return paragraphs;
    }

    private static void decodeParagraph(String content, ParagraphBuilder builder) {
        if (decodeParagraphStyle(builder, content)) {
            return;
        }
        content = decodeLineStyle(builder, content);
        decodeWordStyle(builder, content);
    }

    private static boolean decodeParagraphStyle(ParagraphBuilder builder, String content) {
        if (content.startsWith(StyleCode.ImageBegin)) {
            String url = content.substring(StyleCode.ImageBegin.length(),
                    content.length() - StyleCode.ImageEnd.length());
            builder.image(url);
            return true;
        }

        if (content.startsWith(StyleCode.DividingLine)) {
            builder.dividingLine(true);
            return true;
        }
        return false;
    }

    private static String decodeLineStyle(ParagraphBuilder builder, String content) {
        int indentation = 0;
        while (content.startsWith(StyleCode.Indentation)) {
            indentation++;
            content = content.substring(StyleCode.Indentation.length());
        }
        builder.indentation(indentation);

        if (content.startsWith(StyleCode.Bullet)) {
            builder.bullet(true);
            return content.substring(StyleCode.Bullet.length());
        }
        if (content.startsWith(StyleCode.NumList)) {
            builder.numList(true);
            return content.substring(StyleCode.NumList.length());
        }
        if (content.startsWith(StyleCode.CheckBox)) {
            builder.checkbox(true);
            return content.substring(StyleCode.CheckBox.length());
        }
        if (content.startsWith(StyleCode.UnCheckBox)) {
            builder.uncheckBox(true);
            return content.substring(StyleCode.UnCheckBox.length());
        }
        return content;
    }

    private static void decodeWordStyle(ParagraphBuilder builder, String content) {
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
            builder.addWords(words, wordStyles);
        }
    }

    private static String invertEscape(String words) {
        words = words.replace("\\/", "/")
                .replace("\\<", "<")
                .replace("\\>", ">");
        return words;
    }
}
