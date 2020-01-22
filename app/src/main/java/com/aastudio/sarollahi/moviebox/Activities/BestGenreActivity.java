package com.aastudio.sarollahi.moviebox.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.MovieRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Data.TvRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BestGenreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private TvRecyclerViewAdapter tvRecyclerViewAdapter;
    private List<Movie> movieList;
    private List<TvShow> tvList;
    private RequestQueue queue;
    private Spinner spinner1;
    private Spinner spinner2;
    private List<String> list1;
    private List<String> list2;
    private Button filter;
    private NativeBannerAd nativeBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_genre);
        queue = Volley.newRequestQueue(BestGenreActivity.this);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle genreName = getIntent().getExtras();
        final String genre = genreName.getString("id");
        String type = genreName.getString("type");
        final String genre_key = genreName.getString("key");

        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();

        spinner1 = findViewById(R.id.year1);
        spinner2 = findViewById(R.id.year2);
        filter = findViewById(R.id.setYear);

        getYear();
        getlist2();

        spinner1.setSelection(list1.size() - 1);
        spinner2.setSelection(list2.size() - 1);


        movieList = new ArrayList<>();
        tvList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewbest);
        recyclerView.setHasFixedSize(true);

        final String secyear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

        if (type.equals("movie")) {
            getSupportActionBar().setTitle("Best of " + genre_key + " movies");
            movieList = getBestm(genre, secyear, secyear);
            movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(movieRecyclerViewAdapter);
            movieRecyclerViewAdapter.notifyDataSetChanged();

            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getlist2();
                    spinner2.setSelection(list2.size() - 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieList = getBestm(genre, String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()));
                    movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getApplicationContext(), movieList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(movieRecyclerViewAdapter);
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
            });

        } else if (type.equals("tv")) {
            getSupportActionBar().setTitle("Best of " + genre_key + " tv shows");
            tvList = getBestt(genre, secyear, secyear);
            tvRecyclerViewAdapter = new TvRecyclerViewAdapter(this, tvList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(tvRecyclerViewAdapter);
            tvRecyclerViewAdapter.notifyDataSetChanged();

            spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getlist2();
                    spinner2.setSelection(list2.size() - 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvList = getBestt(genre, String.valueOf(spinner1.getSelectedItem()), String.valueOf(spinner2.getSelectedItem()));
                    tvRecyclerViewAdapter = new TvRecyclerViewAdapter(getApplicationContext(), tvList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(tvRecyclerViewAdapter);
                    tvRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }

        nativeBannerAd = new NativeBannerAd(this, Constants.Best_Genre_ZONE_ID);
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

    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        NativeAdLayout nativeAdLayout = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(BestGenreActivity.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(BestGenreActivity.this, nativeBannerAd, nativeAdLayout);
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
    protected void onResume() {
        super.onResume();
        try {
            nativeBannerAd = new NativeBannerAd(this, Constants.Best_Genre_ZONE_ID);
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

    private void getYear() {

        list1.clear();

        list1 = new ArrayList<String>();

        for (int i = 1900; i < Calendar.getInstance().get(Calendar.YEAR) + 1; i++) {
            list1.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list1);

        spinner1.setAdapter(adapter);
    }

    private void getlist2() {

        list2.clear();

        list2 = new ArrayList<String>();

        for (int i = Integer.parseInt(String.valueOf(spinner1.getSelectedItem())); i < Calendar.getInstance().get(Calendar.YEAR) + 1; i++) {
            list2.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list2);

        spinner2.setAdapter(adapter2);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private List<Movie> getBestm(String id, String firstYear, String secondYear) {

        movieList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.BEST_MOVIE_GENRE_URL_LEFT + firstYear + Constants.BEST_MOVIE_GENRE_URL_MIDDLE + secondYear + Constants.BEST_MOVIE_GENRE_URL_RIGHT + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray moviesArray = response.getJSONArray("results");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        movie.setYear(movieObj.getString("release_date"));

                        movie.setOriginalLanguage(movieObj.getString("original_language"));
                        movie.setPlot(movieObj.getString("overview"));
                        movie.setPoster("http://image.tmdb.org/t/p/w185" + movieObj.getString("poster_path"));
                        movie.setMovieId(movieObj.getString("id"));

                        movieList.add(movie);


                    }

                    movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return movieList;
    }

    private List<TvShow> getBestt(String id, String firstYear, String secondYear) {

        tvList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.BEST_TV_GENRE_URL_LEFT + firstYear + Constants.BEST_TV_GENRE_URL_MIDDLE + secondYear + Constants.BEST_TV_GENRE_URL_RIGHT + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray moviesArray = response.getJSONArray("results");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        TvShow movie = new TvShow();
                        movie.setTitle(movieObj.getString("name"));
                        movie.setYear(movieObj.getString("first_air_date"));

                        movie.setOriginalLanguage(movieObj.getString("original_language"));
                        movie.setPlot(movieObj.getString("overview"));
                        movie.setPoster("http://image.tmdb.org/t/p/w185" + movieObj.getString("poster_path"));
                        movie.setMovieId(movieObj.getString("id"));

                        tvList.add(movie);


                    }

                    tvRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return tvList;
    }


}
