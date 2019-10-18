package com.aastudio.sarollahi.moviebox.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aastudio.sarollahi.moviebox.Data.ReviewRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Review;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTvReviews extends Fragment {

    View v;
    private TvShow movie;
    private String movieId;
    private RecyclerView recyclerView;
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    private List<Review> reviewList;
    private RequestQueue queue;

    public FragmentTvReviews() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.review_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewReview);
        recyclerView.setHasFixedSize(true);
        reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(getContext(), reviewList );
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reviewRecyclerViewAdapter);
        reviewRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity());

        movie = (TvShow) getActivity().getIntent().getSerializableExtra("movie");
        movieId = movie.getMovieId();

        reviewList = new ArrayList<>();

        reviewList = getreviews(movieId);
    }

    private List<Review> getreviews(String movieid) {
        reviewList.clear();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.TV_SHOW_INFO_LEFT + movieid + Constants.MAIN_INFO_URL_RIGHT_REVIEWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject reviewss = response.getJSONObject("reviews");
                    JSONArray reviews = reviewss.getJSONArray("results");

                    String name = null;
                    String text = null;

                    if (reviews.length() > 0) {

                        for (int i = 0; i <= reviews.length(); i++) {

                            JSONObject rev = reviews.getJSONObject(i);
                            Review review = new Review();
                            review.setName(rev.getString("author")+":");
                            review.setText(rev.getString("content"));

                            reviewList.add(review);
                        }

                        reviewRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                    }else {
                        Review review = new Review();
                        review.setName("No reviews found");
                        review.setText("");

                        reviewList.add(review);

                        reviewRecyclerViewAdapter.notifyDataSetChanged();//Important!!
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
        return reviewList;
    }
}
