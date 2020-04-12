package com.ekamard.consumerapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

//kelas untuk mempermudah apabila terjadi perubahan pada struktur table
public class DatabaseContract {

    //untuk mengidentifikasi bahwa provider NoteProvider milik MyNotesApp yang akan di akses
    public static final String AUTHORITY = "com.ekamard.mynotesapp";

    //untuk menentukan scheme, maka scheme yang tercita untuk nilai di bawah adalah content://
    private static final String SCHEME = "content";

    private DatabaseContract(){

    }
    public static final class NoteColumns implements BaseColumns{
        public static String TABLE_NAME = "note";
        public static final String DATE = "date";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";

        //untuk mengakses uri content provider apps string : content://com.ekamard.mynotesapp/note
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
