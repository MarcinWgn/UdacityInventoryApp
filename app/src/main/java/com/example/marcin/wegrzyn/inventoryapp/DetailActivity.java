package com.example.marcin.wegrzyn.inventoryapp;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    public static final String TAG = DetailActivity.class.getSimpleName();

    private static final int PODUCT_LOADER = 5;

    private Uri curUri;

    private TextView idTextView;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView quantityTextView;
    private TextView supplierTextView;
    private TextView describeTextView;

    private TextView emptyView;
    private ImageView imageView;

    private Button plusButton;
    private Button minusButton;
    private Button orderButton;

    private int quantity;
    private String supplier;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        setTitle("product");

        idTextView = (TextView) findViewById(R.id.TVid);
        nameTextView = (TextView) findViewById(R.id.TVname);
        priceTextView = (TextView) findViewById(R.id.TVprice);
        quantityTextView = (TextView) findViewById(R.id.TVquantity);
        supplierTextView = (TextView) findViewById(R.id.TVorder);
        describeTextView = (TextView) findViewById(R.id.TVdescribe);
        emptyView = (TextView) findViewById(R.id.TVempty);
        imageView = (ImageView) findViewById(R.id.image);

        plusButton = (Button) findViewById(R.id.plusBTN);
        minusButton = (Button) findViewById(R.id.minusBTN);
        orderButton = (Button) findViewById(R.id.orderBTN);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(quantity>0){
                   quantity--;
                   quantityTextView.setText(String.valueOf(quantity));
               }

            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + supplier));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_product));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.order_question)+" "+name);
               try {
                   startActivity(intent);
               }catch (ActivityNotFoundException e){
                   Toast.makeText(getBaseContext(), R.string.went_wrong,Toast.LENGTH_SHORT).show();
               }
            }
        });

        Intent intent = getIntent();
        curUri = intent.getData();


        getLoaderManager().initLoader(PODUCT_LOADER, null, this);

        Log.d(TAG,"uri ---> "+curUri.toString());

    }

    private void updateQuantity(int quantity){
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_QUANTITY,quantity);
        getContentResolver().update(curUri,values,null,null);
    }

    @Override
    protected void onPause() {
        updateQuantity(quantity);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            delete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_QUANTITY,
                ProductEntry.COLUMN_PRICE,
                ProductEntry.COLUMN_DESC,
                ProductEntry.COLUMN_IMAGE,
                ProductEntry.COLUMN_SUPPLIER
        };

        return new CursorLoader(this, curUri, projection, null, null, null);
    }
    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void deleteProduct(){
        if(curUri!= null){
            int rowDel = getContentResolver().delete(curUri,null,null);
            if(rowDel!=0) finish();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data == null || data.getCount() <1) return;

        if(data.moveToFirst()){

            int idColumn = data.getColumnIndex(ProductEntry._ID);
            int nameColumn = data.getColumnIndex(ProductEntry.COLUMN_NAME);
            int quntityColumn = data.getColumnIndex(ProductEntry.COLUMN_QUANTITY);
            int priceColumn = data.getColumnIndex(ProductEntry.COLUMN_PRICE);
            int descColumn = data.getColumnIndex(ProductEntry.COLUMN_DESC);
            int imageColumn = data.getColumnIndex(ProductEntry.COLUMN_IMAGE);
            int supplierColumn = data.getColumnIndex(ProductEntry.COLUMN_SUPPLIER);

            int id = data.getInt(idColumn);
            name = data.getString(nameColumn);
            quantity = data.getInt(quntityColumn);
            float price = data.getFloat(priceColumn);
            String desc = data.getString(descColumn);
            byte[] img= data.getBlob(imageColumn);
            supplier = data.getString(supplierColumn);

            idTextView.setText(String.valueOf(id));
            nameTextView.setText(name);
            priceTextView.setText(String.valueOf(price));
            quantityTextView.setText(String.valueOf(quantity));
            supplierTextView.setText(supplier);
            describeTextView.setText(desc);

            if(img!= null){
                ByteArrayInputStream inputStream = new ByteArrayInputStream(img);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                emptyView.setVisibility(View.INVISIBLE);
            }else emptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}
