package com.aastudio.sarollahi.moviebox.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.aastudio.sarollahi.moviebox.Data.PersonImagesRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Data.PersonRecyclerViewAdapter;
import com.aastudio.sarollahi.moviebox.Model.Actor;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.aastudio.sarollahi.moviebox.Util.Utility;
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

public class FragmentImages extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private PersonImagesRecyclerViewAdapter personRecyclerViewAdapter;
    private List<Actor> actorList;
    private RequestQueue queue;
    private ProgressBar mProgressBar;

    public FragmentImages() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.person_image_fragment,container,false);

        int mNoOfColumns = Utility.calculateNoOfColumns(getActivity().getApplicationContext(),85);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBaract);
        personRecyclerViewAdapter = new PersonImagesRecyclerViewAdapter(getActivity(), actorList );
        recyclerView.setAdapter(personRecyclerViewAdapter);
        personRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity());

        Bundle person = getActivity().getIntent().getExtras();

        String personId = person.getString("personId");


        actorList = new ArrayList<>();

        // getMovies(search);
        actorList = getActor(personId);


    }

    public List<Actor> getActor(String name) {
        actorList.clear();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Constants.PERSON_INFO_URL_LEFT + name + Constants.PERSON_INFO_URL_RIGHT_2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                try{
                    JSONObject images = response.getJSONObject("images");
                    JSONArray imagesArray = images.getJSONArray("profiles");

                    for (int i = 0; i < imagesArray.length(); i++) {

                        JSONObject actorObj = imagesArray.getJSONObject(i);

                        Actor actor = new Actor();
                        actor.setName(actorObj.getString("height")+"×"+actorObj.getString("width"));
                        actor.setPoster(actorObj.getString("file_path"));
                        actor.setActorId("");

                        actorList.add(actor);


                    }

                    personRecyclerViewAdapter.notifyDataSetChanged();


                }catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return actorList;

    }
}
