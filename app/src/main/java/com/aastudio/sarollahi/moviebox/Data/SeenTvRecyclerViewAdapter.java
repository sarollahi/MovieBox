package com.aastudio.sarollahi.moviebox.Data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Activities.SeenTvActivity;
import com.aastudio.sarollahi.moviebox.Activities.TvDetailActivity;
import com.aastudio.sarollahi.moviebox.Model.TvShow;
import com.android.sarollahi.moviebox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeenTvRecyclerViewAdapter extends RecyclerView.Adapter<SeenTvRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<TvShow> tvShowList;
    private String myear;

    public SeenTvRecyclerViewAdapter(Context context, List<TvShow> tvShows) {
        this.context = context;
        tvShowList = tvShows;
    }

    @Override
    public SeenTvRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylist_row, parent, false);



        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(SeenTvRecyclerViewAdapter.ViewHolder holder, int position) {

        final TvShow tvShow = tvShowList.get(position);
        String posterLink =  tvShow.getPoster();

        holder.title.setText(tvShow.getTitle());
        String release_date = tvShow.getYear();
        if (release_date.equals("")){
            myear = "--";
        }else {
            myear = release_date.substring(0,4);
        }

        holder.year.setText(myear + " | " + tvShow.getOriginalLanguage().toUpperCase());
        holder.overview.setText(tvShow.getPlot());
        holder.addedOn.setText("Added on: " + tvShow.getDateItemAdded());

        Picasso.get()
                .load(posterLink)
                .placeholder(R.drawable.noimage)
                .into(holder.poster);


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TvDetailActivity.class);
                intent.putExtra("movie",tvShow);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final DbHelper db = new DbHelper(context);
                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle("Delete tvshow");
                dialog.setMessage("are you sure?");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        db.deleteMovie(Integer.parseInt(tvShow.getMovieId()));
                        Intent intent = new Intent(context, SeenTvActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        dialog.dismiss();
                    }
                });
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return tvShowList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView year;
        TextView overview;
        TextView addedOn;
        ImageView poster;
        CardView cardView ;


        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.movieTitleID);
            year = (TextView) itemView.findViewById(R.id.movieReleaseID);
            overview = (TextView) itemView.findViewById(R.id.movieOverview);
            addedOn = (TextView) itemView.findViewById(R.id.movieaddedID);
            poster = (ImageView) itemView.findViewById(R.id.movieImageID);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
