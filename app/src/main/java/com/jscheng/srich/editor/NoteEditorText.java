package com.jscheng.srich.editor;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.jscheng.srich.utils.EditTextUtil;
import com.jscheng.srich.utils.KeyboardUtil;
import com.jscheng.srich.utils.OsUtil;
/**
 * Created By Chengjunsen on 2019/2/21
 */
public class NoteEditorText extends AppCompatEditText implements TextWatcher{
    private static final String TAG = "NoteEditorText";
    private int startTextChangePos = 0;
    private int endTextChangePos = 0;
    private NoteEditorStyleManager mStyleManager;

    public NoteEditorText(Context context) {
        super(context, null);
        init();
    }

    public NoteEditorText(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public NoteEditorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mStyleManager = new NoteEditorStyleManager(this);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
        this.setScrollBarStyle(SCROLLBARS_INSIDE_INSET);
        this.setInputType(EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setBackground(null);
        this.setHighlightColor(NoteEditorConfig.HighLightColor);
        this.setCursorColor(NoteEditorConfig.CursorColor);
        this.setHandlerColor(NoteEditorConfig.HandleColor);
        this.setSingleLine(false);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, NoteEditorConfig.TextSizeSp);
        this.setLetterSpacing(NoteEditorConfig.LetterSpacing);
        this.setLineSpacing(NoteEditorConfig.LineSpacing, 1f);
        this.setFocusableInTouchMode(true); // 触摸获得焦点
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
        this.setSelected(true);
        this.showSoftKeyboard();
        this.requestFocus();
        this.moveCursorToLastSpan();
        this.addTextChangedListener(this);
        this.setFilters(new InputFilter[] {new WritingInputFilter()});
    }

    // 读模式
    public void readingMode() {
        this.setCursorVisible(false);
        this.setSelected(false);
        this.removeTextChangedListener(this);
        this.setFilters(new InputFilter[] {new ReadingInputFilter()});
        this.hideSoftKeyboard();
    }

    private void moveCursorToLastSpan() {
        Editable editable = getText();
        if (editable != null) {
            this.setSelection(editable.length());
        }
    }

    private void hideSoftKeyboard() {
        clearFocus();
        KeyboardUtil.configSoftInput(this, false);
        KeyboardUtil.hideSoftInput(getContext(), this);
    }

    private void showSoftKeyboard() {
        KeyboardUtil.configSoftInput(this, true);
        KeyboardUtil.showSoftInput(getContext(), this);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mStyleManager != null) {
            mStyleManager.onSelectionChanged(getEditableText(), selStart, selEnd);
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
        if (mStyleManager != null) {
            mStyleManager.onTextChanged(editable, startTextChangePos, endTextChangePos);
        }
   }

    int downX = 0;
    int downY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                break;
            default:
                break;
        }
       return super.onTouchEvent(event);
    }

    public NoteEditorStyleManager getStyleManager() {
        return mStyleManager;
    }

    private class ReadingInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return source.length() < 1 ? dest.subSequence(dstart, dend) : "";
        }
    }

    private class WritingInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            return null;
        }
    }
}
