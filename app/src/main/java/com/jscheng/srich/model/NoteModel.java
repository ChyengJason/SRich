package com.jscheng.srich.model;
import android.content.Context;
import android.util.Log;

import com.jscheng.srich.converter.decoder.ParagraphDecoder;
import com.jscheng.srich.converter.encoder.ParagraphEncoder;
import com.jscheng.srich.dao.NoteDao;
import com.jscheng.srich.utils.StorageUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class NoteModel {
    private static final String TAG = "NoteModel";
    /**
     * TODO 异步加载
     * @param note
     */
    public static Note parserParagraphs(Note note) {
        if (note != null) {
            String localPath = note.getLocalPath();
            if (localPath != null && !localPath.isEmpty()) {
                String content = StorageUtil.readFile(localPath);
                List<Paragraph> paragraphs = ParagraphDecoder.decode(content);
                note.setParagraphs(paragraphs);
                note.setDirty(false);
            }
        }
        return note;
    }

    public static Note buildNote(Context context) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        long time = System.currentTimeMillis();
        String localPath = StorageUtil.getDiskCachePath(context) + File.separator + uuid + ".txt";

        Note note = new NoteBuilder()
                .id(uuid)
                .createtime(time)
                .motifytime(time)
                .localPath(localPath)
                .build();
        return note;
    }

    public static void deleteNote(Context context, Note note) {
        NoteDao dao = new NoteDao(context);
        dao.delete(note.getId());
    }

    public static void deleteNote(Context context, String id) {
        NoteDao dao = new NoteDao(context);
        dao.delete(id);
    }

    public static Note findNote(Context context, String id) {
        NoteDao dao = new NoteDao(context);
        return dao.find(id);
    }

    public static List<Note> getNotes(Context context) {
        NoteDao dao = new NoteDao(context);
        return dao.getData();
    }

    public static void updateNote(Context context, Note note) {
        NoteDao dao = new NoteDao(context);
        if (note.isDirty()) {
            String localPath = note.getLocalPath();
            String conent = ParagraphEncoder.encode(note.getParagraphs());
            String title = conent.length() > 10 ? conent.substring(0, 10) : conent;
            String summary = conent.length() > 50 ? conent.substring(title.length(), 50) : conent.substring(title.length());
            String summaryImage = summaryImageUrl(note.getParagraphs());

            note.setTitle(title);
            note.setSummary(summary);
            note.setSummaryImageUrl(summaryImage);
            Log.e(TAG, "updateNote: " + conent);
            if (dao.find(note.getId()) != null) {
                dao.update(note);
            } else {
                dao.add(note);
            }
            StorageUtil.overwiteFile(localPath, conent);
            note.setDirty(false);
        }
    }

    private static String summaryImageUrl(List<Paragraph> paragraphs) {
        for (Paragraph paragraph : paragraphs) {
            if (paragraph.isImage()) {
                return paragraph.getImageUrl();
            }
        }
        return "";
    }

    public static boolean isNoteNull(Note note) {
        if (note.getParagraphs() == null || note.getParagraphs().isEmpty()) {
            return true;
        }
        for (Paragraph paragraph : note.getParagraphs()) {
            if (paragraph.isParagraphStyle()) {
                return false;
            }
            if (!paragraph.getWords().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
