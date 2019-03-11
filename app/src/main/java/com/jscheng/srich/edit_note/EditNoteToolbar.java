package com.jscheng.srich.edit_note;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jscheng.srich.R;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteToolbar extends LinearLayout implements View.OnClickListener {
    private ImageView mBackView;
    private ImageView mMoreView;
    private ImageView mRedoView;
    private ImageView mUndoView;
    private ImageView mFormatView;
    private ImageView mAttachView;
    private ImageView mTickView;
    private EditNotePresenter mPresenter;

    public EditNoteToolbar(Context context) {
        this(context, null);
    }

    public EditNoteToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditNoteToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.edit_note_toolbar, this);
        mBackView = findViewById(R.id.back_btn);
        mBackView.setOnClickListener(this);

        mMoreView = findViewById(R.id.more_btn);
        mMoreView.setOnClickListener(this);

        mTickView = findViewById(R.id.tick_btn);
        mTickView.setOnClickListener(this);

        mRedoView = findViewById(R.id.redo_btn);
        mRedoView.setOnClickListener(this);

        mUndoView = findViewById(R.id.undo_btn);
        mUndoView.setOnClickListener(this);

        mAttachView = findViewById(R.id.attach_btn);
        mAttachView.setOnClickListener(this);

        mFormatView = findViewById(R.id.format_btn);
        mFormatView.setOnClickListener(this);
    }

    public void setPresenter(EditNotePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void writingMode() {
        mBackView.setVisibility(GONE);
        mTickView.setVisibility(VISIBLE);
        mUndoView.setVisibility(VISIBLE);
        mRedoView.setVisibility(VISIBLE);
        mAttachView.setVisibility(VISIBLE);
        mFormatView.setVisibility(VISIBLE);
    }

    public void readingMode() {
        mBackView.setVisibility(VISIBLE);
        mTickView.setVisibility(GONE);
        mUndoView.setVisibility(GONE);
        mRedoView.setVisibility(GONE);
        mAttachView.setVisibility(GONE);
        mFormatView.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
        checkPresenter();
        switch (v.getId()) {
            case R.id.back_btn:
                mPresenter.tapBack();
                break;
            case R.id.more_btn:
                mPresenter.tapMore();
                break;
            case R.id.tick_btn:
                mPresenter.tapTick();
                break;
            case R.id.undo_btn:
                mPresenter.tapUndo();
                break;
            case R.id.redo_btn:
                mPresenter.tapRedo();
                break;
            case R.id.format_btn:
                boolean isEnable = !mFormatView.isSelected();
                mPresenter.tapEditorBar(isEnable);
                break;
            case R.id.attach_btn:
                mPresenter.tapAttach();
            default:
                break;
        }
    }

    private void checkPresenter() {
        if (mPresenter == null) {
            throw new RuntimeException("you need to call setPresenter() at first");
        }
    }

    public void setFormatEnable(boolean isEnable) {
        mFormatView.setSelected(isEnable);
    }

    public void setUndoEnable(boolean isEnable) {
        mUndoView.setEnabled(isEnable);
    }

    public void setRedoEnable(boolean isEnable) {
        mRedoView.setEnabled(isEnable);
    }
}
