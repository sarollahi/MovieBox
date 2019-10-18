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

import com.aastudio.sarollahi.moviebox.Data.DbHelper;
import com.aastudio.sarollahi.moviebox.Data.SeenTvRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.android.sarollahi.moviebox.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentFavoriteTvShows extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private SeenTvRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<TvShow> movieList;
    private DbHelper db;
    LinearLayoutManager manager;


    public FragmentFavoriteTvShows() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tvshows_fragment,container,false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTvShows);
        recyclerView.setHasFixedSize(true);
        movieRecyclerViewAdapter = new SeenTvRecyclerViewAdapter(getContext(), movieList );
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
        movieList = db.getAllfavtvshows();
    }
}
