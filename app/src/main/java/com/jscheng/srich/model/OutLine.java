package com.jscheng.srich.model;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class OutLine {
    public enum Type {
        Note, Date
    }

    private Note note;

    private long time;

    private Type type;

    public OutLine(Note note) {
        this.type = Type.Note;
        this.note = note;
    }

    public OutLine(long time) {
        this.type = Type.Date;
        this.time = time;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Type getType() {
        return type;
    }

}
