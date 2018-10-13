package com.example.android.inventoryapp1.bookData;

import android.provider.BaseColumns;

//Outer Class
public final class BookContract {
    //To prevent someone from accidentally instantiating the contract class,
    //give it an empty constructor
    private BookContract(){}
    /**
     * Inner class that defines constant values for the books database table.
     * Each entry in the table represents a single Book
     */
    public static final class BookEntry implements BaseColumns{

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
        public final static String BOOK_PRICE = "price($)";

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
