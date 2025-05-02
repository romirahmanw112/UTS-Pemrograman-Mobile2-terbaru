package com.example.urnote;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_NOTE_REQUEST = 1;

    private TextView tvNoNotes;
    private ListView listView;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        tvNoNotes = findViewById(R.id.tvNoNotes);
        listView = findViewById(R.id.listView);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(intent, ADD_NOTE_REQUEST);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedNote = adapter.getItem(position);
            showDeleteConfirmationDialog(selectedNote);
            return true;  // Return true to indicate the long click was handled
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedNote = adapter.getItem(position);
            int noteId = databaseHelper.getNoteId(selectedNote);
            String noteContent = databaseHelper.getNoteContent(noteId);

            // Open ReadActivity to view the note
            Intent intent = new Intent(MainActivity.this, ReadActivity.class);
            intent.putExtra("note_id", noteId);
            intent.putExtra("note_title", selectedNote);
            intent.putExtra("note_content", noteContent);
            startActivity(intent);
        });
        loadNotes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(); // Refresh the notes list when returning to this activity
    }

    private void loadNotes() {
        List<String> notes = databaseHelper.getAllNotes();
        if (notes.isEmpty()) {
            tvNoNotes.setText("No notes available");
            tvNoNotes.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            tvNoNotes.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
            listView.setAdapter(adapter);
        }
    }

    private void showDeleteConfirmationDialog(String title) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    int noteId = databaseHelper.getNoteId(title);
                    boolean isDeleted = false;

                    if (noteId != -1) {
                        isDeleted = databaseHelper.deleteNoteById(noteId);
                    } else {
                        // Error - this method doesn't exist, add it to DatabaseHelper
                        Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isDeleted) {
                        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        loadNotes(); // Refresh the list
                    } else {
                        Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra("note_title");
            String content = data.getStringExtra("note_content");

            boolean isInserted = databaseHelper.insertNote(title, content);
            if (isInserted) {
                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
                loadNotes();
            } else {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}