package com.jscheng.srich;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jscheng.srich.editor.NoteEditorBar;
import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.widget.EditNoteToolbar;
import com.jscheng.srich.widget.FloatEditButton;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteActivity extends BaseActivity implements EditNotePresenter.EditNoteView {

    private EditNoteToolbar mToolbar;
    private EditNotePresenter mPresenter;
    private FloatEditButton mEditButton;
    private NoteEditorText mEditorText;
    private NoteEditorBar mEditorBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        mPresenter = new EditNotePresenter();
        this.getLifecycle().addObserver(mPresenter);
        mEditButton = findViewById(R.id.float_edit_button);
        mEditButton.setPresenter(mPresenter);

        mEditorText = findViewById(R.id.edit_note_editor);
        mToolbar = findViewById(R.id.edit_note_toolbar);
        mToolbar.setPresenter(mPresenter);

        mEditorBar = findViewById(R.id.edit_note_bar);
        mEditorBar.setEditorText(mEditorText);
        mEditorBar.hide();
    }

    @Override
    public void writingMode(boolean isEditorBarEnable) {
        mEditorText.writingMode();
        mToolbar.writingMode();
        mEditorBar.setVisibility(isEditorBarEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEditorBarEnable);

    }

    @Override
    public void readingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.show();
        mEditorBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        mPresenter.pressBack();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void setEditorbar(boolean isEnable) {
        mEditorBar.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEnable);
    }

}
