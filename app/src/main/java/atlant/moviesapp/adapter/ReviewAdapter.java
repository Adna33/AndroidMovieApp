package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Review;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Korisnik on 16.04.2017..
 */

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>  {
    private List<Review> reviews;
    private int rowLayout;
    private Context mcontext;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        private boolean isClicked=true;
        @BindView(R.id.review_item_layout)
        LinearLayout reviewLayout;

        @BindView(R.id.username_review)
        TextView username;



        @BindView(R.id.overview_review)
        TextView overview;

        @BindView(R.id.read_more_review)
        TextView readMore;


        @OnClick(R.id.read_more_review)
        public void submit() {
            if(!isClicked){

                overview.setMaxLines(4);
                isClicked = true;
                readMore.setText("Read More");
            } else {

                overview.setMaxLines(Integer.MAX_VALUE);
                isClicked = false;
                readMore.setText("Hide");
            }
        }

        public ReviewViewHolder(View v)
        {
            super(v);

            ButterKnife.bind(this, v);



        }
    }

    public ReviewAdapter(List<Review> reviews, int rowLayout, Context mcontext) {
        this.reviews = reviews;
        this.rowLayout = rowLayout;
        this.mcontext = mcontext;
    }
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, final int position)
    {
        holder.username.setText(reviews.get(position).getAuthor());
        holder.overview.setText(reviews.get(position).getContent());


    }
    @Override
    public int getItemCount()
    {return reviews.size();}
}
