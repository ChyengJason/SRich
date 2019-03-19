package com.jscheng.srich.editor.render;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.widget.EditText;

import com.jscheng.srich.model.Paragraph;

import java.util.List;

/**
 * 绘制字体样式
 * Created By Chengjunsen on 2019/3/4
 */
public abstract class NoteWordSpanRender<T extends CharacterStyle> {

    public void draw(int globalPos, Paragraph paragraph, SpannableStringBuilder builder) {
        List<Integer> wordStyles = paragraph.getWordStyles();
        int start = -1;
        for (int i = 0; i < wordStyles.size(); i++) {
            if (isStyle(wordStyles.get(i))) {
                if (start == -1) { start = i; }
            } else if (start > -1) {
                draw(globalPos, start, i, builder);
                start = -1;
            }
        }
        if (start > -1) {
            draw(globalPos, start, wordStyles.size(), builder);
        }
    }

    private void draw(int globalPos, int start, int end, SpannableStringBuilder builder) {
        builder.setSpan(createSpan(), start + globalPos, end + globalPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected abstract T createSpan();

    protected abstract int getStyle();

    protected abstract boolean isStyle(int style);
}
