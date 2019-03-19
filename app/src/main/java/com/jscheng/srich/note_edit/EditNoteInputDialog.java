package com.jscheng.srich.note_edit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jscheng.srich.R;
import com.jscheng.srich.utils.UrlUtil;

/**
 * Created By Chengjunsen on 2019/3/19
 */
public class EditNoteInputDialog implements View.OnClickListener{
    private Dialog dialog;
    private Context context;
    private AppCompatTextView titleTextView;
    private AppCompatEditText contentEditText;
    private EditNotePresenter presenter;

    public EditNoteInputDialog(Context context, EditNotePresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        init();
    }

    public String getTitle() {
        return titleTextView.getText().toString();

    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public String getContent() {
        return contentEditText.getText().toString();
    }

    public void setContent(String content) {
        contentEditText.setText(content);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    private void init() {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_note_input_dialog);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        layoutParams.width = (int) (displayMetrics.widthPixels * 1);
        window.setAttributes(layoutParams);
        titleTextView = dialog.findViewById(R.id.titleTextView);
        titleTextView.setOnClickListener(this);
        contentEditText = dialog.findViewById(R.id.contentEditText);
        dialog.findViewById(R.id.cancelTextView).setOnClickListener(this);
        dialog.findViewById(R.id.confirmTextView).setOnClickListener(this);
        titleTextView.setText("输入");
        contentEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmTextView:
                confirm();
                break;
            case R.id.cancelTextView:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void confirm() {
        String url = getContent();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!UrlUtil.isUrl(url)){
            Toast.makeText(context, "链接不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        presenter.tapInsertUrl(url);
    }
}
