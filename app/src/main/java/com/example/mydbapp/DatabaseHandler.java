package com.example.mydbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements AutoCloseable {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SongsManager";

    public static class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_ALBUM_NAME = "albumName";
    }

    private static final String SQL_CREATE_SONGS = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)",
            SongEntry.TABLE_NAME,
            SongEntry._ID,
            SongEntry.COLUMN_NAME_NAME,
            SongEntry.COLUMN_NAME_AUTHOR,
            SongEntry.COLUMN_NAME_DURATION,
            SongEntry.COLUMN_NAME_ALBUM_NAME);

    private static final String SQL_DELETE_SONGS = "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SONGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_SONGS);
        onCreate(db);
    }

    public void addSong(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SongEntry.COLUMN_NAME_NAME, songs.getName());
        values.put(SongEntry.COLUMN_NAME_AUTHOR, songs.getAuthor());
        values.put(SongEntry.COLUMN_NAME_DURATION, songs.getDuration());
        values.put(SongEntry.COLUMN_NAME_ALBUM_NAME, songs.getAlbumName());
        db.insert(SongEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteSong(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SongEntry.TABLE_NAME, SongEntry._ID + " = ?", new String[]{String.valueOf(songs.getID())});
        db.close();
    }

    public void updateSong(Songs songs) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SongEntry.COLUMN_NAME_NAME, songs.getName());
        values.put(SongEntry.COLUMN_NAME_AUTHOR, songs.getAuthor());
        values.put(SongEntry.COLUMN_NAME_DURATION, songs.getDuration());
        values.put(SongEntry.COLUMN_NAME_ALBUM_NAME, songs.getAlbumName());
        db.update(SongEntry.TABLE_NAME, values, SongEntry._ID + " = ?", new String[]{String.valueOf(songs.getID())});
    }

    public List<Songs> getAllSongs() {
        List<Songs> songsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SongEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Songs songs = new Songs(
                        cursor.getInt(cursor.getColumnIndexOrThrow(SongEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_DURATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_ALBUM_NAME))
                );
                songsList.add(songs);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songsList;
    }

    public List<Songs> getSongsByNameFilter(String authorFilter) {
        List<Songs> songsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SongEntry.TABLE_NAME + " WHERE " + SongEntry.COLUMN_NAME_NAME + " LIKE ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + authorFilter + "%"});

        if (cursor.moveToFirst()) {
            do {
                Songs songs = new Songs(
                        cursor.getInt(cursor.getColumnIndexOrThrow(SongEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_DURATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(SongEntry.COLUMN_NAME_ALBUM_NAME))
                );
                songsList.add(songs);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsList;
    }

    public List<String> getSongsSortedByDuration() {
        List<String> songsNames = new ArrayList<>();
        String selectQuery = "SELECT " + SongEntry.COLUMN_NAME_NAME + " FROM " + SongEntry.TABLE_NAME + " ORDER BY " + SongEntry.COLUMN_NAME_DURATION + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                songsNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsNames;
    }

    public List<String> getSongsSortedByName() {
        List<String> songsNames = new ArrayList<>();
        String selectQuery = "SELECT " + SongEntry.COLUMN_NAME_NAME + " FROM " + SongEntry.TABLE_NAME + " ORDER BY " + SongEntry.COLUMN_NAME_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                songsNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsNames;
    }

    public List<String> getSongsDependsOnAuthor(String authorName) {
        List<String> songsNames = new ArrayList<>();
        String selectQuery = "SELECT " + SongEntry.COLUMN_NAME_NAME + " FROM " + SongEntry.TABLE_NAME + " WHERE " + SongEntry.COLUMN_NAME_AUTHOR + " = '" + authorName + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                songsNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsNames;
    }

    public List<String> getSongsOfAlbumWithAuthor(String albumName, String authorName) {
        List<String> songsNames = new ArrayList<>();
        String selectQuery = "SELECT " + SongEntry.COLUMN_NAME_NAME + " FROM " + SongEntry.TABLE_NAME + " WHERE " + SongEntry.COLUMN_NAME_AUTHOR + " = '" + authorName + "'" + " AND " + SongEntry.COLUMN_NAME_ALBUM_NAME + " = '" + albumName + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                songsNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsNames;
    }

    /*public List<String> getAuthorsSortedByYear() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + SongEntry.COLUMN_NAME_AUTHOR + " FROM " + SongEntry.TABLE_NAME + " ORDER BY " + BookEntry.COLUMN_NAME_YEAR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsDependsOnDisk(int isDisk) {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_DISK + " = " + isDisk;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsWithBooksAfter2007AndMinAmount3000() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_YEAR + " >= 2007 AND " + BookEntry.COLUMN_NAME_AMOUNT + " >= 3000";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<String> getAuthorsInAlphabeticalOrder() {
        List<String> authors = new ArrayList<>();
        String selectQuery = "SELECT " + BookEntry.COLUMN_NAME_AUTHOR + " FROM " + BookEntry.TABLE_NAME + " ORDER BY " + BookEntry.COLUMN_NAME_AUTHOR;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                authors.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return authors;
    }

    public List<Songs> getBooksByAuthorFilter(String authorFilter) {
        List<Songs> songsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BookEntry.TABLE_NAME + " WHERE " + BookEntry.COLUMN_NAME_AUTHOR + " LIKE ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + authorFilter + "%"});

        if (cursor.moveToFirst()) {
            do {
                Songs songs = new Songs(
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AUTHOR)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_DISK)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NAME_YEAR))
                );
                songsList.add(songs);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return songsList;
    }*/
}
