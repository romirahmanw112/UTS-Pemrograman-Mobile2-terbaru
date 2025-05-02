package com.example.urnote;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReadActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent;
    private Button btnEdit;
    private int noteId;
    private String noteTitle, noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        tvTitle = findViewById(R.id.tvNoteTitle);
        tvContent = findViewById(R.id.tvNoteContent);
        btnEdit = findViewById(R.id.btnEditNote);

        // Get note data from intent
        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getIntExtra("note_id", -1);
            noteTitle = intent.getStringExtra("note_title");
            noteContent = intent.getStringExtra("note_content");

            // Display note data
            tvTitle.setText(noteTitle);
            tvContent.setText(noteContent);
        }

        // In onCreate method after setting up other UI elements:
        Button btnDelete = findViewById(R.id.btnDeleteNote);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        // Handle edit button click
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ReadActivity.this, AddNoteActivity.class);
                editIntent.putExtra("note_id", noteId);
                editIntent.putExtra("note_title", noteTitle);
                editIntent.putExtra("note_content", noteContent);
                startActivity(editIntent);
                finish();
            }
        });
    }

    // Add this method to show delete confirmation dialog
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    boolean isDeleted = databaseHelper.deleteNoteById(noteId);

                    if (isDeleted) {
                        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Return to previous activity
                    } else {
                        Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}