package com.ekamard.mynotesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ekamard.mynotesapp.db.DatabaseContract.NoteColumns;

import static com.ekamard.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbnoteapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            NoteColumns._ID,
            NoteColumns.TITLE,
            NoteColumns.DESCRIPTION,
            NoteColumns.DATE);

    DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //untuk membuat table database pertama kali
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    //untuk mengupdate database bila terjadi ada perubahan
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //menggunakan variable yg terdapat pada kelas Database contract untuk mengisi kolom table jika ada perubahan pada tabel
        onCreate(db);
    }
}
