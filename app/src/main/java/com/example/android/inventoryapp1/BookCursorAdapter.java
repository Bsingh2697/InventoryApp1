package com.example.android.inventoryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;

public class BookCursorAdapter extends android.support.v4.widget.CursorAdapter {

    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView = view.findViewById(R.id.summary);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.buy_quantity);

        int bookNameColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_NAME);
        int supplierColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_SUPPLIER_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_QUANTITY);

        String bookName = cursor.getString(bookNameColumnIndex);
        String bookSupplier = cursor.getString(supplierColumnIndex);
        int bookPrice = cursor.getInt(priceColumnIndex);
        int bookQuantity = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(bookSupplier)) {
            bookSupplier = context.getString(R.string.unknown_supplier);
        }
        nameTextView.setText(bookName);
        summaryTextView.setText(bookSupplier);
        priceTextView.setText(Integer.toString(bookPrice));
        quantityTextView.setText(Integer.toString(bookQuantity));

        final int cursorPosition = cursor.getPosition();
        final Button saleBtn = view.findViewById(R.id.buy_button);
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();
                cursor.moveToPosition(cursorPosition);
                String quantity = quantityTextView.getText().toString();
                int bookId = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));
                if(Integer.valueOf(quantity)>0) {
                    int newQuantity = Integer.valueOf(quantity) - 1;
                    values.put(BookEntry.BOOK_QUANTITY, newQuantity);
                    Uri bookSelectedUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, bookId);
                    int rowsAffected = context.getContentResolver().update(bookSelectedUri, values, null, null);
                    if (rowsAffected > 0) {
                        quantityTextView.setText(Integer.toString(newQuantity));
                    }else{
                        Toast.makeText(context,R.string.error_selling_book,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context,R.string.book_not_in_stock,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
