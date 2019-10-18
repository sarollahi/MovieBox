package com.aastudio.sarollahi.moviebox.Data;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.aastudio.sarollahi.moviebox.Model.Review;
import com.android.sarollahi.moviebox.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.List;

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Review> reviewList;

    public ReviewRecyclerViewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        reviewList = reviews;
    }

    @Override
    public ReviewRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);



        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ReviewRecyclerViewAdapter.ViewHolder holder, int position) {

        final Review review = reviewList.get(position);

        holder.name.setText(review.getName());
        holder.text.setText(review.getText());

        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        holder.cardView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView text;
        CardView cardView ;


        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            name = (TextView) itemView.findViewById(R.id.id_index_reviews_name);
            text = (JustifiedTextView) itemView.findViewById(R.id.id_index_reviews_text);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
