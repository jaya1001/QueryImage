package com.example.imagequery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Search extends AppCompatActivity {

    private Button b1;
    private EditText query1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar);

        b1 = (Button) findViewById(R.id.but);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query1 = (EditText) findViewById(R.id.query_image);
                Intent i = new Intent(Search.this, MainActivity.class);
                i.putExtra("query", query1.getText().toString());
                startActivity(i);

            }
        });
    }
}