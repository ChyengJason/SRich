package com.jscheng.srich.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/18
 */
public class NoteSnapBuilder {

    private NoteSnap snap;

    public NoteSnapBuilder(Note note) {
        snap = new NoteSnap();
        snap.setId(note.getId());

        List<Paragraph> list = new ArrayList<>();
        for (Paragraph item: note.getParagraphs()) {
            list.add(item.clone());
        }
        snap.setParagraphs(list);
    }

    public NoteSnapBuilder selection(int start, int end) {
        snap.setSelection(start, end);
        return this;
    }

    public NoteSnap build() {
        snap.setTime(System.currentTimeMillis());
        return snap;
    }

    public NoteSnapBuilder continuous(boolean continuousAction) {
        snap.setContinuousAction(true);
        return this;
    }
}
