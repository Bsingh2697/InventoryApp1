package com.example.android.inventoryapp1.bookData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.Selection;
import android.util.Log;

import com.example.android.inventoryapp1.BookEditor;
import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;

public class BookProvider extends ContentProvider {

    private BookDbHelper dbHelper;

    private static final int BOOKS = 100;
    private static final int BOOKS_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //Static initializer. This is run the first time anything is called from this class.
    static{
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS, BOOKS);
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY,BookContract.PATH_BOOKS+"/#", BOOKS_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       SQLiteDatabase database = dbHelper.getReadableDatabase();
       Cursor cursor;
       int match = uriMatcher.match(uri);
       switch(match) {
           case BOOKS:
               cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs,
                       null, null, sortOrder);
               break;
           case BOOKS_ID:
               selection = BookEntry._ID + "=?";
               selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
               cursor = database.query(BookEntry.TABLE_NAME,projection,selection,selectionArgs,
                       null,null,sortOrder);

               break;
           default:
               throw new IllegalArgumentException("Cannot query unknown URI " + uri);
       }
       cursor.setNotificationUri(getContext().getContentResolver(),uri);
       return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch(match){
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri + " with match "+match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){

       final int match = uriMatcher.match(uri);
       switch(match){
           case BOOKS:
               return insertBook(uri, values);
           default:
               throw new IllegalArgumentException("Insertion is not supported for "+uri);
       }
    }

    public Uri insertBook(Uri uri, ContentValues values) {
        // Check that the book name is not null
        String name = values.getAsString(BookEntry.BOOK_NAME);
        if(name == null){
            throw new IllegalArgumentException("Book requires a name");
        }
        // Check that the price is not null
        Integer price = values.getAsInteger(BookEntry.BOOK_PRICE);
        if(price == null){
            throw new IllegalArgumentException("Book requires a valid Price");
        }
        // Check that the quantity is not null
        String language = values.getAsString(BookEntry.BOOK_QUANTITY);
        if(language == null){
            throw new IllegalArgumentException("Please enter the Book language");
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(BookEntry.TABLE_NAME,null,values);
        if(id == -1)
        {
            Log.e("BookProvider","Failed to insert row for "+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database =dbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = uriMatcher.match(uri);
        switch (match)
        {
            case BOOKS:
                rowsDeleted = database.delete(BookEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case BOOKS_ID:
                selection = BookEntry._ID +"=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for "+uri);
        }
        if(rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,String[] selectionArgs ) {
        final int match = uriMatcher.match(uri);
        switch(match){
            case BOOKS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case BOOKS_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,contentValues,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for "+uri);
        }
    }
    public int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.containsKey(BookEntry.BOOK_NAME)) {
            String name = contentValues.getAsString(BookEntry.BOOK_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }
        if (contentValues.containsKey(BookEntry.BOOK_PRICE)) {
            Integer price = contentValues.getAsInteger(BookEntry.BOOK_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Please! enter the price");
            }
        }
        if (contentValues.containsKey(BookEntry.BOOK_LANGUAGE)) {
            String language = contentValues.getAsString(BookEntry.BOOK_LANGUAGE);
            if (language == null) {
                throw new IllegalArgumentException("Please! update the book language");
            }
        }
        //if there are no values to update, then don't try to update the DB
        if (contentValues.size() == 0) {
            return 0;
        }
        // Otherwise get the writable database to update the data
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsUpdated = database.update(BookEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
