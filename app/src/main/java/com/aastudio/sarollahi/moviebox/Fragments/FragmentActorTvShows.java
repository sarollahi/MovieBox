package com.aastudio.sarollahi.moviebox.Fragments;

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

import com.aastudio.sarollahi.moviebox.Data.TvRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Actor;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentActorTvShows extends Fragment {

    View v;
    LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private TvRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<TvShow> movieList;
    private RequestQueue queue;
    private Actor actor;
    private String actorId;

    public FragmentActorTvShows() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tvshows_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerViewTvShows);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new TvRecyclerViewAdapter(getContext(), movieList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        actor = (Actor) getActivity().getIntent().getSerializableExtra("actor");
        assert actor != null;
        actorId = actor.getActorId();

        movieList = new ArrayList<>();

        getTvShows getTvShows = (getTvShows) new getTvShows().execute(actorId);
    }


    class getTvShows extends AsyncTask<String, Void, List<TvShow>> {
        @Override
        protected List<TvShow> doInBackground(String... id) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    Constants.MAIN_FIND_TV_BY_ACTOR_LEFT + actorId + Constants.MAIN_FIND_TV_BY_ACTOR_RIGHT, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {

                        JSONArray moviesArray = response.getJSONArray("cast");

                        for (int i = 0; i < moviesArray.length(); i++) {

                            JSONObject movieObj = moviesArray.getJSONObject(i);

                            TvShow movie = new TvShow();
                            movie.setTitle(movieObj.getString("name"));
                            movie.setYear(movieObj.getString("first_air_date"));

                            movie.setOriginalLanguage(movieObj.getString("original_language"));
                            movie.setPlot(movieObj.getString("overview"));
                            movie.setPoster("http://image.tmdb.org/t/p/w185" + movieObj.getString("poster_path"));
                            movie.setMovieId(movieObj.getString("id"));

                            movieList.add(movie);


                        }

                        movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar.make(getView(), R.string.No_tv_shows_found, Snackbar.LENGTH_LONG).show();
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
}
