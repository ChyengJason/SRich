package com.jscheng.srich;
import android.content.Context;

import com.jscheng.srich.dao.NoteDao;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.utils.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteFactory {

    /**
     * parser note paragraphs
     * @param note
     */
    public static Note parserParagraphs(Note note) {
        String localPath = note.getLocalPath();
        if (localPath != null && !localPath.isEmpty()) {
            String content = StorageUtil.readFile(localPath);
            List<Paragraph> paragraphs = decodeParagraphs(content);
            note.setParagraphs(paragraphs);
        }
        return note;
    }

    public static Note createNote(Context context) {
        Note note = new Note();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        long time = System.currentTimeMillis();
        String localPath = StorageUtil.getDiskCachePath(context) + File.separator + uuid + ".txt";

        note.setId(uuid);
        note.setCreateTime(time);
        note.setModifyTime(time);
        note.setLocalPath(localPath);
        return note;
    }

    public static void addNote(Context context, Note note) {
        NoteDao dao = new NoteDao(context);
        dao.add(note);
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
            String conent = encodeParagraphs(note.getParagraphs());
            String title = conent.length() > 10 ? conent.substring(0, 10) : conent;
            String summary = conent.length() > 50 ? conent.substring(title.length(), 50) : conent.substring(title.length());
            String summaryImage = summaryImageUrl(note.getParagraphs());

            note.setTitle(title);
            note.setSummary(summary);
            note.setSummaryImageUrl(summaryImage);

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
        return new String();
    }

    private static List<Paragraph> decodeParagraphs(String content) {
        List<Paragraph> paragraphs = new ArrayList<>();
        for (String item: content.split("\n")) {
            Paragraph paragraph = new Paragraph();
            List<Integer> wordstyles = new ArrayList<>();
            for (int i = 0; i < item.length(); i++) {
                wordstyles.add(0);
            }
            paragraph.addWords(item, wordstyles);
        }
        return paragraphs;
    }

    private static String encodeParagraphs(List<Paragraph> paragraphs) {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < paragraphs.size() - 1; i++) {
            Paragraph item = paragraphs.get(i);
            content.append(item.getWords());
            content.append("\n");
        }
        if (!paragraphs.isEmpty()) {
            content.append(paragraphs.get(paragraphs.size() - 1).getWords());
        }
        return content.toString();
    }

    public static boolean isNoteNull(Note note) {
        return note.getParagraphs() == null || note.getParagraphs().isEmpty();
    }
}
