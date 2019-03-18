package com.jscheng.srich.note_edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;

import com.jscheng.annotations.Route;
import com.jscheng.srich.BaseActivity;
import com.jscheng.srich.R;
import com.jscheng.srich.editor.NoteEditorBar;
import com.jscheng.srich.editor.NoteEditorClickListener;
import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.utils.KeyboardUtil;
import com.jscheng.srich.utils.PermissionUtil;
import com.jscheng.srich.widget.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created By Chengjunsen on 2019/2/21
 */
@Route("editnote")
public class EditNoteActivity extends BaseActivity implements EditNotePresenter.EditNoteView, NoteEditorClickListener {
    private final static int ACTION_PICK_CODE = 1;
    private final static int PERMISSION_CODE = 2;

    private EditNoteToolbar mToolbar;
    private EditNotePresenter mPresenter;
    private FloatEditButton mEditButton;
    private NoteEditorText mEditorText;
    private NoteEditorBar mEditorBar;
    private EditNoteFormatDialog mFormatDialog;
    private CircularProgressView mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        mPresenter = new EditNotePresenter(getIntent());
        this.getLifecycle().addObserver(mPresenter);
        mEditButton = findViewById(R.id.float_edit_button);
        mEditButton.setPresenter(mPresenter);

        mLoadingView = findViewById(R.id.circular_progress);
        mLoadingView.setVisibility(View.GONE);

        mEditorText = findViewById(R.id.edit_note_editor);
        mEditorText.getStyleManager().addClickListener(this);

        mToolbar = findViewById(R.id.edit_note_toolbar);
        mToolbar.setPresenter(mPresenter);

        mEditorBar = findViewById(R.id.edit_note_bar);
        mEditorBar.setEditorText(mEditorText);
        mEditorBar.hide();

        requestPermission();
    }

    @Override
    public void setNote(Note note) {
        mEditorText.reset(note);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void writingMode(boolean isEditorBarEnable) {
        mEditorText.writingMode();
        mToolbar.writingMode();
        mEditorBar.setVisibility(isEditorBarEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEditorBarEnable);
        mEditButton.hide();
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void readingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.show();
        mEditorBar.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void loadingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.setVisibility(View.GONE);
        mEditorBar.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
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
        List<String> urls = new ArrayList<>();
        urls.add("https://upload-images.jianshu.io/upload_images/7722639-621573aa9b77e25e.jpeg");
        urls.add("https://upload-images.jianshu.io/upload_images/5185243-17b362f03fada750.png");
        urls.add("https://upload-images.jianshu.io/upload_images/851787-05da829df447c9a4.jpg");
        urls.add("https://upload-images.jianshu.io/upload_images/5279191-ffd9e07ef242457d.png");
        urls.add("https://upload-images.jianshu.io/upload_images/1781119-076ca4fe67b6cf5e.jpg");

        Random random = new Random();
        String url = urls.get(random.nextInt(urls.size()));
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

    @Override
    public void onClickImage(List<String> urls, int index) {
        mPresenter.tapImage(urls, index);
    }
}
