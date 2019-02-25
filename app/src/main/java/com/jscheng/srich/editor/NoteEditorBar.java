package com.jscheng.srich.editor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jscheng.srich.utils.DisplayUtil;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteEditorBar extends LinearLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    private int mScreenHeight;
    private boolean isKeyboardVisiable;
    private int mKeyboardHeight;

    public NoteEditorBar(Context context) {
        super(context);
        init();
    }

    public NoteEditorBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NoteEditorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setBackgroundColor(Color.YELLOW);
        this.mScreenHeight = DisplayUtil.getScreenHeight(getContext());
        this.isKeyboardVisiable = false;
        this.mKeyboardHeight = 0;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        getRootView().getWindowVisibleDisplayFrame(rect);
        int keyboardHeight = mScreenHeight - rect.bottom;
        if (!isKeyboardVisiable && keyboardHeight > 100) {
            isKeyboardVisiable = true;
            mKeyboardHeight = keyboardHeight;
            showKeyboard();
        } else if (isKeyboardVisiable && keyboardHeight < 100) {
            isKeyboardVisiable = false;
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        MarginLayoutParams layoutParams = (MarginLayoutParams)getLayoutParams();
        int left = layoutParams.leftMargin;
        int right = layoutParams.rightMargin;
        int top = layoutParams.topMargin;
        int bottom = layoutParams.bottomMargin - mKeyboardHeight;
        layoutParams.setMargins(left, top, right, bottom);
        this.setLayoutParams(layoutParams);
    }

    private void showKeyboard() {
        MarginLayoutParams layoutParams = (MarginLayoutParams)getLayoutParams();
        int left = layoutParams.leftMargin;
        int right = layoutParams.rightMargin;
        int top = layoutParams.topMargin;
        int bottom = layoutParams.bottomMargin + mKeyboardHeight;
        layoutParams.setMargins(left, top, right, bottom);
        this.setLayoutParams(layoutParams);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
