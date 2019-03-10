package com.jscheng.srich;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;
import com.jscheng.srich.route.Router;
import com.jscheng.srich.route.RouterConfig;

import org.jetbrains.annotations.NotNull;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinePresenter extends IPresenter {
    private OutLineView mView;

    public interface OutLineView extends IView {
        void setData(List<Note> ntoes);
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (OutLineView) owner;
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        this.mView = null;
    }

    @Override
    protected void onResume() {
        this.reload();
    }

    public void reload() {
        List<Note> notes = NoteFactory.getNotes((Context)mView);
        this.mView.setData(notes);
    }

    public void tapNew() {
        Router.with((Context)mView).route(RouterConfig.EditNoteActivityUri).go();
    }
}
