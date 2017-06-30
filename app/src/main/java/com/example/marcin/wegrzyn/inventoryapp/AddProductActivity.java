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

import java.io.FileNotFoundException;

import static com.example.marcin.wegrzyn.inventoryapp.Data.ProductConrtact.ProductEntry;

public class AddProductActivity extends AppCompatActivity {


    public static final String TAG = AddProductActivity.class.getName();

    static final int IMAGE_REQUEST_CODE = 1;
    public static final String SAVE_URI = "SaveUri";
    private ImageView imageView;
    private TextView emptyView;
    private Uri uri;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(uri!= null) outState.putString(SAVE_URI,uri.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_layout);

        setTitle("add Product");

        imageView = (ImageView) findViewById(R.id.image);
        emptyView = (TextView) findViewById(R.id.textEmpty);

        if(savedInstanceState!=null){
            uri = Uri.parse(savedInstanceState.getString(SAVE_URI,null));
            showImage();
        }
        EditText editText = (EditText) findViewById(R.id.editName);
        editText.getText();

        if(uri!=null)showImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add){
            Toast.makeText(this,"add",Toast.LENGTH_SHORT).show();
            return true;

        }else if(id == R.id.addImage){
            Intent getImageIntent = new Intent(Intent.ACTION_PICK);
            getImageIntent.setType("image/*");
            startActivityForResult(getImageIntent, IMAGE_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null){
            uri = data.getData();
            showImage();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showImage(){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        emptyView.setVisibility(View.INVISIBLE);
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
