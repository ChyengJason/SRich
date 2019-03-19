package com.jscheng.srich.outline;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.jscheng.srich.model.NoteModel;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;
import com.jscheng.srich.route.Router;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class OutLinePresenter extends IPresenter {
    private OutLineView mView;

    public interface OutLineView extends IView {
        void setData(List<Note> ntoes);
        void showCenterDialog(String id);
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
        Observable.create(new ObservableOnSubscribe<List<Note>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Note>> emitter) throws Exception {
                List<Note> notes = NoteModel.getNotes((Context)mView);
                emitter.onNext(notes);
            }
        }).subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Consumer<List<Note>>() {
            @Override
            public void accept(List<Note> notes) throws Exception {
                mView.setData(notes);
            }
        });
    }

    public void tapNew() {
        Router.with((Context)mView).route("editnote").go();
    }

    public void tapNote(String id) {
        Router.with((Context)mView).route("editnote").intent("id", id).go();
    }

    public void taplongNote(String id) {
        mView.showCenterDialog(id);
    }

    public void tapDelete(String id) {
        NoteModel.deleteNote((Context)mView, id);
        reload();
    }
}
