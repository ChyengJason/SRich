package com.jscheng.srich.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jscheng.srich.EditNotePresenter;
import com.jscheng.srich.R;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteToolbar extends LinearLayout implements View.OnClickListener {
    private ImageView mBackView;
    private ImageView mMoreView;
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
    }

    public void setPresenter(EditNotePresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    public void writingMode() {
        mBackView.setVisibility(GONE);
        mTickView.setVisibility(VISIBLE);
    }

    public void readingMode() {
        mBackView.setVisibility(VISIBLE);
        mTickView.setVisibility(GONE);
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
            default:
                break;
        }
    }

    private void checkPresenter() {
        if (mPresenter == null) {
            throw new RuntimeException("you need to call setPresenter() at first");
        }
    }
}
