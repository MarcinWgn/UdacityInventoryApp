package com.example.marcin.wegrzyn.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

/**
 * Created by Marcin on 25.06.2017 :)
 */

public class ProductCursorAdapter extends CursorAdapter {


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView id = (TextView) view.findViewById(R.id.item_id);
        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView price = (TextView) view.findViewById(R.id.item_price);
        TextView quantity = (TextView) view.findViewById(R.id.item_quantity);

        int idColumn = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumn = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int priceColumn = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int quntityColumn = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);

        String productId = cursor.getString(idColumn);
        String productName = cursor.getString(nameColumn);
        String productPrice = cursor.getString(priceColumn);
        String productQuantity = cursor.getString(quntityColumn);

        id.setText(productId);
        name.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);

    }
}
