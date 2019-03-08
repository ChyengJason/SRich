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
    private boolean isEditorBarEnable;

    private enum Mode {
        Writing,
        Reading
    }

    public interface EditNoteView extends IView {
        void writingMode(boolean isEditorBarEnable);
        void readingMode();
        void finish();
        void setEditorbar(boolean isEnable);
        void insertImage();
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (EditNoteView)owner;
        this.isEditorBarEnable = true;
        this.readingMode();
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

    public void tapRedo() {

    }

    public void tapUndo() {

    }

    public void tapAttach() {
        mView.insertImage();
    }

    public void tapEditorBar(boolean isEnable) {
        isEditorBarEnable = isEnable;
        mView.setEditorbar(isEditorBarEnable);
    }

    private void readingMode() {
        mView.readingMode();
        mMode = Mode.Reading;
    }

    private void writingMode() {
        mView.writingMode(isEditorBarEnable);
        mMode = Mode.Writing;
    }

    public void pressBack() {
        if (mMode == Mode.Writing) {
            readingMode();
        } else {
            mView.finish();
        }
    }

}
