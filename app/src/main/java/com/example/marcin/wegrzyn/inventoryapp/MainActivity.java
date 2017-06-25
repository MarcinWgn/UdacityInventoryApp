package com.example.marcin.wegrzyn.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;
import com.example.marcin.wegrzyn.inventoryapp.Data.ProductDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int PRODUCT_LOADER = 0;
    ProductCursorAdapter productCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Test Insert",Toast.LENGTH_SHORT).show();
                testInsert("Cukier",10,5,"bialy","obraz","Biedronka");
            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);


        productCursorAdapter = new ProductCursorAdapter(this,null);
        listView.setAdapter(productCursorAdapter);

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_QUANTITY
               };

        return new CursorLoader(this,ProductEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        productCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productCursorAdapter.swapCursor(null);
    }

    private void testInsert(String name,int quantity, int price, String desc, String img, String supplier ){

        ProductDbHelper pdbHelper = new ProductDbHelper(this);
        SQLiteDatabase database = pdbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductEntry.COLUMN_NAME,name);
        contentValues.put(ProductEntry.COLUMN_QUANTITY,quantity);
        contentValues.put(ProductEntry.COLUMN_PRICE,price);
        contentValues.put(ProductEntry.COLUMN_DESC,desc);
        contentValues.put(ProductEntry.COLUMN_IMAGE,img);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER,supplier);

        long RowId = database.insert(ProductEntry.TABLE_NAME, null, contentValues);

    }
}
