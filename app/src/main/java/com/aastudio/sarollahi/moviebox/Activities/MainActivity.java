package com.aastudio.sarollahi.moviebox.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Fragments.FragmentNow;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentPopular;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentTop;
import com.aastudio.sarollahi.moviebox.Fragments.FragmentUp;
import com.aastudio.sarollahi.moviebox.Data.ViewPagerAdapter;
import com.aastudio.sarollahi.moviebox.Model.Genre;
import com.android.sarollahi.moviebox.R;
import com.aastudio.sarollahi.moviebox.Util.Constants;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.layout.simple_spinner_item;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private DrawerLayout drawer;
    private ArrayList<Genre> movieGenreArrayList;
    private ArrayList<Genre> tvGenreArrayList;
    private ArrayList<String> mnames = new ArrayList<String>();
    private ArrayList<String> tnames = new ArrayList<String>();
    private HashMap<String ,String> moviemap = new HashMap<String,String>();
    private HashMap<String ,String> tvmap = new HashMap<String,String>();
    private Spinner spinnerMovie;
    private Spinner spinnerTv;
    private EditText actorname;
    private Button actorbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new FragmentUp(),"UPCOMING");
        adapter.AddFragment(new FragmentNow(),"NOW PLAYING");
        adapter.AddFragment(new FragmentPopular(),"POPULAR");
        adapter.AddFragment(new FragmentTop(),"TOP RATED");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView2.setNavigationItemSelectedListener(this);

        mnames.add("Select an Item...");
        tnames.add("Select an Item...");

        //spinner = findViewById(R.id.spGenre);
        spinnerMovie = (Spinner) navigationView2.getMenu().findItem(R.id.navigation_drawer_item3).getActionView();
        spinnerTv = (Spinner) navigationView2.getMenu().findItem(R.id.navigation_drawer_item4).getActionView();
        actorname = (EditText) navigationView2.getMenu().findItem(R.id.navigation_drawer_item5).getActionView();
        actorbtn = (Button) navigationView2.getMenu().findItem(R.id.navigation_drawer_item6).getActionView();

        actorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actorname.getText().toString().trim() != ""){
                    Intent intent = new Intent(MainActivity.this,SearchByActorActivity.class);
                    intent.putExtra("actname",actorname.getText().toString());
                    startActivity(intent);
                }
            }
        });



        getMovieGenres();
        getTvGenres();

        spinnerMovie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.GRAY);

                String genre_Name = parentView.getItemAtPosition(position).toString();
                if (genre_Name != "Select an Item..."){
                    String genre_Key = moviemap.get(genre_Name);
                    Intent intent = new Intent(MainActivity.this,BestGenreActivity.class);
                    intent.putExtra("id",genre_Key);
                    intent.putExtra("type","movie");
                    intent.putExtra("key",genre_Name);
                    startActivity(intent);
                    spinnerMovie.setSelection(0);
                    drawer.closeDrawer(GravityCompat.END);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.GRAY);

                String genre_Name = parentView.getItemAtPosition(position).toString();
                if (genre_Name != "Select an Item...") {
                    String genre_Keys = tvmap.get(genre_Name);
                    Intent intent = new Intent(MainActivity.this, BestGenreActivity.class);
                    intent.putExtra("id", genre_Keys);
                    intent.putExtra("type","tv");
                    intent.putExtra("key",genre_Name);
                    startActivity(intent);
                    spinnerTv.setSelection(0);
                    drawer.closeDrawer(GravityCompat.END);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            showInputDialog();
            // return true;
        }else if (id == R.id.drawer_right){
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movies) {

            startActivity(new Intent(MainActivity.this, SeenMoviesActivity.class));

        } else if (id == R.id.nav_tv_shows) {

            startActivity(new Intent(MainActivity.this, SeenTvActivity.class));

        } else if (id == R.id.nav_wish) {

            startActivity(new Intent(MainActivity.this, FavoriteActivity.class));

        } else if (id == R.id.nav_about) {

            alertDialogBuilder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.about_view, null);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            ImageButton playicon = (ImageButton) dialog .findViewById(R.id.imgPlayStore);
            TextView playtext = (TextView) dialog .findViewById(R.id.txtInfoRate);

            playicon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

            playtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

        } else if (id == R.id.nav_contact) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("https://github.com/sarollahi/MovieBox"));
                    startActivity(intent);

        } else if (id == R.id.nav_br) {
            startActivity(new Intent(MainActivity.this,BackupRestoreActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void showInputDialog() {

        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        final EditText newSearchEdt = (EditText) view.findViewById(R.id.searchEdt);
        final EditText newSearchyear = (EditText) view.findViewById(R.id.movieyear);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!newSearchEdt.getText().toString().isEmpty()) {

                    String nametemp = newSearchEdt.getText().toString();
                    String yeartemp = newSearchyear.getText().toString();
                    String search = nametemp.replaceAll(" ", "%20");
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("movieName",search);
                    intent.putExtra("movieYear",yeartemp);

                    startActivity(intent);
                }
                dialog.dismiss();


            }
        });
    }

    private void getMovieGenres() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.MOVIE_GENRE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);

                            moviemap = new HashMap<String,String>();
                            movieGenreArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("genres");

                            for (int i = 0; i < dataArray.length(); i++) {

                                Genre moviegenre = new Genre();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                moviegenre.setName(dataobj.getString("name"));
                                moviegenre.setId(dataobj.getString("id"));

                                moviemap.put(dataobj.getString("name") ,dataobj.getString("id"));

                                movieGenreArrayList.add(moviegenre);

                            }

                            for (int i = 0; i < movieGenreArrayList.size(); i++){
                                mnames.add(movieGenreArrayList.get(i).getName().toString());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), simple_spinner_item, mnames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            spinnerMovie.setAdapter(spinnerArrayAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void getTvGenres() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.TV_GENRE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject obj = new JSONObject(response);

                            tvmap = new HashMap<String,String>();
                            tvGenreArrayList = new ArrayList<>();
                            JSONArray dataArray  = obj.getJSONArray("genres");

                            for (int j = 0; j < dataArray.length(); j++) {

                                Genre tvgenre = new Genre();
                                JSONObject dataobj = dataArray.getJSONObject(j);

                                tvgenre.setName(dataobj.getString("name"));
                                tvgenre.setId(dataobj.getString("id"));

                                tvmap.put(dataobj.getString("name") ,dataobj.getString("id"));

                                tvGenreArrayList.add(tvgenre);

                            }

                            for (int j = 0; j < tvGenreArrayList.size(); j++){
                                tnames.add(tvGenreArrayList.get(j).getName().toString());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), simple_spinner_item, tnames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                            spinnerTv.setAdapter(spinnerArrayAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs

                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

}
