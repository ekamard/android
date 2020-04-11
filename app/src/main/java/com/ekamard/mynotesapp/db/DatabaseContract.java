package com.ekamard.mynotesapp.db;

import android.provider.BaseColumns;

//kelas untuk mempermudah apabila terjadi perubahan pada struktur table
public class DatabaseContract {



    public static final class NoteColumns implements BaseColumns{
        public static String TABLE_NAME = "note";
        public static final String DATE = "date";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
    }
}
