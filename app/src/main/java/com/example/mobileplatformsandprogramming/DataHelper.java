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
    /*              Database information            */
    public static final String DATABASE_NAME = "imdbUser.db";

    /*              Wishlist table                 */
    public static final String TABLE_WISHLIST = "wishlist_table";
    public static final String WISHLIST_COL_Id = "id";
    public static final String WISHLIST_COL_MovieName = "moviename";
    public static final String WISHLIST_COL_MovieId = "movieid";

    /*              Users table                 */
    public static final String TABLE_USER = "user_table";
    public static final String REGISTER_COL_Id = "id";
    public static final String REGISTER_COL_Username = "username";
    public static final String REGISTER_COL_Password = "password";

    /*              Create table - USER         */
    public static final String CREATE_TABLE_USER = "create table " + TABLE_USER +
            " (" + REGISTER_COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            REGISTER_COL_Username + " TEXT, " +
            REGISTER_COL_Password + " TEXT)";

    /*              Create table - WISHLIST         */
    public static final String CREATE_TABLE_WISHLIST = "create table " + TABLE_WISHLIST +
            " (" + WISHLIST_COL_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WISHLIST_COL_MovieName + " TEXT, " +
            WISHLIST_COL_MovieId + " TEXT)";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_WISHLIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST);
        onCreate(db);
    }

    /*                  USER                    */
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

    /*                  WISHLIST                    */
    public boolean addMovieToWishlist(String moviename, String movieid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WISHLIST_COL_MovieName, moviename);
        contentValues.put(WISHLIST_COL_MovieId, movieid);

        long result = db.insert(TABLE_WISHLIST, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllWishlistData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_WISHLIST, null);
        return res;
    }
}
