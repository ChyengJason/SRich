package com.jscheng.srich.note_edit;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jscheng.srich.R;

public class EditNoteFormatDialog extends Dialog implements View.OnClickListener{

    private EditNotePresenter mPresenter;

    public EditNoteFormatDialog(Context context, EditNotePresenter presenter) {
        super(context, R.style.EditNoteBottomDialogTheme);
        setContentView(R.layout.edit_note_bottom_dialog);
        this.mPresenter = presenter;

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.EditNoteBottomDialogAnimTheme);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.dialog_album).setOnClickListener(this);
        findViewById(R.id.dialog_network).setOnClickListener(this);
        findViewById(R.id.dialog_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_album:
                mPresenter.tapAlbum();
                this.dismiss();
                break;
            case R.id.dialog_network:
                mPresenter.tapNetworkUrl();
                this.dismiss();
                break;
            case R.id.dialog_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}
