package com.jscheng.srich.outline;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jscheng.srich.R;

/**
 * Created By Chengjunsen on 2019/3/19
 */
public class OutLineCenterDialog extends Dialog implements View.OnClickListener{

    private OutLinePresenter mPresenter;
    private String noteid;

    public OutLineCenterDialog(@NonNull Context context, OutLinePresenter mPresenter) {
        super(context);
        setContentView(R.layout.outline_center_dialog);
        this.mPresenter = mPresenter;

        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.delete_view).setOnClickListener(this);
    }

    public void show(String noteid) {
        this.noteid = noteid;
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_view:
                mPresenter.tapDelete(noteid);
                super.dismiss();
                break;
            default:
                break;
        }
    }
}
