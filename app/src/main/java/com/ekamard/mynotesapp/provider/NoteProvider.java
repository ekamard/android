package com.ekamard.mynotesapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.ekamard.mynotesapp.db.NoteHelper;

import static com.ekamard.mynotesapp.db.DatabaseContract.AUTHORITY;
import static com.ekamard.mynotesapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.ekamard.mynotesapp.db.DatabaseContract.NoteColumns.TABLE_NAME;

public class NoteProvider extends ContentProvider {
    public NoteProvider() {
    }

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private NoteHelper noteHelper;

    //berfungsi sebagai pembanding uri dengan nilai integer tertentu
    private static final UriMatcher myUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /**
         * uri matcher untuk opsi pertama
        content://com.ekamard.mynotesapp/note
         mengambil all data pada database
         **/
        myUriMatcher.addURI(AUTHORITY, TABLE_NAME , NOTE);

        /**
         * uri matcher untuk opsi kedua
        content://com.ekamard.mynotesapp/note/id

         # berfungsi sebagai pengganti id nanti yang akan di input, fungsi nya mirip seperti ? dalam query atau %s dalam string
         mengambil data pada database lebih spesifik
         **/
        myUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                NOTE_ID);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        noteHelper = NoteHelper.getInstance(getContext());
        noteHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s,
                        String[] strings1, String s1) {
        // TODO: Implement this to handle query requests from clients.

        Cursor cursor;

        /**
         * identifikasi obyek uri yang akan di request
         */
        switch (myUriMatcher.match(uri)){

            //jika obyek uri cocok dengan nilai pada variable NOTE atau = 1 maka query yang akan di jalankan adalah select all data pada database
            case NOTE:
                cursor = noteHelper.queryAll();
                break;

             //jika obyej uri cocok dengan nilai pada variable NOTE_ID atau - 2 , maka query yang akan di proses adalah select data per id pada database
            case NOTE_ID:
                /**
                 * getLastPathSegment() berfungsi untuk mengambil nilai pada path terakhir sebuah uri
                 * contoh path uri  content://com.ekamard.mynotesapp/note/2
                 * maka query akan mengambil nilai id 2 dari path terakhir uri di atas
                 */
                cursor = noteHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
       return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        long added;
        switch (myUriMatcher.match(uri)){
            case NOTE:
                added = noteHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }

        //untuk menotifikasi apabila ada perubahan data pada provider dan mengirim nya ke semua apps yang menggunkan content provider ini
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" +added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String s,
                      String[] strings) {
        // TODO: Implement this to handle requests to update one or more rows.

        int updated;
        switch (myUriMatcher.match(uri)){
            case NOTE_ID:
                updated = noteHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        // Implement this to handle requests to delete one or more rows.

        int deleted;
        switch (myUriMatcher.match(uri)){
            case NOTE_ID:
                deleted = noteHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }
}
