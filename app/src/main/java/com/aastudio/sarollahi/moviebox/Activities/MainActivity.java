package com.aastudio.sarollahi.moviebox.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentNow;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentPopular;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentTop;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentUp;
import com.aastudio.sarollahi.moviebox.Model.Genre;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private DrawerLayout drawer;
    private ArrayList<Genre> movieGenreArrayList;
    private ArrayList<Genre> tvGenreArrayList;
    private ArrayList<String> mnames = new ArrayList<String>();
    private ArrayList<String> tnames = new ArrayList<String>();
    private HashMap<String, String> moviemap = new HashMap<String, String>();
    private HashMap<String, String> tvmap = new HashMap<String, String>();
    private Spinner spinnerMovie;
    private Spinner spinnerTv;
    private EditText actorname;
    private NativeBannerAd nativeBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AudienceNetworkAds.initialize(this);


        nativeBannerAd = new NativeBannerAd(this, Constants.MAIN_ZONE_ID);
        nativeBannerAd.setAdListener(new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);
                List<View> clickableViews = new ArrayList<>();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        // load the ad
        nativeBannerAd.loadAd();


        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentUp(), "UPCOMING");
        adapter.AddFragment(new FragmentNow(), "NOW PLAYING");
        adapter.AddFragment(new FragmentPopular(), "POPULAR");
        adapter.AddFragment(new FragmentTop(), "TOP RATED");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(0);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView2 = findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        mnames.add("Select an Item...");
        tnames.add("Select an Item...");

        spinnerMovie = (Spinner) navigationView2.getMenu().findItem(R.id.navigation_drawer_item3).getActionView();
        spinnerTv = (Spinner) navigationView2.getMenu().findItem(R.id.navigation_drawer_item4).getActionView();
        actorname = (EditText) navigationView2.getMenu().findItem(R.id.navigation_drawer_item5).getActionView();
        Button actorbtn = (Button) navigationView2.getMenu().findItem(R.id.navigation_drawer_item6).getActionView();

        actorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actorname.getText().toString().trim() != "") {
                    Intent intent = new Intent(MainActivity.this, SearchByActorActivity.class);
                    intent.putExtra("actname", actorname.getText().toString());
                    startActivity(intent);
                }
            }
        });


        getMovieGenres movieGenres = (getMovieGenres) new getMovieGenres().execute();
        getTvGenres tvGenres = (getTvGenres) new getTvGenres().execute();

        spinnerMovie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);

                String genre_Name = parentView.getItemAtPosition(position).toString();
                if (genre_Name != "Select an Item...") {
                    String genre_Key = moviemap.get(genre_Name);
                    Intent intent = new Intent(MainActivity.this, BestGenreActivity.class);
                    intent.putExtra("id", genre_Key);
                    intent.putExtra("type", "movie");
                    intent.putExtra("key", genre_Name);
                    startActivity(intent);
                    spinnerMovie.setSelection(0);
                    drawer.closeDrawer(GravityCompat.END);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.GRAY);

                String genre_Name = parentView.getItemAtPosition(position).toString();
                if (genre_Name != "Select an Item...") {
                    String genre_Keys = tvmap.get(genre_Name);
                    Intent intent = new Intent(MainActivity.this, BestGenreActivity.class);
                    intent.putExtra("id", genre_Keys);
                    intent.putExtra("type", "tv");
                    intent.putExtra("key", genre_Name);
                    startActivity(intent);
                    spinnerTv.setSelection(0);
                    drawer.closeDrawer(GravityCompat.END);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            nativeBannerAd = new NativeBannerAd(this, Constants.MAIN_ZONE_ID);
            nativeBannerAd.setAdListener(new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Race condition, load() called again before last ad was displayed
                    if (nativeBannerAd == null || nativeBannerAd != ad) {
                        return;
                    }
                    // Inflate Native Banner Ad into Container
                    inflateAd(nativeBannerAd);
                    List<View> clickableViews = new ArrayList<>();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }

            });
            // load the ad
            nativeBannerAd.loadAd();
        } catch (Error error) {
        }
    }


    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(MainActivity.this, nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showInputDialog();
            // return true;
        } else if (id == R.id.drawer_right) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movies) {

            startActivity(new Intent(MainActivity.this, SeenMoviesActivity.class));

        } else if (id == R.id.nav_tv_shows) {

            startActivity(new Intent(MainActivity.this, SeenTvActivity.class));

        } else if (id == R.id.nav_wish) {

            startActivity(new Intent(MainActivity.this, FavoriteActivity.class));

        } else if (id == R.id.nav_about) {

            alertDialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.about_view, null);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            ImageButton playicon = dialog.findViewById(R.id.imgPlayStore);
            TextView playtext = dialog.findViewById(R.id.txtInfoRate);

            playicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

            playtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

        } else if (id == R.id.nav_contact) {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://github.com/sarollahi/MovieBox"));
            startActivity(intent);

        } else if (id == R.id.nav_br) {
            startActivity(new Intent(MainActivity.this, BackupRestoreActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void showInputDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        final EditText newSearchEdt = view.findViewById(R.id.searchEdt);
        final EditText newSearchyear = view.findViewById(R.id.movieyear);
        Button submitButton = view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String nametemp = newSearchEdt.getText().toString();
                    String yeartemp = newSearchyear.getText().toString();
                    String search = nametemp.replaceAll(" ", "%20");
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("movieName", search);
                    intent.putExtra("movieYear", yeartemp);

                    startActivity(intent);
                }
                dialog.dismiss();


            }
        });
    }

    class getMovieGenres extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.MOVIE_GENRE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject obj = new JSONObject(response);

                                moviemap = new HashMap<String, String>();
                                movieGenreArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("genres");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    Genre moviegenre = new Genre();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    moviegenre.setName(dataobj.getString("name"));
                                    moviegenre.setId(dataobj.getString("id"));

                                    moviemap.put(dataobj.getString("name"), dataobj.getString("id"));

                                    movieGenreArrayList.add(moviegenre);

                                }

                                for (int i = 0; i < movieGenreArrayList.size(); i++) {
                                    mnames.add(movieGenreArrayList.get(i).getName());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), simple_spinner_item, mnames);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spinnerMovie.setAdapter(spinnerArrayAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs
                        }
                    });

            // request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            requestQueue.add(stringRequest);
            return null;
        }
    }

    class getTvGenres extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.TV_GENRE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject obj = new JSONObject(response);

                                tvmap = new HashMap<String, String>();
                                tvGenreArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("genres");

                                for (int j = 0; j < dataArray.length(); j++) {

                                    Genre tvgenre = new Genre();
                                    JSONObject dataobj = dataArray.getJSONObject(j);

                                    tvgenre.setName(dataobj.getString("name"));
                                    tvgenre.setId(dataobj.getString("id"));

                                    tvmap.put(dataobj.getString("name"), dataobj.getString("id"));

                                    tvGenreArrayList.add(tvgenre);

                                }

                                for (int j = 0; j < tvGenreArrayList.size(); j++) {
                                    tnames.add(tvGenreArrayList.get(j).getName());
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), simple_spinner_item, tnames);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spinnerTv.setAdapter(spinnerArrayAdapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //displaying the error in toast if occurrs

                        }
                    });

            // request queue
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            requestQueue.add(stringRequest);
            return null;
        }
    }
}
