package com.jscheng.srich;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;

import org.jetbrains.annotations.NotNull;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNotePresenter extends IPresenter {
    private static final Mode DefaultMode = Mode.Reading;
    private Mode mMode = DefaultMode;
    private EditNoteView mView;
    private boolean isEditorBarEnable;
    private Note mNote;
    private String mNoteid;

    public EditNotePresenter(Intent intent) {
        mNoteid = intent.getStringExtra("id");
    }

    private enum Mode {
        Writing,
        Reading
    }

    public interface EditNoteView extends IView {
        void writingMode(Note note, boolean isEditorBarEnable);
        void readingMode(Note note);
        void finish();
        void setEditorbar(boolean isEnable);
        void showFormatDialog();
        void showAlbumDialog();
        void showNetworkDialog();
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (EditNoteView)owner;
        this.isEditorBarEnable = true;
        this.mNote = null;
        if (mNoteid != null) {
            mNote = NoteFactory.findNote((Context)mView, mNoteid);
            mNote = NoteFactory.parserParagraphs(mNote);
        }
        if (mNote == null) {
            mNote = NoteFactory.createNote((Context)mView);
        }
        this.readingMode();
    }

    @Override
    public void onDestroy(@NotNull LifecycleOwner owner) {
        this.mView = null;
    }

    public void tapNetworkUrl() {
        mView.showNetworkDialog();
    }

    public void tapAlbum() {
        mView.showAlbumDialog();
    }

    public void tapEdit() {
        writingMode();
    }

    public void tapTick() {
        readingMode();
        updateNote();
    }

    public void tapBack() {
        if (mMode == Mode.Writing) {
            readingMode();
        } else {
            updateNote();
            mView.finish();
        }
    }

    public void tapMore() {

    }

    public void tapRedo() {

    }

    public void tapUndo() {

    }

    public void tapAttach() {
        mView.showFormatDialog();
    }

    public void tapEditorBar(boolean isEnable) {
        isEditorBarEnable = isEnable;
        mView.setEditorbar(isEditorBarEnable);
    }

    private void readingMode() {
        mView.readingMode(mNote);
        mMode = Mode.Reading;
    }

    private void writingMode() {
        mView.writingMode(mNote, isEditorBarEnable);
        mMode = Mode.Writing;
    }

    private void updateNote() {
        if (NoteFactory.isNoteNull(mNote)) {
            NoteFactory.deleteNote((Context)mView, mNote);
            Toast.makeText((Context) mView, "正在删除数据", Toast.LENGTH_SHORT).show();
        } else if (mNote.isDirty()) {
            NoteFactory.updateNote((Context)mView, mNote);
            Toast.makeText((Context) mView, "正在保存数据", Toast.LENGTH_SHORT).show();
        }
    }
}
