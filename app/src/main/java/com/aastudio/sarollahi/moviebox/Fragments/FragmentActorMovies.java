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

import com.aastudio.sarollahi.moviebox.Data.MovieRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Actor;
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

public class FragmentActorMovies extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private Actor actor;
    private String actorId;
    LinearLayoutManager manager;

    public FragmentActorMovies() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.movies_fragment,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewMovies);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(getContext(), movieList);
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

    private List<Movie> getMovies(String id) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.MAIN_FIND_MOVIE_BY_ACTOR_LEFT + id + Constants.MAIN_FIND_MOVIE_BY_ACTOR_RIGHT,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    JSONArray moviesArray = response.getJSONArray("cast");

                    for (int i = 0; i < moviesArray.length(); i++) {

                        JSONObject movieObj = moviesArray.getJSONObject(i);

                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        if (movieObj.has("release_date")){
                            movie.setYear(movieObj.getString("release_date"));
                        }else {
                            movie.setYear("");
                        }


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
