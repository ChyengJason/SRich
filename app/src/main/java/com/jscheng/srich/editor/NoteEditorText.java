package com.jscheng.srich.editor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.jscheng.srich.editor.spans.BoldSpan;
import com.jscheng.srich.utils.EditTextUtil;
import com.jscheng.srich.utils.OsUtil;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class NoteEditorText extends AppCompatEditText implements TextWatcher {
    private static final String TAG = "NoteEditorText";
    private int startTextChangePos = 0;
    private int endTextChangePos = 0;

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
        this.setBackground(null);
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

    // 写模式
    public void writingMode() {
        this.setCursorVisible(true);
        this.setFocusableInTouchMode(true); // 触摸获得焦点
        this.setInputType(EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setSelected(true);
        this.showSoftKeyBoard();
        this.moveCursorToLastSpan();
        this.requestFocus();
        this.addTextChangedListener(this);
    }

    // 读模式
    public void readingMode() {
        this.setFocusableInTouchMode(false); // 触摸获得焦点
        this.setCursorVisible(false);
        this.setSelected(false);
        this.clearFocus();
        this.setInputType(InputType.TYPE_NULL);
        this.hideSoftKeyBoard();
        this.removeTextChangedListener(this);
    }

    private void moveCursorToLastSpan() {
        Editable editable = getText();
        if (editable != null) {
            this.setSelection(editable.length());
        }
    }

    private void hideSoftKeyBoard() {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.SHOW_FORCED);
    }

    private void showSoftKeyBoard() {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd){
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == selEnd) {

        } else {

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.startTextChangePos = start;
        this.endTextChangePos = start + count;
    }

    /**
     * SPAN_EXCLUSIVE_EXCLUSIVE // 在Span前后输入的字符都不应用Span效果
     * SPAN_EXCLUSIVE_INCLUSIVE // 在Span前面输入的字符不应用Span效果，后面输入的字符应用Span效果
     * SPAN_INCLUSIVE_EXCLUSIVE // 在Span前面输入的字符应用Span效果，后面输入的字符不应用Span效果
     * SPAN_INCLUSIVE_INCLUSIVE // 在Span前后输入的字符都应用Span效果
     */
    @Override
    public void afterTextChanged(Editable editable) {
        BoldSpan boldSpan = new BoldSpan();
        editable.setSpan(boldSpan, startTextChangePos, endTextChangePos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
