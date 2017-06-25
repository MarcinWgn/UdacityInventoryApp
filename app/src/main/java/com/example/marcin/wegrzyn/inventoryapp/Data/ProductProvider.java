package com.example.marcin.wegrzyn.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

/**
 * Created by Marcin on 24.06.2017 :)
 */

public class ProductProvider extends ContentProvider {


    public static final int PRODUCTS = 10;
    public static final int PRODUCT_ID = 11;

    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        URI_MATCHER.addURI(ProductConrtact.CONTENT_AUTHORITY,ProductConrtact.PATH_PRODUCTS,PRODUCTS);

        URI_MATCHER.addURI(ProductConrtact.CONTENT_AUTHORITY,ProductConrtact.PATH_PRODUCTS + "/#",PRODUCT_ID);

    }

    private ProductDbHelper productDbHelper;



    @Override
    public boolean onCreate() {
        productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database = productDbHelper.getReadableDatabase();

        Cursor cursor = null;

        int match = URI_MATCHER.match(uri);

        switch (match){
            case PRODUCTS:
                cursor = database.query(ProductEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ProductEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("unknown URI"+uri);
        }

        return cursor;
    }
    // TODO: 25.06.2017 skończyć resztę zapytań

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
