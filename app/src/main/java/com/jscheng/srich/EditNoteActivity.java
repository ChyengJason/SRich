package com.jscheng.srich;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.widget.FloatEditButton;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteActivity extends BaseActivity implements EditNotePresenter.EditNoteView, View.OnClickListener {

    private EditNotePresenter mPresenter;
    private FloatEditButton mEditButton;
    private NoteEditorText mEditorText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        mEditButton = findViewById(R.id.float_edit_button);
        mEditButton.setOnClickListener(this);
        mEditorText = findViewById(R.id.edit_note_editor);
        mPresenter = new EditNotePresenter();
        this.getLifecycle().addObserver(mPresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_edit_button:
                mPresenter.tapEdit();
                break;
            default:
                break;
        }
    }

    @Override
    public void writingMode() {
        mEditorText.writingMode();
    }

    @Override
    public void readingMode() {
        mEditorText.readingMode();
    }
}
