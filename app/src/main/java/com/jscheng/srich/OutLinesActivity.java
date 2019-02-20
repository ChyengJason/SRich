package com.jscheng.srich;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinesActivity extends BaseActivity {
    private OutLinePresenter mPresenter = null;
    private RecyclerView mRecyclerView = null;
    private RecyclerView.LayoutManager mLayoutManager = null;
    private RecyclerView.Adapter mRecyclerAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outline);

        this.mPresenter = new OutLinePresenter();
        this.getLifecycle().addObserver(mPresenter);

        this.mRecyclerView = findViewById(R.id.outline_recyclerview);
        this.mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        this.mRecyclerAdapter = new OutLinesRecyclerViewAdapter(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setAdapter(mRecyclerAdapter);
    }
}
