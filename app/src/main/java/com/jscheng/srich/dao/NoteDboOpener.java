package com.jscheng.srich.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDboOpener extends SQLiteOpenHelper {
    public static final String DataBaseName = "srich.db";
    public static final int DataBaseVersion = 1;
    public static final String NoteTable = "note";

    public static String DataCreateTableSql = "create table " + NoteTable + " (" +
            "id varchar(200) primary key not null, " +
            "title varchar(200) , " +
            "createTime bigint , " +
            "modifyTime bigint ," +
            "summary varchar(200) , " +
            "summaryImageUrl varchar(200) , " +
            "localPath varchar(200)" +
            ");";

    public NoteDboOpener(Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataCreateTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
