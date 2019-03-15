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
    private static final int MAX_TITLE_LENGTH = 10;
    private static final int MAX_SUMMARY_LENGTH = 30;

    public static Note openNote(Note note) {
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

    public static Note createNote(Context context) {
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
        if (note.isDirty()) {
            updateNoteProperity(note);

            NoteDao dao = new NoteDao(context);
            String conent = ParagraphEncoder.encode(note.getParagraphs());
            String localPath = note.getLocalPath();

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

    private static void updateNoteProperity(Note note) {
        String title = "";
        String summaryImage = "";
        StringBuilder summary = new StringBuilder();

        for (Paragraph paragraph : note.getParagraphs()) {
            if (summaryImage.isEmpty() && paragraph.isImage()) {
                summaryImage = paragraph.getImageUrl();
            }
            if (paragraph.getLength() > 0) {
                String words = paragraph.getWords();
                int len = words.length();
                if (title.isEmpty()) {
                    title = words.substring(0, Math.min(MAX_TITLE_LENGTH, len));
                    words = words.substring(Math.min(MAX_TITLE_LENGTH, len));
                }

                len = words.length();
                int summaryLackLen = MAX_SUMMARY_LENGTH - len;
                if (summaryLackLen > 0) {
                    summary.append(words.substring(0, Math.min(summaryLackLen, len)));
                }
            }
        }
        note.setTitle(title);
        note.setSummary(summary.toString());
        note.setSummaryImageUrl(summaryImage);
        note.setModifyTime(System.currentTimeMillis());
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
