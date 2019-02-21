package com.jscheng.srich;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinePresenter implements IPresenter {
    private OutLineView mView;

    public interface OutLineView extends IView {
        void setData(List<Note> ntoes);
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (OutLineView) owner;
        this.init();
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        this.mView = null;
    }

    @Override
    public void onLifecycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }

    private void init() {
        List<Note> datas = generalData();
        this.mView.setData(datas);
    }

    private List<Note> generalData() {
        List<Note> notes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Note note = NoteBuilder.create().mtime(System.nanoTime()).title("note " + i).build();
            notes.add(note);
        }
        return notes;
    }

}
