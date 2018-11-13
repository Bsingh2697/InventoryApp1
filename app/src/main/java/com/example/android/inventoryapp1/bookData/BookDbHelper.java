package com.example.android.inventoryapp1.bookData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    /**Name of the database file*/
    private static final String DATABASE_NAME = "bookStore.db";

    /**
     * Database version. If you change the database schema, you must increment
     * the database version
     */
    private static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION );
    }

    /**
     * This is called when the database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create a String that contains the SQL statement to create the books table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE "+ BookEntry.TABLE_NAME+" ("
                +BookEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +BookEntry.BOOK_NAME+" TEXT NOT NULL, "
                +BookEntry.BOOK_PRICE+" REAL NOT NULL DEFAULT 0, "
                +BookEntry.BOOK_QUANTITY+" INTEGER NOT NULL DEFAULT 1, "
                +BookEntry.BOOK_SUPPLIER_NAME +" TEXT NOT NULL, "
                +BookEntry.BOOK_SUPPLIER_PHONE +" TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
