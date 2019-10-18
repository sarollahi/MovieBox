package com.aastudio.sarollahi.moviebox.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentMovies;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentTvShows;
import com.android.sarollahi.moviebox.R;

public class SearchActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        tabLayout = findViewById(R.id.searchtabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentMovies(),"MOVIES");
        adapter.AddFragment(new FragmentTvShows(),"TV SHOWS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
