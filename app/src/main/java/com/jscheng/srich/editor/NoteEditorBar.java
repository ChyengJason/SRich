package com.jscheng.srich.editor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.srich.R;
import com.jscheng.srich.utils.DisplayUtil;

import org.w3c.dom.Text;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteEditorBar extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener, View.OnClickListener{
    private ImageView mForwardButton;
    private ImageView mBackwardButton;
    private TextView mBoldButton;
    private TextView mItalicButton;
    private TextView mUnderlineButton;
    private TextView mCheckboxButton;
    private TextView mColorButton;
    private TextView mNumListButton;
    private TextView mBulletListButton;
    private TextView mIndentationButton;
    private TextView mReduceIndentationButton;
    private TextView mDividingLineButton;
    private TextView mSuperScriptButton;
    private TextView mSubscriptButton;
    private TextView mStrikethroughButton;

    private HorizontalScrollView mScollView;
    private NoteEditorOptions mEditorOptions;

    private NoteEditorText mEditorText;

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
        initView();
        this.mEditorOptions = new NoteEditorOptions();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.edit_note_editor_bar, this);
        mBoldButton = findViewById(R.id.edit_bar_bold);
        mBoldButton.setOnClickListener(this);
        mItalicButton = findViewById(R.id.edit_bar_italic);
        mItalicButton.setOnClickListener(this);
        mUnderlineButton = findViewById(R.id.edit_bar_underline);
        mUnderlineButton.setOnClickListener(this);
        mForwardButton = findViewById(R.id.edit_note_bar_forward);
        mForwardButton.setOnClickListener(this);
        mBackwardButton = findViewById(R.id.edit_note_bar_backward);
        mBackwardButton.setOnClickListener(this);
        mCheckboxButton = findViewById(R.id.edit_bar_checkbox);
        mCheckboxButton.setOnClickListener(this);
        mColorButton = findViewById(R.id.edit_bar_background_color);
        mColorButton.setOnClickListener(this);
        mNumListButton = findViewById(R.id.edit_bar_num_list);
        mNumListButton.setOnClickListener(this);
        mBulletListButton = findViewById(R.id.edit_bar_bullet_list);
        mBulletListButton.setOnClickListener(this);
        mIndentationButton = findViewById(R.id.edit_bar_indentation);
        mIndentationButton.setOnClickListener(this);
        mReduceIndentationButton = findViewById(R.id.edit_bar_reduce_indentation);
        mReduceIndentationButton.setOnClickListener(this);
        mDividingLineButton = findViewById(R.id.edit_bar_dividing_line);
        mDividingLineButton.setOnClickListener(this);
        mSuperScriptButton = findViewById(R.id.edit_bar_superscript);
        mSuperScriptButton.setOnClickListener(this);
        mSubscriptButton = findViewById(R.id.edit_bar_subscript);
        mSubscriptButton.setOnClickListener(this);
        mStrikethroughButton = findViewById(R.id.edit_bar_strikethrough);
        mStrikethroughButton.setOnClickListener(this);

        mScollView = findViewById(R.id.edit_note_bar_scroll_view);
        mScollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int endViewRight = mScollView.getChildAt(mScollView.getChildCount() - 1).getRight();
                    if(endViewRight - mScollView.getScrollX() - mScollView.getWidth() < 20) {
                        mForwardButton.setVisibility(GONE);
                        mBackwardButton.setVisibility(VISIBLE);
                    } else if(mScollView.getScrollX() < 20) {
                        mBackwardButton.setVisibility(GONE);
                        mForwardButton.setVisibility(VISIBLE);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onGlobalLayout() {

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

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_note_bar_forward:
                forward();
                break;
            case R.id.edit_note_bar_backward:
                backward();
                break;
            case R.id.edit_bar_bold:
                tapBarBold();
                break;
            case R.id.edit_bar_italic:
                tapBarItalic();
                break;
            case R.id.edit_bar_underline:
                tapBarUnderline();
                break;
            case R.id.edit_bar_checkbox:
                tapBarCheckBox();
                break;
            case R.id.edit_bar_background_color:
                tapBarColor();
                break;
            case R.id.edit_bar_num_list:
                tapBarNumList();
                break;
            case R.id.edit_bar_bullet_list:
                tapBarBulletList();
                break;
            case R.id.edit_bar_indentation:
                tapBarIndentation();
                break;
            case R.id.edit_bar_reduce_indentation:
                tapBarReduceIndentation();
                break;
            case R.id.edit_bar_dividing_line:
                tapBarDividingLine();
                break;
            case R.id.edit_bar_superscript:
                tapBarSuperscript();
                break;
            case R.id.edit_bar_subscript:
                tapBarSubscript();
                break;
            case R.id.edit_bar_strikethrough:
                tapbarStrikeThrought();
                break;
            default:
                break;
        }
    }

    private void tapbarStrikeThrought() {
        boolean isSelected = !mEditorOptions.isStrikethrough();
        mEditorOptions.setStrikethrough(isSelected);
        applyStatus(mStrikethroughButton, isSelected);
    }

    private void tapBarSubscript() {
        boolean isSelected = !mEditorOptions.isSubScript();
        mEditorOptions.setSubScript(isSelected);
        applyStatus(mSubscriptButton, isSelected);

        if (isSelected) {
            mEditorOptions.setSuperScript(false);
            applyStatus(mSuperScriptButton, false);
        }
    }

    private void tapBarSuperscript() {
        boolean isSelected = !mEditorOptions.isSuperScript();
        mEditorOptions.setSuperScript(isSelected);
        applyStatus(mSuperScriptButton, isSelected);

        if (isSelected) {
            mEditorOptions.setSubScript(false);
            applyStatus(mSubscriptButton, false);
        }
    }

    private void tapBarDividingLine() {

    }

    private void tapBarReduceIndentation() {

    }

    private void tapBarIndentation() {

    }

    private void tapBarBulletList() {

    }

    private void tapBarNumList() {

    }

    private void tapBarColor() {
        boolean isSelected = !mEditorOptions.isColor();
        mEditorOptions.setColor(isSelected);
        applyStatus(mColorButton, isSelected);
    }

    private void tapBarCheckBox() {

    }

    private void tapBarUnderline() {
        boolean isSelected = !mEditorOptions.isUnderline();
        mEditorOptions.setUnderline(isSelected);
        applyStatus(mUnderlineButton, isSelected);
    }

    private void tapBarItalic() {
        boolean isSelected = !mEditorOptions.isItalic();
        mEditorOptions.setItalic(isSelected);
        applyStatus(mItalicButton, isSelected);
    }

    private void tapBarBold() {
        boolean isSelected = !mEditorOptions.isBold();
        mEditorOptions.setBold(isSelected);
        applyStatus(mBoldButton, isSelected);
    }

    private void backward() {
        mScollView.fullScroll(View.FOCUS_LEFT);
        mForwardButton.setVisibility(VISIBLE);
        mBackwardButton.setVisibility(GONE);
    }

    private void forward() {
        mScollView.fullScroll(FOCUS_RIGHT);
        mForwardButton.setVisibility(GONE);
        mBackwardButton.setVisibility(VISIBLE);
    }

    public void apply(NoteEditorOptions options) {
        this.mEditorOptions = options;
        this.applyStatus(mBoldButton, options.isBold());
        this.applyStatus(mItalicButton, options.isItalic());
        this.applyStatus(mUnderlineButton, options.isUnderline());
    }

    private void applyStatus(TextView view, boolean isSelected) {
        if (isSelected) {
            int color = getContext().getResources().getColor(R.color.editor_bar_item_selected_text_color, null);
            view.setTextColor(color);
        } else {
            int color = getContext().getResources().getColor(R.color.editor_bar_item_text_color, null);
            view.setTextColor(color);
        }
    }

    public NoteEditorOptions getNoteEditorOptions() {
        return mEditorOptions;
    }
}
