package com.aastudio.sarollahi.moviebox.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Activities.SearchMovieByActorIdActivity;
import com.aastudio.sarollahi.moviebox.Model.Actor;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.sarollahi.moviebox.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentBiography extends Fragment {

    View v;

    private RequestQueue queue;
    private TextView name;
    private TextView birth;
    private TextView birthplace;
    private ImageView imdb;
    private ImageView website;
    private ImageView facebook;
    private ImageView instagram;
    private JustifiedTextView bio;
    private ImageView poster;
    private Button searchbyact;
    private Context mContext;
    private ProgressBar mProgressBar;

    public FragmentBiography() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.person_biography_fragment,container,false);

        name = v.findViewById(R.id.actorname);
        birth = v.findViewById(R.id.actorbirth);
        birthplace = v.findViewById(R.id.actorbirthplace);
        bio = v.findViewById(R.id.actorbio);
        poster = v.findViewById(R.id.actorimage);
        imdb = v.findViewById(R.id.imdbactor);
        website = v.findViewById(R.id.internetactor);
        facebook = v.findViewById(R.id.facebookactor);
        instagram = v.findViewById(R.id.instagramactor);
        searchbyact = v.findViewById(R.id.searchMTbyactor);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
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

        Bundle person = getActivity().getIntent().getExtras();

        String personId = person.getString("personId");


        getPersonDetails(personId);
    }

    private void getPersonDetails(final String personId) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Constants.PERSON_INFO_URL_LEFT + personId + Constants.PERSON_INFO_URL_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {

                try {

                    String actname ="";

                    name.setText(response.getString("name"));
                    if (!response.getString("birthday").equals("null")){
                        birth.setText(response.getString("birthday"));
                    }else {
                        birth.setText("");
                    }
                    if (!response.getString("place_of_birth").equals("null")){
                        birthplace.setText(response.getString("place_of_birth"));
                    }else {
                        birthplace.setText("");
                    }

                    bio.setText(response.getString("biography"));
                    Picasso.get().load("https://image.tmdb.org/t/p/h632"+response.getString("profile_path")).placeholder(R.drawable.noimage).into(poster);
                    if (response.has("name")){
                        actname = response.getString("name");
                    }else {
                        actname = "";
                    }
                    final String finalActname = actname;
                    searchbyact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Actor act = new Actor();
                            act.setActorId(personId);
                            act.setName(finalActname);
                            Intent intent = new Intent(getActivity(),SearchMovieByActorIdActivity.class);
                            intent.putExtra("actor",act);
                            startActivity(intent);
                        }
                    });
                    if (!response.isNull("homepage")){

                        final String finalWeb = response.getString("homepage");
                        website.setVisibility(View.VISIBLE);
                        website.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                intent.setData(Uri.parse(finalWeb));
                                startActivity(intent);
                            }
                        });

                    }
                    try {
                        JSONObject exlink = response.getJSONObject("external_ids");



                        if (!exlink.isNull("imdb_id")){
                            imdb.setVisibility(View.VISIBLE);
                            final String im = exlink.getString("imdb_id");
                            imdb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse("https://www.imdb.com/name/"+ im+"/"));
                                    startActivity(intent);
                                }
                            });

                        }
                        if (!exlink.isNull("facebook_id")){

                            final String face = exlink.getString("facebook_id");
                            facebook.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse("https://www.facebook.com/"+ face+"/"));
                                    startActivity(intent);
                                }
                            });

                        }
                        if (!exlink.isNull("instagram_id")){

                            final String insta = exlink.getString("instagram_id");
                            instagram.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                                    intent.setData(Uri.parse("https://www.instagram.com/"+ insta+"/"));
                                    startActivity(intent);
                                }
                            });
                        }

                        try {
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        } catch (Exception e){

                        }
                        mProgressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
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
    }
}
