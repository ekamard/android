package com.ekamard.mynotesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.ekamard.mynotesapp.adapter.NoteAdapter;
import com.ekamard.mynotesapp.db.NoteHelper;
import com.ekamard.mynotesapp.entity.Note;
import com.ekamard.mynotesapp.helper.MappingHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadNotesCallback  {

    private ProgressBar myProgressBar;
    private RecyclerView rvNotes;
    private NoteAdapter adapter;
    private FloatingActionButton fabAdd;
    private NoteHelper myNoteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Notes");

        myProgressBar = findViewById(R.id.progressbar);
        rvNotes = findViewById(R.id.rv_notes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        adapter = new NoteAdapter(this);
        rvNotes.setAdapter(adapter);

        fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadData = new Intent(MainActivity.this, NoteAddUpdateActivity.class);
                startActivityForResult(loadData , NoteAddUpdateActivity.REQUEST_ADD);
            }
        });

        myNoteHelper = NoteHelper.getInstance(getApplicationContext());
        myNoteHelper.open();

        if (savedInstanceState == null){
            new LoadNotesAsync(myNoteHelper, this).execute();
        } else {
            ArrayList<Note> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null){
                adapter.setListNotes(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getListNotes());
    }

    @Override
    public void preExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Note> notes) {
        myProgressBar.setVisibility(View.INVISIBLE);
        if (notes.size() > 0 ){
            adapter.setListNotes(notes);
        } else {
            adapter.setListNotes(new ArrayList<Note>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<Note>>{
        private final WeakReference<NoteHelper> weakNoteHelper;
        private final WeakReference<LoadNotesCallback> weakCallback;

        private LoadNotesAsync(NoteHelper myNoteHelper , LoadNotesCallback callback){
            weakNoteHelper = new WeakReference<>(myNoteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            Cursor dataCursor = weakNoteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == NoteAddUpdateActivity.REQUEST_ADD) {
                if (resultCode == NoteAddUpdateActivity.RESULT_ADD) {
                    Note myNote = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);

                    adapter.addItem(myNote);
                    rvNotes.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage("Satu item berhasil ditambahkan");
                }
            } else if (requestCode == NoteAddUpdateActivity.REQUEST_UPDATE) {
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    Note myNote = data.getParcelableExtra(NoteAddUpdateActivity.EXTRA_NOTE);
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);

                    adapter.updateItem(position, myNote);
                    rvNotes.smoothScrollToPosition(position);

                    showSnackbarMessage("Satu item berhasil di ubah");
                } else if (resultCode == NoteAddUpdateActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(NoteAddUpdateActivity.EXTRA_POSITION, 0);
                    adapter.removeItem(position);
                    showSnackbarMessage("Satu item berhasil dihapus");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myNoteHelper.close();
    }

    private void showSnackbarMessage (String message){
        Snackbar.make(rvNotes , message , Snackbar.LENGTH_SHORT).show();
    }
}

interface LoadNotesCallback{

    void preExecute();
    void postExecute(ArrayList<Note> notes);
}
