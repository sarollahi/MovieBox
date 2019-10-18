package com.aastudio.sarollahi.moviebox.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Data.SeenTvRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.android.sarollahi.moviebox.R;

import java.util.ArrayList;
import java.util.List;

public class SeenTvActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SeenTvRecyclerViewAdapter seenTvRecyclerViewAdapter;
    private List<TvShow> tvShowList;
    private List<TvShow> listItems;
    private DbHelper db;;
    private AppCompatActivity activity = SeenTvActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_tvs);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setSubtitle(R.string.my_tv_shows_collection);

        getTvs();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvShowList.clear();
        getTvs();
    }

    private void getTvs() {

        db = new DbHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvShowList = new ArrayList<>();

        // Get items from database
        tvShowList = new ArrayList<>();
        listItems = new ArrayList<>();

        // Get items from database
        tvShowList = db.getAlltvshows();

        for (TvShow c : tvShowList) {
            TvShow tv = new TvShow();
            tv.setTitle(c.getTitle());
            tv.setYear(c.getYear());
            tv.setOriginalLanguage(c.getOriginalLanguage());
            tv.setDateItemAdded(c.getDateItemAdded());
            tv.setPoster(c.getPoster());
            tv.setPlot(c.getPlot());
            tv.setMovieId(c.getMovieId());

            listItems.add(tv);

        }

        seenTvRecyclerViewAdapter = new SeenTvRecyclerViewAdapter(this, listItems);
        recyclerView.setAdapter(seenTvRecyclerViewAdapter);
        seenTvRecyclerViewAdapter.notifyDataSetChanged();
    }
}
