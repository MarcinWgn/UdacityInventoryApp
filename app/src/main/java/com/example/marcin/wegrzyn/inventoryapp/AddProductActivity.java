package com.example.marcin.wegrzyn.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

public class AddProductActivity extends AppCompatActivity {

    public static final String SAVE_URI = "SaveUri";
    static final int IMAGE_REQUEST_CODE = 1;
    private ImageView imageView;
    private TextView emptyView;
    private Uri uri = null;
    private Bitmap bitmap = null;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText describeEditText;
    private EditText emailEditText;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (uri != null && !uri.equals(Uri.EMPTY)) {
            outState.putString(SAVE_URI, uri.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_layout);

        setTitle(getString(R.string.add_product));

        imageView = (ImageView) findViewById(R.id.image);
        emptyView = (TextView) findViewById(R.id.textEmpty);

        nameEditText = (EditText) findViewById(R.id.editName);
        priceEditText = (EditText) findViewById(R.id.editPrice);
        quantityEditText = (EditText) findViewById(R.id.editQuantity);
        describeEditText = (EditText) findViewById(R.id.editDescribe);
        emailEditText = (EditText) findViewById(R.id.editSupplier);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            uri = Uri.parse(savedInstanceState.getString(SAVE_URI, null));
            showImage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            saveProduct();
            return true;

        } else if (id == R.id.addImage) {
            addImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            showImage();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_PICK);
        getImageIntent.setType("image/*");
        startActivityForResult(getImageIntent, IMAGE_REQUEST_CODE);
    }

    private void showImage() {

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        emptyView.setVisibility(View.INVISIBLE);
    }

    private void saveProduct() {

        String name = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String describe = describeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (name.isEmpty()
                || priceString.isEmpty()
                || quantityString.isEmpty()
                || describe.isEmpty()
                || email.isEmpty()
                || bitmap==null) {
            Toast.makeText(this, R.string.complete_field, Toast.LENGTH_SHORT).show();
        } else {

            double price = Double.parseDouble(priceString);
            int quantity = Integer.parseInt(quantityString);

            if (price == 0 || quantity == 0) {
                Toast.makeText(this, R.string.not_be_zero, Toast.LENGTH_SHORT).show();
            } else {
                productInsert(name, quantity, price, describe, bitmap, email);
            }
        }

    }

    private byte[] convertBitmap(Bitmap bitmap) {

        byte[] output;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        output = outputStream.toByteArray();
        return output;
    }

    private boolean productInsert(String name, int quantity, double price, String desc, Bitmap bitmap, String supplier) {

        byte[] img = null;

        if (bitmap != null) {
            img = convertBitmap(bitmap);
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(ProductEntry.COLUMN_NAME, name);
        contentValues.put(ProductEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(ProductEntry.COLUMN_PRICE, price);
        contentValues.put(ProductEntry.COLUMN_DESC, desc);
        contentValues.put(ProductEntry.COLUMN_IMAGE, img);
        contentValues.put(ProductEntry.COLUMN_SUPPLIER, supplier);


        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
        if (uri == null) {
            Toast.makeText(this, R.string.insert_failed, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(this, R.string.insert_successful, Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }

    }

}
