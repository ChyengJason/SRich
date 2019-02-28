package com.jscheng.srich.editor;

import android.text.Editable;
import android.text.Spanned;
import com.jscheng.srich.editor.spans.NoteBackgroundSpan;
import com.jscheng.srich.editor.spans.NoteBoldSpan;
import com.jscheng.srich.editor.spans.NoteBulletSpan;
import com.jscheng.srich.editor.spans.NoteDividingLineSpan;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.NoteStrikethroughSpan;
import com.jscheng.srich.editor.spans.NoteSubscriptSpan;
import com.jscheng.srich.editor.spans.NoteSuperscriptSpan;
import com.jscheng.srich.editor.spans.NoteUnderLineSpan;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public class NoteEditableExecutor {

    /**
     * 删除 start 到 end 指定样式 spanClass
     * @param spanClass
     * @param editable
     * @param start
     * @param end
     * @param <T> [ )
     */
    public static <T> void remove(Class<T> spanClass, Editable editable, int start, int end) {
        T[] styles = editable.getSpans(start, end, spanClass);
        for (int i = 0; i < styles.length; i++) {
            T style = styles[i];
            int spanStart = editable.getSpanStart(style);
            int spanEnd =  editable.getSpanEnd(style);
            removeSpan(style, editable);
            if (spanStart < start) {
                addSpanWithMerge(createWordSpan(spanClass), editable, spanStart, start);
            }
            if (end < spanEnd) {
                addSpanWithMerge(createWordSpan(spanClass), editable, end, spanEnd);
            }
        }
    }

    /**
     * 增加 start 到 end 位置指定样式 spanClass
     * @param spanClass
     * @param editable
     * @param start
     * @param end
     * @param <T>
     */
    public static <T> void add(Class<T> spanClass, Editable editable, int start, int end) {
        addSpanWithMerge(createWordSpan(spanClass), editable, start, end);
    }

    public static void insert(NoteEditorOptions options, Editable editable, int start, int end) {
        if (options.isBold()) {
            addSpanWithMerge(NoteBoldSpan.create(), editable, start, end);
        }
        if (options.isItalic()) {
            addSpanWithMerge(NoteItalicSpan.create(), editable, start, end);
        }
        if (options.isUnderline()) {
            addSpanWithMerge(NoteUnderLineSpan.create(), editable, start, end);
        }
        if (options.isColor()) {
            addSpanWithMerge(NoteBackgroundSpan.create(), editable, start, end);
        }
        if (options.isSuperScript()) {
            addSpanWithMerge(NoteSuperscriptSpan.create(), editable, start, end);
        }
        if (options.isSubScript()) {
            addSpanWithMerge(NoteSubscriptSpan.create(), editable, start, end);
        }
        if (options.isStrikethrough()) {
            addSpanWithMerge(NoteStrikethroughSpan.create(), editable, start, end);
        }
    }

    /**
     * 将pos位置的样式切成左右两部分
     * @param styleSpanCls
     * @param editable
     * @param pos
     * @param <T>
     */
    public static <T> void seperate(Class<T> styleSpanCls, Editable editable, int pos) {
        Object[] spans = editable.getSpans(pos, pos + 1, styleSpanCls);
        for (int i = 0; i < spans.length; i++) {
            int temStart = editable.getSpanStart(spans[i]);
            int temEnd = editable.getSpanEnd(spans[i]);
            removeSpan(spans[i], editable);
            addSpan(createWordSpan(styleSpanCls), editable, temStart, pos);
            addSpan(createWordSpan(styleSpanCls), editable, pos, temEnd);
        }
    }

    private static <T> Object createWordSpan(Class<T> spanClass) {
        if (spanClass == NoteBoldSpan.class) {
            return NoteBoldSpan.create();
        }
        if (spanClass == NoteItalicSpan.class) {
           return NoteItalicSpan.create();
        }
        if (spanClass == NoteUnderLineSpan.class) {
            return NoteUnderLineSpan.create();
        }
        if (spanClass == NoteBackgroundSpan.class) {
            return NoteBackgroundSpan.create();
        }
        if (spanClass == NoteSuperscriptSpan.class) {
            return NoteSuperscriptSpan.create();
        }
        if (spanClass == NoteSubscriptSpan.class) {
            return NoteSubscriptSpan.create();
        }
        if (spanClass == NoteStrikethroughSpan.class) {
            return NoteStrikethroughSpan.create();
        }
        return null;
    }

    public static void addDividingLine(int width, Editable editable, int pos) {
        editable.insert(pos, "-");
        addSpan(NoteDividingLineSpan.create(width), editable, pos, pos + 1);
    }

    public static void addBulletList(Editable editable, int pos) {
        int start = getParagraphPosition(editable, pos);
        editable.insert(start, "*");
        addSpan(NoteBulletSpan.create(), editable, start, start + 1);
    }

    public static void removeBulletList(Editable editable, int pos) {
        int start = getParagraphPosition(editable, pos);
    }

    /**
     * 添加样式
     * 对start - 1 和 end + 1 位置的相同样式进行合并
     * @param span
     * @param editable
     * @param start
     * @param end
     */
    private static void addSpanWithMerge(Object span, Editable editable, int start, int end) {
        int left = start > 0 ? start - 1 : start;
        int right = end < editable.length() - 1 ? end + 1 : end;
        Object[] spans = editable.getSpans(left, right, span.getClass());
        int spanStart = start;
        int spanEnd = end;
        for (int i = 0; i < spans.length; i++) {
            int temStart = editable.getSpanStart(spans[i]);
            if (temStart < spanStart) {
                spanStart = temStart;
            }
            int temEnd = editable.getSpanEnd(spans[i]);
            if (temEnd > spanEnd) {
                spanEnd = temEnd;
            }
            removeSpan(spans[i], editable);
        }
        editable.setSpan(span, spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 添加样式
     * 不会合并
     * @param span
     * @param editable
     * @param start
     * @param end
     */
    private static void addSpan(Object span, Editable editable, int start, int end) {
        editable.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 删除样式
     * @param span
     * @param editable
     */
    private static void removeSpan(Object span, Editable editable) {
        editable.removeSpan(span);
    }

    private static int getParagraphPosition(Editable editable, int pos) {
        int first = pos >= editable.length() ? editable.length() - 1 : pos;
        while (first > 0) {
            int c = editable.charAt(first);
            if (c == NoteEditorConfig.NewLine) {
                if (first < editable.length() - 1) {
                    // firstPos 指向换行符下一个字符
                    first++;
                }
                break;
            } else {
                first--;
            }
        }
        return first;
    }
}
