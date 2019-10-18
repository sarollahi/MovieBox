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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FragmentActorTvShows extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private TvRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<TvShow> movieList;
    private RequestQueue queue;
    private Actor actor;
    private String actorId;
    LinearLayoutManager manager;

    public FragmentActorTvShows() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tvshows_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTvShows);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new TvRecyclerViewAdapter(getContext(), movieList );
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity());

        actor = (Actor) getActivity().getIntent().getSerializableExtra("actor");
        actorId = actor.getActorId();

        movieList = new ArrayList<>();

        // getMovies(search);
        movieList = getMovies(actorId);
    }

    private List<TvShow> getMovies(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.MAIN_FIND_TV_BY_ACTOR_LEFT + id + Constants.MAIN_FIND_TV_BY_ACTOR_RIGHT,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    JSONArray moviesArray = response.getJSONArray("cast");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        TvShow movie = new TvShow();
                        movie.setTitle(movieObj.getString("name"));
                        movie.setYear(movieObj.getString("first_air_date"));

                        movie.setOriginalLanguage(movieObj.getString("original_language"));
                        movie.setPlot(movieObj.getString("overview"));
                        movie.setPoster("http://image.tmdb.org/t/p/w185"+movieObj.getString("poster_path"));
                        movie.setMovieId(movieObj.getString("id"));

                        movieList.add(movie);


                    }

                    movieRecyclerViewAdapter.notifyDataSetChanged();//Important!!

                }catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(getView(), R.string.No_tv_shows_found,Snackbar.LENGTH_LONG).show();
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
