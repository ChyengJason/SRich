package com.jscheng.srich;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.route.Router;
import com.jscheng.srich.route.RouterConfig;
import com.jscheng.srich.uitl.DateUtil;
import com.jscheng.srich.widget.FloatNewButton;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesActivity extends BaseActivity implements OutLinePresenter.OutLineView, View.OnClickListener {
    private final static String TAG = "OutLinesActivity";
    private OutLinePresenter mPresenter = null;
    private RecyclerView mRecyclerView = null;
    private LinearLayoutManager mLayoutManager = null;
    private OutLinesAdapter mRecyclerAdapter = null;
    private LinearLayout mHeadDateLayout = null;
    private TextView mHeadDateText = null;
    private FloatNewButton mFloatButton = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline);

        this.mPresenter = new OutLinePresenter();
        this.getLifecycle().addObserver(mPresenter);

        this.mHeadDateLayout = findViewById(R.id.outline_head_date);
        this.mHeadDateText = mHeadDateLayout.findViewById(R.id.date_text);
        this.mFloatButton = findViewById(R.id.float_new_button);
        this.mFloatButton.setOnClickListener(this);

        this.mRecyclerView = findViewById(R.id.outline_recyclerview);
        this.mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.mRecyclerAdapter = new OutLinesAdapter(this, mLayoutManager);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setAdapter(mRecyclerAdapter);
        this.mRecyclerView.addOnScrollListener(new ScrollChangeListener());
    }

    @Override
    public void setData(List<Note> notes) {
        mRecyclerAdapter.setData(notes);
    }

    private void tapNewButton() {
        Router.with(this).route(RouterConfig.EditNoteActivityUri).go();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_new_button:
                tapNewButton();
                break;
            default:
                break;
        }
    }

    private class ScrollChangeListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            long time = mRecyclerAdapter.getFirstVisibleDateTime();
            if (time > 0) {
                mHeadDateLayout.setVisibility(View.VISIBLE);
                mHeadDateText.setText(DateUtil.formatDate(time));
            } else {
                mHeadDateText.setVisibility(View.INVISIBLE);
            }
        }
    }
}
