package com.example.imagequery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class fullImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        Intent x = getIntent();
        String y = x.getStringExtra("BUNDLE");

        Glide.with(this)
                .load(y)
                .into(fullScreenImageView);
    }
}

