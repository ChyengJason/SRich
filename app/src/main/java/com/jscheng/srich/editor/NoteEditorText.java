package com.jscheng.srich.editor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import com.jscheng.srich.utils.EditTextUtil;
import com.jscheng.srich.utils.OsUtil;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class NoteEditorText extends AppCompatEditText {
    private static final String TAG = "NoteEditorText";
    public NoteEditorText(Context context) {
        this(context, null);
    }

    public NoteEditorText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteEditorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setFocusableInTouchMode(true); // 触摸获得焦点
        this.setBackground(null);
        this.setInputType(EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setHighlightColor(Color.GREEN);
        this.setCursorColor(Color.GREEN);
        this.setHandlerColor(Color.GREEN);
        this.readingMode();
    }

    public void setCursorColor(int color) {
        EditTextUtil.setEditTextCursorColor(this, color);
    }

    public void setHandlerColor(int color) {
        if (!OsUtil.isMIUI()) { // MIUI会出现奇怪现象
            EditTextUtil.setEditTextHandleColor(this, color);
        }
    }

    public void writingMode() {
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true);
        this.setKeyListener(super.getKeyListener());
        this.setClickable(true);
        this.setFocusable(true);
    }

    public void readingMode() {
        this.setCursorVisible(false);
        this.setFocusableInTouchMode(false); // 不可编辑
        this.setKeyListener(null); // 不可粘贴，长按不会弹出粘贴框
        this.setClickable(false); // 不可点击
        this.setFocusable(false); // 不可编辑
    }

}
