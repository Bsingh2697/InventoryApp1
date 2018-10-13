package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.inventoryapp1.bookData.BookDbHelper;
import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;



public class MainActivity extends AppCompatActivity {

    private BookDbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        dbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Helper method to insert hardcoded book data into the database.
     */
    private void insertPet()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_NAME,"Wings Of Fire");
        values.put(BookEntry.BOOK_PRICE,20.2);
        values.put(BookEntry.BOOK_QUANTITY,2);
        values.put(BookEntry.BOOK_AUTHOR_NAME,"A.P.J Abdul Kalam");
        values.put(BookEntry.BOOK_LANGUAGE,"English");
    }

    private void queryData()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Create Projection for all the columns needed
        String[] projection = {
                BookEntry.BOOK_NAME,
                BookEntry.BOOK_PRICE,
                BookEntry.BOOK_QUANTITY,
                BookEntry.BOOK_AUTHOR_NAME,
                BookEntry.BOOK_LANGUAGE
        };

        Cursor cursor = db.query(BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        cursor.close();
    }
}
