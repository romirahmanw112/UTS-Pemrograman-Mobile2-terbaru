package com.example.urnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT)";
        db.execSQL(createTable);
    }

    public int getNoteId(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_ID},
                COLUMN_TITLE + "=?", new String[]{title},
                null, null, null);

        int id = -1;
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            cursor.close();
        }
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public boolean deleteNoteById(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTES, COLUMN_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
        return result > 0;
    }

    // Add this missing method needed by MainActivity
    public boolean deleteNote(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NOTES, COLUMN_TITLE + "=?", new String[]{title});
        db.close();
        return result > 0;
    }

    public boolean insertNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        long result = db.insert(TABLE_NOTES, null, values);
        return result != -1;
    }

    public List<String> getAllNotes() {
        List<String> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                notes.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public boolean updateNote(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        int result = db.update(TABLE_NOTES, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public String getNoteContent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, new String[]{COLUMN_CONTENT}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
            cursor.close();
            return content;
        }
        return null;
    }
}