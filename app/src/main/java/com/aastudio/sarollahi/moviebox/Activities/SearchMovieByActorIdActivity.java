package com.aastudio.sarollahi.moviebox.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentActorMovies;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentActorTvShows;
import com.aastudio.sarollahi.moviebox.Model.Actor;
import com.android.sarollahi.moviebox.R;
import com.google.android.material.tabs.TabLayout;

public class SearchMovieByActorIdActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Actor actor;
    private String actorname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie_by_actor_id);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actor = (Actor) getIntent().getSerializableExtra("actor");
        actorname = actor.getName();

        getSupportActionBar().setTitle(actorname);


        tabLayout = findViewById(R.id.searchtabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentActorMovies(), "MOVIES");
        adapter.AddFragment(new FragmentActorTvShows(), "TV SHOWS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
