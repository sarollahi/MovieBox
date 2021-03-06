package com.aastudio.sarollahi.moviebox.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.aastudio.sarollahi.moviebox.Activities.PersonActivity;
import com.aastudio.sarollahi.moviebox.Activities.SeasonActivity;
import com.aastudio.sarollahi.moviebox.Activities.TvDetailActivity;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentOverviewTv extends Fragment {

    View v;

    private TvShow tvShow;
    private TextView plot;
    private TextView language;
    private TextView companies;
    private TextView country;
    private TextView awards;
    private TextView awardsTitle;
    private CardView awardsCard;
    private TextView writerTitle;
    private CardView writerCard;
    private TextView directorTitle;
    private CardView directorCard;
    private TextView metascore;
    private TextView score;
    private TextView imdbRating;
    private TextView tomatoRating;
    private TextView boxOffice;
    private ImageButton facebook;
    private ImageButton instagram;
    private ImageButton homepage;
    private ImageButton imdb_id;
    private TextView budget;
    private String tvId;
    private LinearLayout mGallery;
    private LinearLayout dGallery;
    private LinearLayout wGallery;
    private LinearLayout sGallery;
    private LinearLayout rGallery;
    private LinearLayout seasonGallery;
    private LayoutInflater mInflater;
    private RequestQueue queue;
    private String tvImdbId;
    private Context mContext;
    private ProgressBar mProgressBar;
    private RelativeLayout layout;
    private NativeBannerAd nativeBannerAd;

    public FragmentOverviewTv() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tv_overview_fragment, container, false);

        plot = v.findViewById(R.id.plotDet);
        language = v.findViewById(R.id.lanDet);
        country = v.findViewById(R.id.countryDet);
        awards = v.findViewById(R.id.awardsDet);
        awardsTitle = v.findViewById(R.id.awardst);
        awardsCard = v.findViewById(R.id.awardscard);
        writerTitle = v.findViewById(R.id.writertitle);
        writerCard = v.findViewById(R.id.writercard);
        directorTitle = v.findViewById(R.id.directortitle);
        directorCard = v.findViewById(R.id.directorcard);
        metascore = v.findViewById(R.id.metacriticRating);
        score = v.findViewById(R.id.scoreRating);
        imdbRating = v.findViewById(R.id.imdbRating);
        tomatoRating = v.findViewById(R.id.tomatosRating);
        boxOffice = v.findViewById(R.id.boxOfficeDets);
        homepage = v.findViewById(R.id.homepage);
        imdb_id = v.findViewById(R.id.imdb_id);
        facebook = v.findViewById(R.id.facebook);
        instagram = v.findViewById(R.id.instagram);
        companies = v.findViewById(R.id.production_companiesDet);
        budget = v.findViewById(R.id.budgetDets);
        mGallery = v.findViewById(R.id.id_gallery);
        dGallery = v.findViewById(R.id.director_gallery);
        wGallery = v.findViewById(R.id.writer_gallery);
        sGallery = v.findViewById(R.id.similar_gallery);
        rGallery = v.findViewById(R.id.recommend_gallery);
        seasonGallery = v.findViewById(R.id.seasons_gallery);
        layout = v.findViewById(R.id.overlayout);
        mProgressBar = v.findViewById(R.id.progressBartv);

        nativeBannerAd = new NativeBannerAd(getContext(), Constants.Tv_Overview_ZONE_ID);
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
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            nativeBannerAd = new NativeBannerAd(getContext(), Constants.Tv_Overview_ZONE_ID);
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
        NativeAdLayout nativeAdLayout = v.findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(getContext(), nativeBannerAd, nativeAdLayout);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = LayoutInflater.from(mContext);

        queue = Volley.newRequestQueue(mContext);

        tvShow = (TvShow) getActivity().getIntent().getSerializableExtra("movie");
        tvId = tvShow.getMovieId();

        getTvDetails getTvDetail = new getTvDetails();
        getTvDetail.execute(tvId);
    }

    class getTvDetails extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... id) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                    , Constants.TV_SHOW_INFO_LEFT + id[0] + Constants.TV_SHOW_INFO_RIGHT, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        score.setText(response.getString("vote_average"));
                        budget.setText(response.getString("number_of_seasons"));
                        boxOffice.setText(response.getString("number_of_episodes"));

                        JSONObject imdbid = response.getJSONObject("external_ids");
                        tvImdbId = imdbid.getString("imdb_id");

                        if (!response.isNull("homepage")) {

                            final String finalWeb = response.getString("homepage");
                            homepage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse(finalWeb));
                                    startActivity(intent);
                                }
                            });

                        }

                        try {

                            JSONArray production = response.getJSONArray("production_companies");

                            if (production.length() > 0) {

                                String name = "";

                                StringBuffer sb = new StringBuffer();

                                for (int i = 0; i <= production.length(); i++) {

                                    JSONObject pcamp = production.getJSONObject(i);

                                    name = pcamp.getString("name");
                                    sb.append(name + ", ");
                                    String compa = sb.substring(0, sb.length() - 2);
                                    companies.setText(compa);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {
                            JSONObject exlink = response.getJSONObject("external_ids");


                            if (!exlink.isNull("imdb_id")) {

                                final String im = exlink.getString("imdb_id");
                                imdb_id.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                        intent.setData(Uri.parse("https://www.imdb.com/name/" + im + "/"));
                                        startActivity(intent);
                                    }
                                });

                            }
                            if (!exlink.isNull("facebook_id")) {

                                final String face = exlink.getString("facebook_id");
                                facebook.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                        intent.setData(Uri.parse("https://www.facebook.com/" + face + "/"));
                                        startActivity(intent);
                                    }
                                });

                            }
                            if (!exlink.isNull("instagram_id")) {

                                final String insta = exlink.getString("instagram_id");
                                instagram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                        intent.setData(Uri.parse("https://www.instagram.com/" + insta + "/"));
                                        startActivity(intent);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        JSONArray season = response.getJSONArray("seasons");
                        try {

                            String image = null;
                            String num = null;
                            String seasonid = null;
                            String name = null;

                            if (season.length() > 0) {

                                for (int i = 0; i <= season.length(); i++) {

                                    JSONObject cast = season.getJSONObject(i);
                                    image = cast.getString("poster_path");
                                    name = cast.getString("name");
                                    seasonid = cast.getString("id");
                                    num = cast.getString("season_number");
                                    View view = mInflater.inflate(R.layout.index_seasons_gallery,
                                            seasonGallery, false);
                                    ImageView img = view
                                            .findViewById(R.id.id_index_seasons_image);
                                    Picasso.get()
                                            .load("https://image.tmdb.org/t/p/h632" + image).placeholder(R.drawable.noimage)
                                            .into(img);
                                    TextView acharacter = view
                                            .findViewById(R.id.id_index_seasons_name);
                                    acharacter.setText(name);
                                    seasonGallery.addView(view);

                                    final String finalSeasonid = num;
                                    img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), SeasonActivity.class);
                                            intent.putExtra("seasonId", finalSeasonid);
                                            intent.putExtra("tvId", tvId);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        JSONObject credits = response.getJSONObject("credits");
                        try {


                            JSONArray casts = credits.getJSONArray("cast");

                            String image = null;
                            String name = null;
                            String actorid = null;
                            String character = null;

                            if (casts.length() > 0) {

                                for (int i = 0; i <= casts.length(); i++) {

                                    JSONObject cast = casts.getJSONObject(i);
                                    image = cast.getString("profile_path");
                                    name = cast.getString("name");
                                    actorid = cast.getString("id");
                                    character = cast.getString("character");
                                    View view = mInflater.inflate(R.layout.index_actors_gallery,
                                            mGallery, false);
                                    ImageView img = view
                                            .findViewById(R.id.id_index_actors_image);
                                    Picasso.get()
                                            .load("https://image.tmdb.org/t/p/h632" + image).placeholder(R.drawable.noimage)
                                            .into(img);
                                    TextView aname = view
                                            .findViewById(R.id.id_index_actors_name);
                                    aname.setText(name);
                                    TextView acharacter = view
                                            .findViewById(R.id.id_index_actors_character);
                                    acharacter.setText(character);
                                    mGallery.addView(view);

                                    final String finalActorid = actorid;
                                    String finalName = name;
                                    img.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), PersonActivity.class);
                                            intent.putExtra("personId", finalActorid);
                                            intent.putExtra("personName", finalName);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {


                            JSONArray crews = credits.getJSONArray("crew");

                            String image1 = null;
                            String name1 = null;
                            String actorid1 = null;
                            String department1 = null;

                            if (crews.length() > 0) {

                                for (int j = 0; j <= crews.length(); j++) {

                                    JSONObject crew = crews.getJSONObject(j);

                                    image1 = crew.getString("profile_path");
                                    name1 = crew.getString("name");
                                    actorid1 = crew.getString("id");
                                    department1 = crew.getString("department");

                                    if (department1.equals("Directing")) {

                                        directorTitle.setVisibility(View.VISIBLE);
                                        directorCard.setVisibility(View.VISIBLE);

                                        View view = mInflater.inflate(R.layout.index_directors_gallery,
                                                dGallery, false);
                                        ImageView img1 = view
                                                .findViewById(R.id.id_index_directors_image);
                                        Picasso.get()
                                                .load("https://image.tmdb.org/t/p/h632" + image1).placeholder(R.drawable.noimage)
                                                .into(img1);
                                        TextView txt1 = view
                                                .findViewById(R.id.id_index_directors_name);
                                        txt1.setText(name1);
                                        dGallery.addView(view);

                                        final String finalActorid = actorid1;
                                        String finalName = name1;
                                        img1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), PersonActivity.class);
                                                intent.putExtra("personId", finalActorid);
                                                intent.putExtra("personName", finalName);
                                                startActivity(intent);
                                            }
                                        });


                                    } else if (department1.equals("Writing")) {

                                        writerTitle.setVisibility(View.VISIBLE);
                                        writerCard.setVisibility(View.VISIBLE);

                                        View view = mInflater.inflate(R.layout.index_writers_gallery,
                                                wGallery, false);
                                        ImageView img1 = view
                                                .findViewById(R.id.id_index_writers_image);
                                        if (crew.getString("profile_path") == null) {
                                            img1.setImageResource(R.drawable.ic_launcher_background);
                                        } else {
                                            Picasso.get()
                                                    .load("https://image.tmdb.org/t/p/h632" + image1).placeholder(R.drawable.noimage)
                                                    .into(img1);
                                        }


                                        TextView txt1 = view
                                                .findViewById(R.id.id_index_writers_name);
                                        txt1.setText(name1);
                                        wGallery.addView(view);

                                        final String finalActorid = actorid1;
                                        String finalName1 = name1;
                                        img1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), PersonActivity.class);
                                                intent.putExtra("personId", finalActorid);
                                                intent.putExtra("personName", finalName1);
                                                startActivity(intent);
                                            }
                                        });
                                    }


                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        JSONObject recommendations = response.getJSONObject("recommendations");
                        try {

                            JSONArray recommendation = recommendations.getJSONArray("results");

                            String movieimage = null;
                            String movieid = null;

                            if (recommendation.length() > 0) {

                                for (int i = 0; i <= recommendation.length(); i++) {

                                    JSONObject similar = recommendation.getJSONObject(i);
                                    movieimage = similar.getString("poster_path");
                                    movieid = similar.getString("id");
                                    View view = mInflater.inflate(R.layout.index_recommended_movies_gallery,
                                            rGallery, false);
                                    ImageView similarimg = view
                                            .findViewById(R.id.id_index_recommended_movies_image);
                                    Picasso.get()
                                            .load("https://image.tmdb.org/t/p/h632" + movieimage).placeholder(R.drawable.noimage)
                                            .into(similarimg);
                                    rGallery.addView(view);

                                    final TvShow movie = new TvShow();
                                    movie.setMovieId(movieid);

                                    similarimg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), TvDetailActivity.class);
                                            intent.putExtra("movie", movie);
                                            startActivity(intent);
                                        }
                                    });
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        JSONObject sim = response.getJSONObject("similar");
                        try {

                            JSONArray similars = sim.getJSONArray("results");

                            String movieimage = null;
                            String moviename = null;
                            String movieid = null;

                            if (similars.length() > 0) {

                                for (int i = 0; i <= similars.length(); i++) {

                                    JSONObject similar = similars.getJSONObject(i);

                                    movieimage = similar.getString("poster_path");
                                    movieid = similar.getString("id");
                                    View view = mInflater.inflate(R.layout.index_similar_movies_gallery,
                                            sGallery, false);
                                    ImageView similarimg = view
                                            .findViewById(R.id.id_index_similar_movies_image);
                                    Picasso.get()
                                            .load("https://image.tmdb.org/t/p/h632" + movieimage).placeholder(R.drawable.noimage)
                                            .into(similarimg);
                                    sGallery.addView(view);

                                    final TvShow movie = new TvShow();
                                    movie.setMovieId(movieid);

                                    similarimg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), TvDetailActivity.class);
                                            intent.putExtra("movie", movie);
                                            startActivity(intent);
                                        }
                                    });
                                }

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

                                        plot.setText(response1.getString("Plot"));
                                        language.setText(response1.getString("Language"));
                                        country.setText(response1.getString("Country"));
                                        if (!(response1.getString("Awards")).equals("N/A")) {
                                            awardsTitle.setVisibility(View.VISIBLE);
                                            awardsCard.setVisibility(View.VISIBLE);
                                            awards.setText(response1.getString("Awards"));
                                        }

                                        if ((response1.getString("imdbRating")).equals("N/A")) {
                                            imdbRating.setText("--");
                                        } else {
                                            imdbRating.setText(response1.getString("imdbRating"));
                                        }
                                        if ((response1.getString("Metascore")).equals("N/A")) {
                                            metascore.setText("--");
                                        } else {
                                            metascore.setText(response1.getString("Metascore"));
                                        }

                                        if (ratings.length() > 0) {

                                            for (int i = 0; i <= ratings.length(); i++) {

                                                JSONObject mRatings = ratings.getJSONObject(i);
                                                source = mRatings.getString("Source");
                                                value = mRatings.getString("Value");

                                                switch (source) {
                                                    case "Rotten Tomatoes":
                                                        tomatoRating.setText(value);
                                                        break;
                                                }
                                            }
                                        } else {
                                            tomatoRating.setText("--");
                                        }


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
                        mProgressBar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);


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
            return null;
        }
    }
}


