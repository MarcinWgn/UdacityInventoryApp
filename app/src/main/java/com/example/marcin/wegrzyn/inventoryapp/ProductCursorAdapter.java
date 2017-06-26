package com.example.marcin.wegrzyn.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

/**
 * Created by Marcin on 25.06.2017 :)
 */

 class ProductCursorAdapter extends CursorAdapter {


    ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView id = (TextView) view.findViewById(R.id.item_id);
        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView price = (TextView) view.findViewById(R.id.item_price);
        TextView quantity = (TextView) view.findViewById(R.id.item_quantity);

        Button saleButton = (Button) view.findViewById(R.id.sale_button);

        int idColumn = cursor.getColumnIndex(ProductEntry._ID);
        int nameColumn = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int priceColumn = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int quntityColumn = cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY);

        String productId = cursor.getString(idColumn);
        String productName = cursor.getString(nameColumn);
        String productPrice = cursor.getString(priceColumn);
        final String productQuantity = cursor.getString(quntityColumn);

        final int intID = cursor.getInt(idColumn);
        final int intQuantity = cursor.getInt(quntityColumn);

        id.setText(productId);
        name.setText(productName);
        price.setText(productPrice);
        quantity.setText(productQuantity);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(intQuantity>0){
                    Uri currentPetUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,intID);
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_QUANTITY,intQuantity-1);
                    context.getContentResolver().update(currentPetUri,values,null,null);
                }
            }
        });

    }
}
