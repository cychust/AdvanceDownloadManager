package com.example.cyc.downloadproject.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by cyc on 17-10-2.
 */



public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public DBHelper(Context context) {
        super(context, "downloadtask2.db", null, 1);
    }
    public static final String CREATE_BOOK="create table download (" +
            "id integer primary key autoincrement, " +
            "url text, " +
            "state integer, " +
            "downtime integer, " +
            "filesize integer, " +
            "downloadlength integer," +
            "oldtime integer)";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
