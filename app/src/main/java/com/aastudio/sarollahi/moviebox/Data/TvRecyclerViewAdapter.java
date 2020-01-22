package com.aastudio.sarollahi.moviebox.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Activities.TvDetailActivity;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.android.sarollahi.moviebox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvRecyclerViewAdapter extends RecyclerView.Adapter<TvRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<TvShow> tvShowList;
    private String myear;

    public TvRecyclerViewAdapter(Context context, List<TvShow> movies) {
        this.context = context;
        tvShowList = movies;
    }

    @Override
    public TvRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(TvRecyclerViewAdapter.ViewHolder holder, int position) {

        final TvShow movie = tvShowList.get(position);
        String posterLink = movie.getPoster();

        holder.title.setText(movie.getTitle());
        String release_date = movie.getYear();
        if (release_date.equals("")) {
            myear = "--";
        } else {
            myear = release_date.substring(0, 4);
        }
        holder.year.setText(myear + " | " + movie.getOriginalLanguage().toUpperCase());
        holder.overview.setText(movie.getPlot());

        Picasso.get()
                .load(posterLink)
                .placeholder(R.drawable.noimage)
                .into(holder.poster);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TvDetailActivity.class);
                intent.putExtra("movie", movie);

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView year;
        TextView overview;
        ImageView poster;
        CardView cardView;


        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.movieTitleID);
            year = itemView.findViewById(R.id.movieReleaseID);
            overview = itemView.findViewById(R.id.movieOverview);
            poster = itemView.findViewById(R.id.movieImageID);
            cardView = itemView.findViewById(R.id.cardview);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
