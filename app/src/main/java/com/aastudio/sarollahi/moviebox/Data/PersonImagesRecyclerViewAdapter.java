package com.aastudio.sarollahi.moviebox.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.aastudio.sarollahi.moviebox.Activities.BigImageActivity;
import com.aastudio.sarollahi.moviebox.Model.Actor;
import com.android.sarollahi.moviebox.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonImagesRecyclerViewAdapter extends RecyclerView.Adapter<PersonImagesRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Actor> actorList;

    public PersonImagesRecyclerViewAdapter(Context context, List<Actor> actors) {
        this.context = context;
        actorList = actors;
    }

    @Override
    public PersonImagesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_tile, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(PersonImagesRecyclerViewAdapter.ViewHolder holder, int position) {

        final Actor actor = actorList.get(position);
        String posterLink = "https://image.tmdb.org/t/p/w185" + actor.getPoster();

        holder.title.setText(actor.getName());

        Picasso.get()
                .load(posterLink)
                .placeholder(R.drawable.noimage)
                .into(holder.poster);

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        holder.cardView.startAnimation(animation);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BigImageActivity.class);
                intent.putExtra("actor", actor.getPoster());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView poster;
        CardView cardView;


        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.movieTitleID);
            poster = itemView.findViewById(R.id.movieImageID);
            cardView = itemView.findViewById(R.id.cardview);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
