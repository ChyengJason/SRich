package com.jscheng.srich.outline;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.OutLine;
import com.jscheng.srich.utils.DateUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created By Chengjunsen on 2019/2/21
 */
public class OutLineModel {

    public static List<OutLine> build(List<Note> notes) {
        List<OutLine> outLines = new ArrayList<>();
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note note1, Note note2) {
                return (int)(note2.getModifyTime() - note1.getModifyTime());
            }
        });

        OutLine newDateOutLine = null;
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (newDateOutLine == null || !DateUtil.isSameDate(newDateOutLine.getTime(), note.getModifyTime())) {
                newDateOutLine = buildDateOutLine(note);
                outLines.add(newDateOutLine);
            }
            outLines.add(new OutLine(note));
        }
        return outLines;
    }

    private static OutLine buildDateOutLine(Note note) {
        return new OutLine(note.getModifyTime());
    }
}
