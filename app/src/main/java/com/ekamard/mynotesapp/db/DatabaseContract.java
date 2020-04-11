package com.ekamard.mynotesapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

//kelas untuk mempermudah apabila terjadi perubahan pada struktur table
public class DatabaseContract {

    public static final String AUTHORITY = "com.ekamard.mynotesapp";
    private static final String SCHEME = "content";

    private DatabaseContract(){

    }
    public static final class NoteColumns implements BaseColumns{
        public static String TABLE_NAME = "note";
        public static final String DATE = "date";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";

        //create URI content://com.ekamard.mynotesapp/note
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
