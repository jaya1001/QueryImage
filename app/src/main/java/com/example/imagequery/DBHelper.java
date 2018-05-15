package com.example.imagequery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String IMAGES_TABLE_NAME = "images";
    public static final String COLUMN_URL = "photo_url";
    public static final String COLUMN_TAG = "photo_tag";

    private static final String query =  "CREATE TABLE " + IMAGES_TABLE_NAME + "(" + COLUMN_URL + " TEXT," + COLUMN_TAG + " TEXT)";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                query
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +  IMAGES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertImage(String url, String tag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_URL, url);
        contentValues.put(COLUMN_TAG, tag);
        db.insert(IMAGES_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = null;
        String[] projection = {
                COLUMN_URL,
                COLUMN_TAG
        };

        String selection = COLUMN_TAG + " = ?";
        String[] selectionArgs = { tag };
        res = db.query(
                IMAGES_TABLE_NAME,projection,selection,selectionArgs,null,null,null
        );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, IMAGES_TABLE_NAME);
        return numRows;
    }
}