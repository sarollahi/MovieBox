package com.aastudio.sarollahi.moviebox.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Data.SeenMoviesRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.android.sarollahi.moviebox.R;

import java.util.ArrayList;
import java.util.List;

public class SeenMoviesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SeenMoviesRecyclerViewAdapter seenMoviesRecyclerViewAdapter;
    private List<Movie> movieList;
    private List<Movie> listItems;
    private DbHelper db;
    private AppCompatActivity activity = SeenMoviesActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_movies);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setSubtitle(R.string.my_movies_collection);

        getmovies();
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        movieList.clear();
        getmovies();
    }

    private void getmovies() {

        db = new DbHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();

        // Get items from database
        movieList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        movieList = db.getAllmovies();

        for (Movie c : movieList) {
            Movie movie = new Movie();
            movie.setTitle(c.getTitle());
            movie.setYear(c.getYear());
            movie.setOriginalLanguage(c.getOriginalLanguage());
            movie.setDateItemAdded(c.getDateItemAdded());
            movie.setPoster(c.getPoster());
            movie.setPlot(c.getPlot());
            movie.setMovieId(c.getMovieId());

            listItems.add(movie);

        }

        seenMoviesRecyclerViewAdapter = new SeenMoviesRecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(seenMoviesRecyclerViewAdapter);
        seenMoviesRecyclerViewAdapter.notifyDataSetChanged();
    }


}
