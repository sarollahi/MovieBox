package com.aastudio.sarollahi.moviebox.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeasonActivity extends AppCompatActivity {

    private RequestQueue queue;
    private TextView title;
    private TextView released;
    private JustifiedTextView plot;
    private ImageView poster;
    private LinearLayout mGallery;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInflater = LayoutInflater.from(this);

        queue = Volley.newRequestQueue(this);

        Bundle movieName = getIntent().getExtras();
        String tv = movieName.getString("tvId");
        String season = movieName.getString("seasonId");

        setUpUI();
        getSeasonDetails(tv,season);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getSeasonDetails(final String tvId,final String seasonnumber) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Constants.TV_SHOW_Episode_Left + tvId + Constants.TV_SHOW_Episode_MIDDLE + seasonnumber + Constants.TV_SHOW_Episode_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                String imdbid = null;
                String web = null;

                try {

                    plot.setText(response.getString("overview"));
                    released.setText(response.getString("air_date"));
                    title.setText(response.getString("name"));
                    Picasso.get().load("https://image.tmdb.org/t/p/h632"+response.getString("poster_path")).into(poster);


                    JSONArray episodeArray = response.getJSONArray("episodes");
                    try {
                        String image = null;
                        String name = null;
                        String plot = null;
                        String date = null;
                        String num = null;

                        if (episodeArray.length() > 0) {

                            for (int i = 0; i <= episodeArray.length(); i++) {

                                JSONObject episode = episodeArray.getJSONObject(i);
                                image = episode.getString("still_path");
                                name = episode.getString("name");
                                plot = episode.getString("overview");
                                date = episode.getString("air_date");
                                num = episode.getString("episode_number");
                                View view = mInflater.inflate(R.layout.index_episodes_gallery,
                                        mGallery, false);
                                ImageView img = (ImageView) view
                                        .findViewById(R.id.id_index_episodes_image);
                                Picasso.get()
                                        .load("https://image.tmdb.org/t/p/h632"+image).placeholder(R.drawable.noimage)
                                        .into(img);
                                TextView anum = (TextView) view
                                        .findViewById(R.id.id_index_episodes_num);
                                anum.setText("Episode "+num +"   -   " + name+"   -   "+ date);
                                TextView aplot = (JustifiedTextView) view
                                        .findViewById(R.id.id_index_episodes_plot);
                                aplot.setText(plot);
                                mGallery.addView(view);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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

        plot = (JustifiedTextView) findViewById(R.id.plotDet);
        title = (TextView) findViewById(R.id.seasonname);
        released = (TextView) findViewById(R.id.seasonReleased);
        poster = (ImageView) findViewById(R.id.seasonimage);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);

    }
}
