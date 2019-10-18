package com.aastudio.sarollahi.moviebox.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentBiography;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentImages;
import com.aastudio.sarollahi.moviebox.Util.CustomViewPager;
import com.android.sarollahi.moviebox.R;

public class PersonActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle person = getIntent().getExtras();

        String personName = person.getString("personName");

        getSupportActionBar().setTitle(personName);


        TabLayout tabLayout = findViewById(R.id.persondetailtabs);
        CustomViewPager viewPager = findViewById(R.id.persondetailviewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentBiography(), "Biography");
        adapter.AddFragment(new FragmentImages(), "Images");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
