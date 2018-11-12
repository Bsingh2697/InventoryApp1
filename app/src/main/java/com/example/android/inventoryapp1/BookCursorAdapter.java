package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;

public class BookCursorAdapter extends android.support.v4.widget.CursorAdapter {

    private Context context;
    private ProductItemClickListener mListener;

    public BookCursorAdapter(Context context, Cursor c, ProductItemClickListener listener) {
        super(context, c, 0);
        this.context = context;
        mListener = listener;
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
        int bookPrice = cursor.getInt(priceColumnIndex);
        int bookQuantity = cursor.getInt(quantityColumnIndex);

        if (TextUtils.isEmpty(bookAuthor)) {
            bookAuthor = context.getString(R.string.unknown_author);
        }
        nameTextView.setText(bookName);
        summaryTextView.setText(bookAuthor);
        priceTextView.setText(Integer.toString(bookPrice));
        quantityTextView.setText(Integer.toString(bookQuantity));

        final Button saleBtn = view.findViewById(R.id.buy_button);
        saleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = quantityTextView.getText().toString();
                if(Integer.valueOf(quantity)>0)
                {
                    int newQuantity = Integer.valueOf(quantity)-1;
                    //Gets the position of button
                    View view1 = (View)view.getParent();
                    View view2 = (View)view1.getParent();
                    ListView listView = (ListView) view2.getParent();
                    int rows = mListener.onBookSold(listView.getPositionForView(view), newQuantity);
                    if(rows>0){
                        Toast.makeText(context,"One Book Sold", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(context,"Error Selling Book",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Book out of Stock",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public interface ProductItemClickListener{
        int onBookSold(int position, int newQuantity);
    }
}
