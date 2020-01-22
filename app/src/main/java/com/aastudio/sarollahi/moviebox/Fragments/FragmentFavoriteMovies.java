package com.aastudio.sarollahi.moviebox.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Data.SeenMoviesRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Movie;
import com.android.sarollahi.moviebox.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavoriteMovies extends Fragment {

    View v;
    LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private SeenMoviesRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private DbHelper db;

    public FragmentFavoriteMovies() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.movies_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerViewMovies);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new SeenMoviesRecyclerViewAdapter(getContext(), movieList);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DbHelper(getContext());

        movieList = new ArrayList<>();
        movieList = db.getAllfavmovies();
    }
}
