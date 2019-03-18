package com.jscheng.srich.note_edit;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jscheng.srich.R;
import com.jscheng.srich.editor.INoteEditorManager;
import com.jscheng.srich.editor.NoteEditorSelectionListener;
import com.jscheng.srich.model.Options;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteToolbar extends LinearLayout implements View.OnClickListener, NoteEditorSelectionListener {
    private ImageView mBackView;
    private ImageView mMoreView;
    private ImageView mRetrokeView;
    private ImageView mRecoverView;
    private ImageView mFormatView;
    private ImageView mAttachView;
    private ImageView mTickView;
    private EditNotePresenter mPresenter;
    private INoteEditorManager mStyleManager;

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

        mRetrokeView = findViewById(R.id.retroke_btn);
        mRetrokeView.setEnabled(false);
        mRetrokeView.setOnClickListener(this);

        mRecoverView = findViewById(R.id.recover_btn);
        mRecoverView.setEnabled(false);
        mRecoverView.setOnClickListener(this);

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
        mRecoverView.setVisibility(VISIBLE);
        mRetrokeView.setVisibility(VISIBLE);
        mAttachView.setVisibility(VISIBLE);
        mFormatView.setVisibility(VISIBLE);
    }

    public void readingMode() {
        mBackView.setVisibility(VISIBLE);
        mTickView.setVisibility(GONE);
        mRecoverView.setVisibility(GONE);
        mRetrokeView.setVisibility(GONE);
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
            case R.id.retroke_btn:
                mPresenter.tapRetroke(mStyleManager.commandRetroke());
                break;
            case R.id.recover_btn:
                mPresenter.tapRecover(mStyleManager.commandRecover());
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

    public void setRetrokeEnable(boolean isEnable) {
        mRecoverView.setEnabled(isEnable);
    }

    public void setRecoverEnable(boolean isEnable) {
        mRetrokeView.setEnabled(isEnable);
    }

    @Override
    public void onStyleChange(int start, int end, Options options) {
        setRetrokeEnable(options.isCanRetroke());
        setRecoverEnable(options.isCanRecover());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mStyleManager != null) {
            mStyleManager.removeSelectionChangeListener(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mStyleManager != null) {
            mStyleManager.addSelectionChangeListener(this);
        }
    }

    public void setStyleManager(INoteEditorManager editorManager) {
        this.mStyleManager = editorManager;
        this.mStyleManager.addSelectionChangeListener(this);
    }
}
