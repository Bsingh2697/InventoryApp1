package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp1.bookData.BookContract.BookEntry;

public class BookDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_BOOK_LOADER = 0;
    private Uri currentBookUri;
    private TextView nameTextView;
    private TextView supplierTextView;
    private TextView priceTextView;
    private TextView quantityTextView;
    private TextView phoneTextView;
    private Button incrementButton;
    private Button decrementButton;
    private Button phoneButton;
    private int quantityChange = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);

        Intent intent = getIntent();
        currentBookUri = intent.getData();

        nameTextView = findViewById(R.id.detail_book_name);
        supplierTextView = findViewById(R.id.detail_supplier_name);
        priceTextView = findViewById(R.id.detail_book_price);
        quantityTextView = findViewById(R.id.detail_book_quantity);
        phoneTextView = findViewById(R.id.detail_supplier_number);
        incrementButton = findViewById(R.id.increment_button);
        decrementButton = findViewById(R.id.decrement_button);
        phoneButton = findViewById(R.id.call);

        getSupportLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_edit:
                Intent intent = new Intent(BookDetails.this, BookEditor.class);
                intent.setData(currentBookUri);
                startActivity(intent);
                finish();
                return true;
            case R.id.detail_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(BookDetails.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_book);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteBook() {
        if (currentBookUri != null) {
            int rowsDeleted = getContentResolver().delete(currentBookUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.error_deleting_book, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.book_deleted, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.BOOK_NAME,
                BookEntry.BOOK_PRICE,
                BookEntry.BOOK_QUANTITY,
                BookEntry.BOOK_SUPPLIER_NAME,
                BookEntry.BOOK_SUPPLIER_PHONE
        };
        return new CursorLoader(this,
                currentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        int nameColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_NAME);
        int supplierColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_SUPPLIER_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_QUANTITY);
        int phoneColumnIndex = cursor.getColumnIndex(BookEntry.BOOK_SUPPLIER_PHONE);

        if (cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            final String phone = cursor.getString(phoneColumnIndex);

            nameTextView.setText(name);
            supplierTextView.setText(supplier);
            priceTextView.setText(Integer.toString(price));
            quantityTextView.setText(Integer.toString(quantity));
            phoneTextView.setText(phone);

            incrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantity = quantityTextView.getText().toString();
                    int newQuantity = Integer.valueOf(quantity) + quantityChange;
                    if (newQuantity >= 0) {
                        ContentValues values = new ContentValues();
                        values.put(BookEntry.BOOK_QUANTITY, newQuantity);
                        getContentResolver().update(currentBookUri, values, null, null);
                        quantityTextView.setText(String.valueOf(newQuantity));
                    }
                }
            });

            decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String quantity = quantityTextView.getText().toString();
                    int newQuantity = Integer.valueOf(quantity) - quantityChange;
                    if (newQuantity >= 0) {
                        ContentValues values = new ContentValues();
                        values.put(BookEntry.BOOK_QUANTITY, newQuantity);
                        getContentResolver().update(currentBookUri, values, null, null);
                        quantityTextView.setText(String.valueOf(newQuantity));
                    } else {
                        Toast.makeText(BookDetails.this, getString(R.string.negative_quantity), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = "tel:"+phone;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        nameTextView.setText("");
        priceTextView.setText("");
        quantityTextView.setText("");
        supplierTextView.setText("");
        phoneTextView.setText("");
    }
}
