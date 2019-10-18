package com.aastudio.sarollahi.moviebox.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.aastudio.sarollahi.moviebox.Data.MovieRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Movie;
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

public class FragmentPopular extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    LinearLayoutManager manager;

    private static final int PAGE_START = 1;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    int currentItems, totalItems, scrollOutItems;
    Boolean isScrolling = false;



    public FragmentPopular() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.popular_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewPopular);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext(), movieList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.addOnScrollListener(scrollListener);
        movieRecyclerViewAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity());

        movieList = new ArrayList<>();

        // getMovies(search);
        movieList = getMovies(currentPage);
    }

    private final RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            currentItems = manager.getChildCount();
            totalItems = manager.getItemCount();
            scrollOutItems = manager.findFirstVisibleItemPosition();

            if(isScrolling && (currentItems + scrollOutItems == totalItems) && currentPage<TOTAL_PAGES)
            {

                isScrolling = false;
                currentPage++;
                movieList = getMovies(currentPage);

            }
        }
    };

    private List<Movie> getMovies(int page) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.POPULAR_URL+page,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    TOTAL_PAGES = Integer.parseInt(response.getString("total_pages"));
                    JSONArray moviesArray = response.getJSONArray("results");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        movie.setYear(movieObj.getString("release_date"));

                        movie.setOriginalLanguage(movieObj.getString("original_language"));
                        movie.setPlot(movieObj.getString("overview"));
                        movie.setPoster("http://image.tmdb.org/t/p/w185"+movieObj.getString("poster_path"));
                        movie.setMovieId(movieObj.getString("id"));

                        movieList.add(movie);


                    }

                    movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                }catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(getView(),R.string.No_movies_found,Snackbar.LENGTH_LONG).show();
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

   }
