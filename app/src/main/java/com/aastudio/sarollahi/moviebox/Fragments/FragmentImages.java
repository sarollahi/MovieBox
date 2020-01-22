package com.aastudio.sarollahi.moviebox.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Data.PersonImagesRecyclerViewAdapter;
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
import java.util.Objects;

public class FragmentImages extends Fragment {

    View v;

    private RecyclerView recyclerView;
    private PersonImagesRecyclerViewAdapter personRecyclerViewAdapter;
    private List<Actor> actorList;
    private RequestQueue queue;
    private String personId;

    public FragmentImages() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.person_image_fragment, container, false);

        int mNoOfColumns = Utility.calculateNoOfColumns(getActivity().getApplicationContext(), 85);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), mNoOfColumns));
        personRecyclerViewAdapter = new PersonImagesRecyclerViewAdapter(getActivity(), actorList);
        recyclerView.setAdapter(personRecyclerViewAdapter);
        personRecyclerViewAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        Bundle person = getActivity().getIntent().getExtras();

        assert person != null;
        personId = person.getString("personId");


        actorList = new ArrayList<>();

        getActor getActor = (getActor) new getActor().execute();


    }

    class getActor extends AsyncTask<String, Void, List<Actor>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            actorList.clear();
        }

        @Override
        protected List<Actor> doInBackground(String... name) {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                    , Constants.PERSON_INFO_URL_LEFT + personId + Constants.PERSON_INFO_URL_RIGHT_2, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {

                    try {
                        JSONObject images = response.getJSONObject("images");
                        JSONArray imagesArray = images.getJSONArray("profiles");

                        for (int i = 0; i < imagesArray.length(); i++) {

                            JSONObject actorObj = imagesArray.getJSONObject(i);

                            Actor actor = new Actor();
                            actor.setName(actorObj.getString("height") + "×" + actorObj.getString("width"));
                            actor.setPoster(actorObj.getString("file_path"));
                            actor.setActorId("");

                            actorList.add(actor);


                        }

                        personRecyclerViewAdapter.notifyDataSetChanged();


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

            return actorList;
        }
    }
}
