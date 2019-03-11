package com.jscheng.srich.editor;

import com.jscheng.srich.model.Options;

/**
 * Created By Chengjunsen on 2019/2/27
 */
public interface NoteEditorSelectionListener {
    void onStyleChange(int start, int end, Options options);
}
