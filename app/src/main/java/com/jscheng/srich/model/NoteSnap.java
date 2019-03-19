package com.jscheng.srich.model;

import java.util.List;

/**
 * Note的快照，用于撤销、反撤销
 * Created By Chengjunsen on 2019/3/8
 */
public class NoteSnap {
    private String id;

    private long time;

    private List<Paragraph> paragraphs;

    private int selectionStart;

    private int selectionEnd;

    private boolean isContinuousAction;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public int getSelectionStart() {
        return selectionStart;
    }

    public void setSelectionStart(int selectionStart) {
        this.selectionStart = selectionStart;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public void setSelectionEnd(int selectionEnd) {
        this.selectionEnd = selectionEnd;
    }

    public void setSelection(int start, int end) {
        this.selectionStart = start;
        this.selectionEnd = end;
    }

    public void setContinuousAction(boolean isContinuousAction) {
        this.isContinuousAction = true;
    }

    public boolean isContinuousAction() {
        return isContinuousAction;
    }
}
