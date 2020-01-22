package com.aastudio.sarollahi.moviebox.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.ReviewRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.aastudio.sarollahi.moviebox.Model.Review;
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

public class FragmentReviews extends Fragment {

    View v;
    private Movie movie;
    private String movieId;
    private RecyclerView recyclerView;
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    private List<Review> reviewList;
    private RequestQueue queue;
    private Context mContext;


    public FragmentReviews() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.review_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerViewReview);
        recyclerView.setHasFixedSize(true);
        reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(getContext(), reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reviewRecyclerViewAdapter);
        reviewRecyclerViewAdapter.notifyDataSetChanged();


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        queue = Volley.newRequestQueue(mContext);

        movie = (Movie) getActivity().getIntent().getSerializableExtra("movie");
        movieId = movie.getMovieId();

        reviewList = new ArrayList<>();

        getReview getReviews = new getReview();
        getReviews.execute(movieId);
    }

    class getReview extends AsyncTask<String, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(String... id) {
            reviewList.clear();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    Constants.MAIN_INFO_URL_LEFT + id[0] + Constants.MAIN_INFO_URL_RIGHT_REVIEWS, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject reviewss = response.getJSONObject("reviews");
                        JSONArray reviews = reviewss.getJSONArray("results");


                        String name = null;
                        String text = null;

                        if (reviews.length() > 0) {

                            for (int i = 0; i <= 0; i++) {

                                JSONObject rev = reviews.getJSONObject(i);
                                Review review = new Review();
                                review.setName(rev.getString("author") + ":");
                                review.setText(rev.getString("content"));

                                reviewList.add(review);
                            }

                            reviewRecyclerViewAdapter.notifyDataSetChanged();

                        } else {
                            Review review = new Review();
                            review.setName("No reviews found");
                            review.setText("");

                            reviewList.add(review);

                            reviewRecyclerViewAdapter.notifyDataSetChanged();
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
}
