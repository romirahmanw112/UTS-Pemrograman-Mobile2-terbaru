package com.example.urnote;

import android.os.Bundle; import android.widget.Button; import android.widget.EditText; import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText editTextTitle, editTextContent;
    private boolean isEditMode = false;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        databaseHelper = new DatabaseHelper(this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Cek apakah kita dalam mode edit
        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            isEditMode = true;
            noteId = intent.getIntExtra("note_id", -1);
            String title = intent.getStringExtra("note_title");
            String content = intent.getStringExtra("note_content");
            editTextTitle.setText(title);
            editTextContent.setText(content);
            buttonSave.setText("Update Note");
        }

        buttonSave.setOnClickListener(view -> {
            if (isEditMode) {
                updateNote();
            } else {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = databaseHelper.insertNote(title, content);
            if (isInserted) {
                Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean isUpdated = databaseHelper.updateNote(noteId, title, content);
            if (isUpdated) {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

