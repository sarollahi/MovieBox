package com.aastudio.sarollahi.moviebox.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.sarollahi.moviebox.R;
import com.squareup.picasso.Picasso;

public class BigImageActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle personimage = getIntent().getExtras();
        String image = personimage.getString("actor");

        imageView = findViewById(R.id.bigsize);

        if (image != null || image != ""){
            Picasso.get()
                    .load("https://image.tmdb.org/t/p/original" + image)
                    .into(imageView);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
