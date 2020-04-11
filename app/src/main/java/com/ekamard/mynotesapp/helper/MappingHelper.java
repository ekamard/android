package com.ekamard.mynotesapp.helper;

import android.database.Cursor;

import com.ekamard.mynotesapp.db.DatabaseContract;
import com.ekamard.mynotesapp.entity.Note;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<Note> mapCursorToArrayList (Cursor notesCusrsor){
        ArrayList<Note> notesList = new ArrayList<>();

        while (notesCusrsor.moveToNext()){
            int id = notesCusrsor.getInt(notesCusrsor.getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID));
            String title = notesCusrsor.getString(notesCusrsor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.TITLE));
            String description = notesCusrsor.getString(notesCusrsor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DESCRIPTION));
            String date = notesCusrsor.getString(notesCusrsor.getColumnIndexOrThrow(DatabaseContract.NoteColumns.DATE));
            notesList.add(new Note(id ,title ,description ,date));
        }
        return notesList;
    }

}
