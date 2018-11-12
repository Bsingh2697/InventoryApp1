package com.example.android.inventoryapp1.bookData;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

//Outer Class
public final class BookContract {
    //To prevent someone from accidentally instantiating the contract class,
    //give it an empty constructor
    private BookContract(){}

    /**
     * The "Content Authoring" is a name for the entire content provider, similar to the
     * relationship between a domain name and it's website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.books";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base contact URI for possible URI's)
     * For instance, content://com.example.android.books/books/ is a valid path for
     * looking at book data. content://com.example.android.books/xyz/ will fail,
     * as the ContentProvider hasn't been given any information, on what to do with "xyz".
     */
    public static final String PATH_BOOKS = "books";

    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single Book
     */
    public static final class BookEntry implements BaseColumns{

        /**
         *  This content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_BOOKS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                +"/"+CONTENT_AUTHORITY+"/"+PATH_BOOKS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                +"/"+CONTENT_AUTHORITY+"/"+PATH_BOOKS;

        /** Name of the database table for Books*/
        public final static String TABLE_NAME = "books";

        /**
         * Unique ID number for the book( only for use in the database table)
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the Book
         *
         * Type: TEXT
         */
        public final static String BOOK_NAME = "name";

        /**
         * Price of the Book
         *
         * Type: REAL
         */
        public final static String BOOK_PRICE = "price";

        /**
         * Quantity of the Book
         *
         * Type: INTEGER
         */
        public final static String BOOK_QUANTITY = "quantity";

        /**
         * Author of the Book
         *
         * Type: TEXT
         */
        public final static String BOOK_AUTHOR_NAME = "author";

        /**
         * Language of the Book
         *
         * Type: TEXT
         */
        public final static String BOOK_LANGUAGE = "language";
    }
}
