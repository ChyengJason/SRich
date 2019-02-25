package com.jscheng.srich;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;

import org.jetbrains.annotations.NotNull;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNotePresenter implements IPresenter {
    private static final Mode DefaultMode = Mode.Reading;
    private Mode mMode = DefaultMode;
    private EditNoteView mView;

    private enum Mode {
        Writing,
        Reading
    }

    public interface EditNoteView extends IView {
        void writingMode();
        void readingMode();
        void finish();
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (EditNoteView)owner;
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        this.mView = null;
    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }

    public void tapEdit() {
        writingMode();
    }

    public void tapTick() {
        readingMode();
    }

    public void tapBack() {

    }

    public void tapMore() {

    }

    private void readingMode() {
        mView.readingMode();
        mMode = Mode.Reading;
    }

    private void writingMode() {
        mView.writingMode();
        mMode = Mode.Writing;
    }

    public void pressBack() {
        if (mMode == Mode.Writing) {
            mView.readingMode();
        } else {
            mView.finish();
        }
    }

}
