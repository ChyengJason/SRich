package com.jscheng.srich.edit_note;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jscheng.srich.model.NoteModel;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.mvp.IPresenter;
import com.jscheng.srich.mvp.IView;

import org.jetbrains.annotations.NotNull;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class EditNotePresenter extends IPresenter {
    private static final EditNoteMode DefaultMode = EditNoteMode.Reading;
    private EditNoteMode mMode = DefaultMode;
    private EditNoteView mView;
    private boolean isEditorBarEnable;
    private Note mNote;
    private String mNoteid;

    public EditNotePresenter(Intent intent) {
        mNoteid = intent.getStringExtra("id");
    }

    public interface EditNoteView extends IView {
        void writingMode(boolean isEditorBarEnable);
        void readingMode();
        void finish();
        void setEditorbar(boolean isEnable);
        void showFormatDialog();
        void showAlbumDialog();
        void showNetworkDialog();
        void resetNote(Note note);
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        this.mView = (EditNoteView)owner;
        this.isEditorBarEnable = true;
        this.mNote = null;
        this.initOrCreateNote(mNoteid);
        this.readingMode();
    }

    /**
     * todo 异步加载转圈
     * @param noteid
     */
    private void initOrCreateNote(String noteid) {
        if (noteid != null) {
            mNote = NoteModel.findNote((Context)mView, noteid);
            mNote = NoteModel.parserParagraphs(mNote);
        }
        if (mNote == null) {
            mNote = NoteModel.buildNote((Context)mView);
        }
        mView.resetNote(mNote);
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
        if (mMode == EditNoteMode.Writing) {
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
        mView.readingMode();
        mMode = EditNoteMode.Reading;
    }

    private void writingMode() {
        mView.writingMode(isEditorBarEnable);
        mMode = EditNoteMode.Writing;
    }

    private void updateNote() {
        if (NoteModel.isNoteNull(mNote)) {
            NoteModel.deleteNote((Context)mView, mNote);
            Toast.makeText((Context) mView, "正在删除数据", Toast.LENGTH_SHORT).show();
        } else if (mNote.isDirty()) {
            NoteModel.updateNote((Context)mView, mNote);
            Toast.makeText((Context) mView, "正在保存数据", Toast.LENGTH_SHORT).show();
        }
    }
}
