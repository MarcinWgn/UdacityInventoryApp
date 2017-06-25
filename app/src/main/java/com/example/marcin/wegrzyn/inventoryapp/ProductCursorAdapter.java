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

        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView data = (TextView) view.findViewById(R.id.item_data);

        int nameColumn = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int nameData = cursor.getColumnIndex(ProductEntry.COLUMN_DESC);

        String productName = cursor.getString(nameColumn);
        String productData = cursor.getString(nameData);

        name.setText(productName);
        data.setText(productData);

    }
}
