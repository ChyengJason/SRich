package com.jscheng.srich;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;

import com.jscheng.srich.editor.NoteEditorBar;
import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.utils.KeyboardUtil;
import com.jscheng.srich.utils.PermissionUtil;
import com.jscheng.srich.widget.EditNoteFormatDialog;
import com.jscheng.srich.widget.EditNoteToolbar;
import com.jscheng.srich.widget.FloatEditButton;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNoteActivity extends BaseActivity implements EditNotePresenter.EditNoteView {
    private final static int ACTION_PICK_CODE = 1;
    private final static int PERMISSION_CODE = 2;

    private EditNoteToolbar mToolbar;
    private EditNotePresenter mPresenter;
    private FloatEditButton mEditButton;
    private NoteEditorText mEditorText;
    private NoteEditorBar mEditorBar;
    private EditNoteFormatDialog mFormatDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        mPresenter = new EditNotePresenter(getIntent());
        this.getLifecycle().addObserver(mPresenter);
        mEditButton = findViewById(R.id.float_edit_button);
        mEditButton.setPresenter(mPresenter);

        mEditorText = findViewById(R.id.edit_note_editor);
        mToolbar = findViewById(R.id.edit_note_toolbar);
        mToolbar.setPresenter(mPresenter);

        mEditorBar = findViewById(R.id.edit_note_bar);
        mEditorBar.setEditorText(mEditorText);
        mEditorBar.hide();

        requestPermission();
    }

    @Override
    public void writingMode(Note note, boolean isEditorBarEnable) {
        mEditorText.writingMode(note);
        mToolbar.writingMode();
        mEditorBar.setVisibility(isEditorBarEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEditorBarEnable);
    }

    @Override
    public void readingMode(Note note) {
        mEditorText.readingMode(note);
        mToolbar.readingMode();
        mEditButton.show();
        mEditorBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        mPresenter.tapBack();
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

    @Override
    public void showFormatDialog() {
        if (mFormatDialog == null) {
            mFormatDialog = new EditNoteFormatDialog(this, mPresenter);
        }
        KeyboardUtil.hideSoftInput(this, mEditorText);
        mFormatDialog.show();
    }

    @Override
    public void showAlbumDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ACTION_PICK_CODE);
    }

    @Override
    public void showNetworkDialog() {
        String url = "https://upload-images.jianshu.io/upload_images/7722639-621573aa9b77e25e.jpeg";
        mEditorText.getStyleManager().commandImage(url, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ACTION_PICK_CODE) {
                Uri uri = data.getData();
                mEditorText.getStyleManager().commandImage(uri, true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestPermission() {
        PermissionUtil.checkPermissionsAndRequest(this, PermissionUtil.STORAGE, PERMISSION_CODE, "请求访问SD卡权限被拒绝");
    }
}
