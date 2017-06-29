package com.example.marcin.wegrzyn.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product_layout);

        setTitle("add Product");

        EditText editText = (EditText) findViewById(R.id.editName);
        editText.getText();
    }
}
