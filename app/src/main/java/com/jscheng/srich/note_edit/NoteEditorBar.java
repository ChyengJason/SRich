package com.jscheng.srich.note_edit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.INoteEditorManager;
import com.jscheng.srich.editor.NoteEditorSelectionListener;
import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.model.Options;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class NoteEditorBar extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener,
        View.OnClickListener, NoteEditorSelectionListener {
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

    private INoteEditorManager mStyleManager;

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

    public void setStyleManager(INoteEditorManager styleManager) {
        this.mStyleManager = styleManager;
        this.mStyleManager.addSelectionChangeListener(this);
    }

    private void init() {
        initView();
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
        if (mStyleManager != null) {
            mStyleManager.addSelectionChangeListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (mStyleManager != null) {
            mStyleManager.removeSelectionChangeListener(this);
        }
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
                tapbarStrikeThrough();
                break;
            default:
                break;
        }
    }

    private void tapbarStrikeThrough() {
        boolean isSelected = !mStrikethroughButton.isSelected();
        mStrikethroughButton.setSelected(isSelected);
        mStyleManager.commandStrikeThrough(isSelected, true);
    }

    private void tapBarSubscript() {
        boolean isSelected = !mSubscriptButton.isSelected();
        mSubscriptButton.setSelected(isSelected);
        if (isSelected) {
            mSuperScriptButton.setSelected(false);
        }
        mStyleManager.commandSubscript(isSelected, true);
    }

    private void tapBarSuperscript() {
        boolean isSelected = !mSuperScriptButton.isSelected();
        mSuperScriptButton.setSelected(isSelected);
        if (isSelected) {
            mSubscriptButton.setSelected(false);
        }
        mStyleManager.commandSuperscript(isSelected,true);
    }

    private void tapBarDividingLine() {
        mStyleManager.commandDividingLine(true);
    }

    private void tapBarReduceIndentation() {
        mStyleManager.commandReduceIndentation(true);
    }

    private void tapBarIndentation() {
        mStyleManager.commandIndentation(true);
    }

    private void tapBarBulletList() {
        boolean isSelected = !mBulletListButton.isSelected();
        mBulletListButton.setSelected(isSelected);
        mStyleManager.commandBulletList(isSelected, true);
    }

    private void tapBarNumList() {
        boolean isSelected = !mNumListButton.isSelected();
        mNumListButton.setSelected(isSelected);
        mStyleManager.commandNumList(isSelected,true);
    }

    private void tapBarColor() {
        boolean isSelected = !mColorButton.isSelected();
        mColorButton.setSelected(isSelected);
        mStyleManager.commandColor(isSelected,true);
    }

    private void tapBarCheckBox() {
        boolean isSelected = !mCheckboxButton.isSelected();
        mCheckboxButton.setSelected(isSelected);
        mStyleManager.commandCheckBox(isSelected,true);
    }

    private void tapBarUnderline() {
        boolean isSelected = !mUnderlineButton.isSelected();
        mUnderlineButton.setSelected(isSelected);
        mStyleManager.commandUnderline(isSelected,true);
    }

    private void tapBarItalic() {
        boolean isSelected = !mItalicButton.isSelected();
        mItalicButton.setSelected(isSelected);
        mStyleManager.commandItalic(isSelected,true);
    }

    private void tapBarBold() {
        boolean isSelected = !mBoldButton.isSelected();
        mBoldButton.setSelected(isSelected);
        mStyleManager.commandBold(isSelected,true);
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

    @Override
    public void onStyleChange(int start, int end, Options options) {
        applyEditorOptions(options);
    }

    private void applyEditorOptions(Options options) {
        mBoldButton.setSelected(options.isBold());
        mItalicButton.setSelected(options.isItalic());
        mUnderlineButton.setSelected(options.isUnderline());
        mColorButton.setSelected(options.isColor());
        mSuperScriptButton.setSelected(options.isSuperScript());
        mSubscriptButton.setSelected(options.isSubScript());
        mStrikethroughButton.setSelected(options.isStrikethrough());
        mBulletListButton.setSelected(options.isBulletList());
        mNumListButton.setSelected(options.isNumList());
        mCheckboxButton.setSelected(options.isCheckBox() || options.isUnCheckBox());
    }
}
