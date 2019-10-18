package com.aastudio.sarollahi.moviebox.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentOverviewTv;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentTvReviews;
import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.aastudio.sarollahi.moviebox.Util.CustomViewPager;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TvDetailActivity extends AppCompatActivity {

    private TextView title;
    private TextView year;
    private TextView rated;
    private TextView released;
    private TextView runTime;
    private TextView genre;
    private ImageView poster;
    private RequestQueue queue;
    private String tvImdbId;
    private LinearLayout iGallery;
    private LayoutInflater mInflater;
    private DbHelper dbHelper;
    private String tvId;

    private String mtype;
    private String myear;
    private String mplot;
    private String mol;
    private String mposter;
    private Boolean seenbtn = true;
    private Boolean favbtn = true;
    private ImageButton play_button;
    private TextView trailerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("");
        mInflater = LayoutInflater.from(this);

        dbHelper = new DbHelper(this);

        queue = Volley.newRequestQueue(this);

        TabLayout tabLayout = findViewById(R.id.detailtabs);
        CustomViewPager viewPager = findViewById(R.id.detailviewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentOverviewTv(), "Overviews");
        adapter.AddFragment(new FragmentTvReviews(), "Reviews");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        TvShow tvShow = (TvShow) getIntent().getSerializableExtra("movie");
        tvId = tvShow.getMovieId();

        if(dbHelper.getmovie(tvId) == 0){
            seenbtn = false;
        }else {
            seenbtn = true;
        }

        if(dbHelper.getfav(tvId) == 0){
            favbtn = false;
        }else {
            favbtn = true;
        }

        setUpUI();
        getTvDetails(tvId);

        final View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.menu_buttons, null);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(v);

        ImageButton back = v.findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        MaterialFavoriteButton favorite = (MaterialFavoriteButton) v.findViewById(R.id.favorite_button);
        favorite.setFavorite(favbtn, false);
        favorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            delfavmovie(getWindow().getDecorView());
                        } else {
                            favmovie(getWindow().getDecorView());
                        }
                    }
                });

        MaterialFavoriteButton seen = (MaterialFavoriteButton) v.findViewById(R.id.seen_button);
        seen.setFavorite(seenbtn, false);
        seen.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            delsavemovie(getWindow().getDecorView());
                        } else {
                            savemovie(getWindow().getDecorView());
                        }
                    }
                });

        play_button = (ImageButton) v.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trailerText.getText().toString().equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+ trailerText.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("VIDEO_ID", trailerText.getText().toString());
                    startActivity(intent);
                }else {
                    play_button.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.baseline_tv_off_24));
                }

            }
        });
    }

    private void savemovie(View v) {

        TvShow tv1 = new TvShow();

        String mtitle = title.getText().toString();

        tv1.setMovieId(tvId);
        tv1.setTitle(mtitle);
        tv1.setYear(myear);
        tv1.setPoster(mposter);
        tv1.setPlot(mplot);
        tv1.setOriginalLanguage(mol);
        tv1.setType(mtype);

        //Save to DB
        dbHelper.addTv(tv1);

        Snackbar.make(v, R.string.added_to_seen_tvshows, Snackbar.LENGTH_LONG).show();
    }

    private void favmovie(View v) {

        Movie movie1 = new Movie();

        String ttitle = title.getText().toString();

        movie1.setMovieId(tvId);
        movie1.setTitle(ttitle);
        movie1.setYear(myear);
        movie1.setPoster(mposter);
        movie1.setPlot(mplot);
        movie1.setOriginalLanguage(mol);
        movie1.setType(mtype);

        //Save to DB
        dbHelper.addFavorite(movie1);

        Snackbar.make(v, R.string.added_to_favorites, Snackbar.LENGTH_LONG).show();
    }

    private void delsavemovie(View v) {

        dbHelper.deleteMovie(Integer.parseInt(tvId));

        Snackbar.make(v, R.string.deleted_from_seen_tvshws, Snackbar.LENGTH_LONG).show();
    }

    private void delfavmovie(View v) {

        dbHelper.deleteFavorite(Integer.parseInt(tvId));

        Snackbar.make(v, R.string.deleted_from_favorites, Snackbar.LENGTH_LONG).show();
    }

    private void getTvDetails(final String tvId) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Constants.TV_SHOW_INFO_LEFT + tvId + Constants.TV_SHOW_INFO_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    mposter = "https://image.tmdb.org/t/p/original" + response.getString("poster_path");
                    myear = response.getString("first_air_date");
                    mol = response.getString("original_language");
                    mplot = response.getString("overview");
                    mtype = "t";

                    Picasso.get()
                            .load("https://image.tmdb.org/t/p/original" + response.getString("backdrop_path"))
                            .into(poster);

                    JSONObject imdbid = response.getJSONObject("external_ids");
                    tvImdbId = imdbid.getString("imdb_id");


                    JSONObject tvimages = response.getJSONObject("images");
                    try {

                        JSONArray similars = tvimages.getJSONArray("backdrops");

                        String movieimage = null;

                        if (similars.length() > 0) {

                            for (int i = 0; i <= similars.length(); i++) {

                                JSONObject similar = similars.getJSONObject(i);

                                movieimage = similar.getString("file_path");
                                View view = mInflater.inflate(R.layout.index_movie_images_gallery,
                                        iGallery, false);
                                ImageView movieimg = (ImageView) view
                                        .findViewById(R.id.id_index_movie_images);
                                Picasso.get()
                                        .load("https://image.tmdb.org/t/p/w300" + movieimage)
                                        .into(movieimg);
                                iGallery.addView(view);

                                final String finalMovieimage = movieimage;
                                movieimg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Picasso.get()
                                                .load("https://image.tmdb.org/t/p/original" + finalMovieimage)
                                                .into(poster);
                                    }
                                });
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject trailer = response.getJSONObject("videos");
                    try {
                        final JSONArray video = trailer.getJSONArray("results");


                        if (video.length() > 0) {

                            JSONObject play = video.getJSONObject(0);
                            trailerText.setText(play.getString("key"));

                        }else {
                            trailerText.setText("");
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JsonObjectRequest jsonomdbObjectRequest = new JsonObjectRequest(Request.Method.GET,
                            Constants.URL + tvImdbId + Constants.API_KEY, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response1) {
                            try {
                                if (response1.has("Ratings")) {
                                    JSONArray ratings = response1.getJSONArray("Ratings");

                                    String source = null;
                                    String value = null;

                                    title.setText(response1.getString("Title"));
                                    year.setText("(" + response1.getString("Year") + ")");
                                    rated.setText(response1.getString("Rated"));
                                    released.setText(response1.getString("Released"));
                                    runTime.setText(response1.getString("Runtime"));
                                    genre.setText(response1.getString("Genre"));

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d("Error:", error.getMessage());

                        }
                    });


                    queue.add(jsonomdbObjectRequest);


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
    }


    private void setUpUI() {

        rated = findViewById(R.id.movieRateIDDets);
        title = findViewById(R.id.movieTitleIDDet);
        year = findViewById(R.id.movieYeayIDDet);
        trailerText = findViewById(R.id.trailer1);
        released = findViewById(R.id.movieReleaseIDDets);
        runTime = findViewById(R.id.movieTimeIDDets);
        genre = findViewById(R.id.movieGenreIDDet);
        poster = findViewById(R.id.moviePoster);
        iGallery = (LinearLayout) findViewById(R.id.movie_images_gallery);
    }
}
