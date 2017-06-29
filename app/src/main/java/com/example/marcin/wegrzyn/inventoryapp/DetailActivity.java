package com.example.marcin.wegrzyn.inventoryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private Uri curUri;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        Intent intent = getIntent();
        curUri = intent.getData();


        textView = (TextView) findViewById(R.id.TV1);
        textView.setText(curUri.toString());
        setTitle("product");

    }
}
