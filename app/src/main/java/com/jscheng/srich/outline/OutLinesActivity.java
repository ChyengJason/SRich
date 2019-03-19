package com.jscheng.srich.outline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jscheng.annotations.Route;
import com.jscheng.srich.BaseActivity;
import com.jscheng.srich.R;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.utils.DateUtil;

import java.util.List;

/**
 * 预览页
 * Created By Chengjunsen on 2019/2/20
 */
@Route("outline")
public class OutLinesActivity extends BaseActivity
        implements OutLinePresenter.OutLineView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private final static String TAG = "OutLinesActivity";
    private OutLinePresenter mPresenter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OutLinesAdapter mRecyclerAdapter ;
    private RelativeLayout mHeadDateLayout;
    private TextView mHeadDateText;
    private FloatNewButton mFloatButton;
    private SwipeRefreshLayout mSwipeLayout;
    private OutLineCenterDialog mCenterDialog;

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
        this.mSwipeLayout = findViewById(R.id.swipe_layout);
        this.mSwipeLayout.setOnRefreshListener(this);

        this.mRecyclerView = findViewById(R.id.outline_recyclerview);
        this.mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.mRecyclerAdapter = new OutLinesAdapter(mPresenter, mRecyclerView, mLayoutManager);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setAdapter(mRecyclerAdapter);
        this.mRecyclerView.addOnScrollListener(new ScrollChangeListener());
    }

    @Override
    public void setData(List<Note> notes) {
        mSwipeLayout.setRefreshing(false);
        mRecyclerAdapter.setData(notes);
    }

    @Override
    public void showCenterDialog(String id) {
        if (mCenterDialog == null) {
            mCenterDialog = new OutLineCenterDialog(this, mPresenter);
        }
        mCenterDialog.show(id);
    }

    private void tapNewButton() {
        mPresenter.tapNew();
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

    @Override
    public void onRefresh() {
        mPresenter.reload();
    }

    private class ScrollChangeListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            updateHeadDateLayout();
        }
    }

    private void updateHeadDateLayout() {
        long time = mRecyclerAdapter.getFirstVisibleDateTime();
        if (time > 0) {
            mHeadDateLayout.setVisibility(View.VISIBLE);
            mHeadDateText.setText(DateUtil.formatDate(time));
        } else {
            mHeadDateLayout.setVisibility(View.INVISIBLE);
        }
    }
}
