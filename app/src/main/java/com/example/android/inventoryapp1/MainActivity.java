package com.example.android.inventoryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;
import static com.example.android.inventoryapp1.bookData.BookContract.BookEntry.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 0;
    BookCursorAdapter cursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BookEditor.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the book data.
        ListView bookListView = findViewById(R.id.list);

        //Find and set empty view on the listView, so that it shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        // Setup an adapter to create a list item for each row of book data in the cursor.
        // There is no book data yet{until the loader finishes} so pass in null for the cursor.
        cursorAdapter = new BookCursorAdapter(this,null);
        bookListView.setAdapter(cursorAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,BookEditor.class);
                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI,id);
                intent.setData(currentBookUri);
                startActivity(intent);
            }
        });
        // Kick off loader
        getSupportLoaderManager().initLoader(BOOK_LOADER,null,this);
    }

    /**
     * Helper method to insert hardcoded book data into the database.
     */
    private void insertBook() {

        ContentValues values = new ContentValues();
        values.put(BookEntry.BOOK_NAME, "Wings Of Fire");
        values.put(BookEntry.BOOK_PRICE, 20.2);
        values.put(BookEntry.BOOK_QUANTITY, 100);
        values.put(BookEntry.BOOK_AUTHOR_NAME, "A.P.J Abdul Kalam");
        values.put(BookEntry.BOOK_LANGUAGE, "English");

        Uri newUri = getContentResolver().insert(CONTENT_URI,values);
    }

    private void showDeleteAllConfirmationDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_books_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAllBook();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null)
                {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAllBook(){
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI,null,null);
        Log.v("MainActivity", rowsDeleted +" rows deleted from the books database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_insert_dummy_book:
                insertBook();
                return true;
            case R.id.action_delete_all_entries:
                showDeleteAllConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.BOOK_NAME,
                BookEntry.BOOK_AUTHOR_NAME,
                BookEntry.BOOK_PRICE};
        return new CursorLoader(this,
                CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
