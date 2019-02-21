package com.jscheng.srich;

import com.jscheng.srich.model.Note;

public class NoteBuilder {
    private Note note = null;

    private NoteBuilder() {
    }

    public static NoteBuilder create(int id) {
        NoteBuilder noteBuilder =  new NoteBuilder();
        noteBuilder.note = new Note();
        noteBuilder.note.setId(id);
        return noteBuilder;
    }

    public static NoteBuilder create() {
        NoteBuilder noteBuilder =  new NoteBuilder();
        noteBuilder.note = new Note();
        return noteBuilder;
    }

    public NoteBuilder title(String title) {
        this.note.setTitle(title);
        return this;
    }

    public NoteBuilder ctime(long millTime) {
        this.note.setCreateTime(millTime);
        return this;
    }

    public NoteBuilder mtime(long millTime) {
        this.note.setModifyTime(millTime);
        return this;
    }

    public NoteBuilder summary(String summary) {
        this.note.setSummary(summary);
        return this;
    }

    public Note build() {
        return note;
    }
}
