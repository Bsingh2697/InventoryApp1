package com.example.android.inventoryapp1;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
        int authorColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_AUTHOR_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_QUANTITY);

        String bookName = cursor.getString(bookNameColumnIndex);
        String bookAuthor = cursor.getString(authorColumnIndex);
        String bookPrice = cursor.getString(priceColumnIndex);
        String bookQuantity = cursor.getString(quantityColumnIndex);

        if (TextUtils.isEmpty(bookAuthor)) {
            bookAuthor = context.getString(R.string.unknown_author);
        }
        nameTextView.setText(bookName);
        summaryTextView.setText(bookAuthor);
        priceTextView.setText(bookPrice);
        quantityTextView.setText(bookQuantity);

        final Button saleBtn = view.findViewById(R.id.buy_button);
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityTextView.setText("1");
            }
        });
    }
}
