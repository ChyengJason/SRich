package com.jscheng.srich.model;

import java.util.List;

/**
 * Created By Chengjunsen on 2019/3/8
 */
public class NoteSnap {
    private int id;

    private List<Paragraph> paragraphs;

    public NoteSnap(Note note) {
        id = note.getId();
        for (Paragraph item: note.getParagraphs()) {
            paragraphs.add(item.clone());
        }
    }
}
