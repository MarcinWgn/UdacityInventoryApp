package com.example.marcin.wegrzyn.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {



    public static final String TAG = MainActivity.class.getSimpleName();

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

                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);

            }
        });

        ListView listView = (ListView) findViewById(R.id.list);
        TextView emptyView = (TextView) findViewById(R.id.empty_view);

        listView.setEmptyView(emptyView);


        productCursorAdapter = new ProductCursorAdapter(this, null);
        listView.setAdapter(productCursorAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri curUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                intent.setData(curUri);
                startActivity(intent);
            }
        });

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

        return new CursorLoader(this, ProductEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished");
        productCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
        productCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.dummy_data){
            testInsert("zegarek",10, 5, "Rolex", null, "gold@ww.pl");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void testInsert(String name, int quantity, int price, String desc, String img, String supplier) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductEntry.COLUMN_NAME, name);
        contentValues.put(ProductEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(ProductEntry.COLUMN_PRICE, price);
        contentValues.put(ProductEntry.COLUMN_DESC, desc);
        contentValues.put(ProductEntry.COLUMN_IMAGE, img);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER, supplier);


        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        Toast.makeText(getBaseContext(), "Test Insert: " + uri, Toast.LENGTH_SHORT).show();

    }
}
