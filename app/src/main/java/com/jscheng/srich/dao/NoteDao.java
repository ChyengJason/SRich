package com.jscheng.srich.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.NoteBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具
 */
public class NoteDao {
    private NoteDboOpener mNoteDboOpener;

    public NoteDao(Context context) {
        mNoteDboOpener = new NoteDboOpener(context);
    }

    private ContentValues getContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", note.getId());
        contentValues.put("title", note.getTitle());
        contentValues.put("createTime", note.getCreateTime());
        contentValues.put("modifyTime", note.getModifyTime());
        contentValues.put("summary", note.getSummary());
        contentValues.put("summaryImageUrl", note.getSummaryImageUrl());
        contentValues.put("localPath", note.getLocalPath());
        return contentValues;
    }

    public long add(Note note) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.insert(NoteDboOpener.NoteTable, null, getContentValues(note));
    }

    public long add(ContentValues contentValues) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.insert(NoteDboOpener.NoteTable, null, contentValues);
    }

    public int delete(String id) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.delete(NoteDboOpener.NoteTable, "id=?", new String[]{ id });
    }

    public int delete(String selection, String[] selectionArgs) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.delete(NoteDboOpener.NoteTable, selection, selectionArgs);
    }

    public int update(Note note) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.update(NoteDboOpener.NoteTable, getContentValues(note), "id=?", new String[]{ note.getId() });
    }

    public int update(String selection, String[] selectionArgs, ContentValues values) {
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.update(NoteDboOpener.NoteTable, values, selection, selectionArgs);
    }

    public int update(Integer id, ContentValues contentValues) {
        String idString = String.valueOf(id);
        SQLiteDatabase database = mNoteDboOpener.getWritableDatabase();
        return database.update(NoteDboOpener.NoteTable, contentValues, "id=?", new String[]{ idString });
    }

    public Note find(String id) {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, "id=?", selectionArgs,
                null, null, null, null);

        Note note = null;
        if (cursor.moveToNext()) {
            note = new NoteBuilder()
                    .id(cursor.getString(0))
                    .title(cursor.getString(1))
                    .createtime(cursor.getLong(2))
                    .motifytime(cursor.getLong(3))
                    .summary(cursor.getString(4))
                    .summaryImageUrl(cursor.getString(5))
                    .localPath(cursor.getString(6))
                    .build();
        }
        cursor.close();
        return note;
    }

    public Cursor query(int id, String orderSort) {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, "id=?", selectionArgs,
                null, null, orderSort, null);
        return cursor;
    }

    public Cursor query(String selection, String[] selectionArgs, String orderSort) {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, selection, selectionArgs,
                null, null, orderSort, null);
        return cursor;
    }

    public int getCount() {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, null, null,
                null, null, null, null);
        return cursor.getCount();
    }

    public List<Note> getOffetsData(int offset, int limit) {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        String limitString = String.valueOf(offset) + "," + String.valueOf(limit);
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, null, null,
                null, null, null, limitString);
        List<Note> list = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Note note = new NoteBuilder()
                    .id(cursor.getString(0))
                    .title(cursor.getString(1))
                    .createtime(cursor.getLong(2))
                    .motifytime(cursor.getLong(3))
                    .summary(cursor.getString(4))
                    .summaryImageUrl(cursor.getString(5))
                    .localPath(cursor.getString(6))
                    .build();

            list.add(note);
        }
        return list;
    }

    public List<Note> getData() {
        SQLiteDatabase database = mNoteDboOpener.getReadableDatabase();
        Cursor cursor = database.query(NoteDboOpener.NoteTable,
                null, null, null,
                null, null, null, null);
        List<Note> list = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            Note note = new NoteBuilder()
                    .id(cursor.getString(0))
                    .title(cursor.getString(1))
                    .createtime(cursor.getLong(2))
                    .motifytime(cursor.getLong(3))
                    .summary(cursor.getString(4))
                    .summaryImageUrl(cursor.getString(5))
                    .localPath(cursor.getString(6))
                    .build();

            list.add(note);
        }
        return list;
    }

}
