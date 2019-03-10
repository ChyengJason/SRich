package com.jscheng.srich.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import org.jetbrains.annotations.NotNull;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public abstract class IPresenter implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected abstract void onCreate(@NotNull LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected abstract void onDestroy(@NotNull LifecycleOwner owner);

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    protected void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {
        switch (event) {
            case ON_RESUME:
                onResume();
                break;
            case ON_START:
                onStart();
                break;
            case ON_STOP:
                onStop();
                break;
            case ON_PAUSE:
                onPause();
                break;
            default:
                break;
        }
    }

    protected void onResume() {

    }

    protected void onStart() {

    }

    protected void onPause() {

    }

    protected void onStop() {

    }
}
