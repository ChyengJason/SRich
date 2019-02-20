package com.jscheng.srich;

import com.bumptech.glide.Glide;
import com.jscheng.srich.model.Note;

public class NoteBuilder {
    private Note note = null;

    private NoteBuilder() {
    }

    public static NoteBuilder create() {
        NoteBuilder noteBuilder =  new NoteBuilder();
        noteBuilder.note = new Note();
        return noteBuilder;
    }

    public NoteBuilder general(String title) {
        this.note.setTitle(title);
        return this;
    }

    public Note build() {
        return note;
    }
}
