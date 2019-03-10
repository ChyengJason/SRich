package com.jscheng.srich.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/20
 */
public class Note {
    private String id;

    private String title;

    private long createTime;

    private long modifyTime;

    private String summary;

    private String summaryImageUrl;

    private String localPath;

    private boolean isDirty;

    private List<Paragraph> paragraphs;

    public Note() {
        paragraphs = new ArrayList<>();
        isDirty = true;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        if (paragraphs != null) {
            this.paragraphs = paragraphs;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryImageUrl() {
        return summaryImageUrl;
    }

    public void setSummaryImageUrl(String summaryImageUrl) {
        this.summaryImageUrl = summaryImageUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
}
