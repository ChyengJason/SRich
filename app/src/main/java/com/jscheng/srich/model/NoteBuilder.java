package com.jscheng.srich.model;

/**
 * Created By Chengjunsen on 2019/3/13
 */
public class NoteBuilder {
    private Note note;

    public NoteBuilder() {
        this.note = new Note();
    }

    public Note build() {
        return note;
    }

    public NoteBuilder id(String id) {
        note.setId(id);
        return this;
    }

    public NoteBuilder title(String title) {
        note.setTitle(title);
        return this;
    }

    public NoteBuilder createtime(long time) {
        note.setCreateTime(time);
        return this;
    }

    public NoteBuilder motifytime(long time) {
        note.setModifyTime(time);
        return this;
    }

    public NoteBuilder summary(String summary) {
        note.setSummary(summary);
        return this;
    }

    public NoteBuilder summaryImageUrl(String url) {
        note.setSummaryImageUrl(url);
        return this;
    }

    public NoteBuilder localPath(String path) {
        note.setLocalPath(path);
        return this;
    }
}
