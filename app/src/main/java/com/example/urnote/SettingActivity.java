package com.example.urnote;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Inisialisasi tombol kembali
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton btnBack = findViewById(R.id.btnBack);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout btnSimpan = findViewById(R.id.btnSimpan);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout btnEdit = findViewById(R.id.btnEdit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout btnHapus = findViewById(R.id.btnHapus);

        // Handle tombol kembali
        btnBack.setOnClickListener(v -> finish());

        // Handle tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            Toast.makeText(SettingActivity.this, "Catatan disimpan", Toast.LENGTH_SHORT).show();
            // Tambahkan logika penyimpanan di sini
        });

        // Handle tombol Edit
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(SettingActivity.this, "Edit catatan", Toast.LENGTH_SHORT).show();
            // Tambahkan logika edit di sini
        });

        // Handle tombol Hapus
        btnHapus.setOnClickListener(v -> {
            Intent intent = getIntent();
            if (intent.hasExtra("note_id")) {
                int noteId = intent.getIntExtra("note_id", -1);
                if (noteId != -1) {
                    showDeleteConfirmationDialog(noteId);
                } else {
                    Toast.makeText(SettingActivity.this, "Cannot identify note to delete", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SettingActivity.this, "No note selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Add this method to SettingActivity
    private void showDeleteConfirmationDialog(int noteId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    boolean isDeleted = databaseHelper.deleteNoteById(noteId);

                    if (isDeleted) {
                        Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to previous activity
                    } else {
                        Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}