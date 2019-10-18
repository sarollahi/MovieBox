package com.aastudio.sarollahi.moviebox.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentFavoriteMovies;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentFavoriteTvShows;

import com.android.sarollahi.moviebox.R;

public class FavoriteActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setSubtitle(R.string.my_favorite_collection);



        tabLayout = findViewById(R.id.searchtabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentFavoriteMovies(),"MOVIES");
        adapter.AddFragment(new FragmentFavoriteTvShows(),"TV SHOWS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        tabLayout = findViewById(R.id.searchtabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentFavoriteMovies(),"MOVIES");
        adapter.AddFragment(new FragmentFavoriteTvShows(),"TV SHOWS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
