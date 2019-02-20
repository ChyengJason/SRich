package com.jscheng.srich;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;

import org.jetbrains.annotations.NotNull;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinePresenter implements IPresenter {
    private OutLineView mView;

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (OutLineView) owner;
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        this.mView = null;
    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }

    public interface OutLineView extends IView {

    }

}
