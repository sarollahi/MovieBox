package com.aastudio.sarollahi.moviebox.Activities;

import android.annotation.SuppressLint;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

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

public class SearchByActorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonRecyclerViewAdapter personRecyclerViewAdapter;
    private List<Actor> actorList;
    private RequestQueue queue;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_actor);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBaract);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);

        int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext(),85);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

        Bundle person = getIntent().getExtras();
        String personname = person.getString("actname");
        String search = personname.replaceAll(" ", "%20");

        actorList = new ArrayList<>();

        // getMovies(search);
        actorList = getActor(search);

        personRecyclerViewAdapter = new PersonRecyclerViewAdapter(this, actorList );
        recyclerView.setAdapter(personRecyclerViewAdapter);
        personRecyclerViewAdapter.notifyDataSetChanged();




    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    public List<Actor> getActor(String name) {
        actorList.clear();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.MAIN_FIND_ACROT_BY_NAME + name,null, new Response.Listener<JSONObject>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(JSONObject response) {

                try{
                    JSONArray actorsArray = response.getJSONArray("results");

                    for (int i = 0; i < actorsArray.length(); i++) {

                        JSONObject actorObj = actorsArray.getJSONObject(i);

                        Actor actor = new Actor();
                        actor.setName(actorObj.getString("name"));
                        actor.setPoster(actorObj.getString("profile_path"));
                        actor.setActorId(actorObj.getString("id"));

                        actorList.add(actor);


                    }

                    personRecyclerViewAdapter.notifyDataSetChanged();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    mProgressBar.setVisibility(View.GONE);


                }catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(R.layout.activity_search_by_actor),getString(R.string.Nothing_found),Snackbar.LENGTH_LONG).show();
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
