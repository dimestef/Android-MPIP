package com.example.mobileplatformsandprogramming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dimitar on 7/29/2017.
 */

public class DataHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "imdbUser.db";
    public static final String TABLE_USER = "user_table";
    public static final String REGISTER_COL_Id = "id";
    public static final String REGISTER_COL_Username = "username";
    public static final String REGISTER_COL_Password = "password";

    public static final String DATABASE_CREATE = "create table " + TABLE_USER +
            " (" + REGISTER_COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            REGISTER_COL_Username + " TEXT, " +
            REGISTER_COL_Password + " TEXT)";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REGISTER_COL_Username, username);
        contentValues.put(REGISTER_COL_Password, password);

        long result = db.insert(TABLE_USER, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USER, null);
        return res;
    }
}
