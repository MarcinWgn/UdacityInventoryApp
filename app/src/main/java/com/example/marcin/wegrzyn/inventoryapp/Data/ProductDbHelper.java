package com.example.marcin.wegrzyn.inventoryapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

/**
 * Created by Marcin on 24.06.2017 :)
 */

class ProductDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "products.db";

    private static final int DATABASE_VERSION = 1;


    ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SQL_PRODUCTS_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME
                + "("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PRICE + " INTEGER NOT NULL,"
                + ProductEntry.COLUMN_DESC + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_IMAGE + " TEXT NOT NULL,"
                + ProductEntry.COLUMN_SUPPLIER + " TEXT NOT NULL"
                + "); ";

        db.execSQL(CREATE_SQL_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
