package com.jscheng.srich;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
    }

    @Override
    public void writingMode() {
        mEditorText.writingMode();
        mToolbar.writingMode();
    }

    @Override
    public void readingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.show();
    }

    @Override
    public void onBackPressed() {
        mPresenter.pressBack();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
