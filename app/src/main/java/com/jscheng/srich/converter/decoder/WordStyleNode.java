package com.jscheng.srich.converter.decoder;

import android.text.TextUtils;

import com.jscheng.srich.model.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public class WordStyleNode {
    private int style;
    private String content;

    public WordStyleNode(int lineStyle, String content) {
        this.style = lineStyle;
        this.content = content;
    }

    public List<WordStyleNode> splitStyle(List<WordStyleNode> nodes, String beginStyleCode, String endStyleCode, int flag) {
        List<WordStyleNode> list = new ArrayList<>();
        for (WordStyleNode node: nodes) {
            list.addAll(node.splitStyle(beginStyleCode, endStyleCode, flag));
        }
        return list;
    }

    public List<WordStyleNode> splitStyle(String beginStyleCode, String endStyleCode, int flag) {
        List<WordStyleNode> childs = new ArrayList<>();
        String[] temp = content.split(beginStyleCode + "|" + endStyleCode);
        for (int i = 0; i< temp.length; i++) {
            if (!TextUtils.isEmpty(temp[i])) {
                style = Style.setWordStyle(style, (i % 2 != 0), flag);
                childs.add(new WordStyleNode(style, temp[i]));
            }
        }
        return childs;
    }

    public int getStyle() {
        return style;
    }

    public String getContent() {
        return content;
    }
}
