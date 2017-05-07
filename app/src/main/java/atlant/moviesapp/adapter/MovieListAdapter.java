package atlant.moviesapp.adapter;

/**
 * Created by Korisnik on 07.04.2017..
 */

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.model.*;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    private Date date;

    public MovieListAdapter.OnLoadMoreListener loadMoreListener;
    public boolean isLoading = false,
            isMoreDataAvailable = true;

    public static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movies_layout)
        RelativeLayout moviesLayout;

        @BindView(R.id.title)
        TextView movieTitle;

        @BindView(R.id.release_date)
        TextView releaseDate;

        @BindView(R.id.poster_iv)
        ImageView moviePoster;

        @BindView(R.id.movieGenre)
        TextView genre;


        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.like_btn)
        ImageButton favorite;

        @BindView(R.id.bookmark_btn)
        ImageButton watchlist;


        public MovieViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void onClick(View v) {

            //Proba
            if (v.getId() == favorite.getId()){
                Toast.makeText(v.getContext(), "FAVOURITE ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == watchlist.getId()){
                Toast.makeText(v.getContext(), "WATCHLIST ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }


        }
    }

    public MovieListAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
        date=new Date(context);
    }

    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        String year=movies.get(position).getReleaseDate();
        holder.movieTitle.setText(movies.get(position).getTitle()+" ("+year.substring(0, Math.min(year.length(), 4))+")");
        holder.releaseDate.setText(date.getFormatedDate(movies.get(position).getReleaseDate()));
        holder.rating.setText(movies.get(position).getRatingString());
        if (movies.get(position).getGenreIds().isEmpty() || MovieGenre.getGenreById(movies.get(position).getGenreIds().get(0)) == null)
            holder.genre.setText(R.string.genre_unknown);
        else
            holder.genre.setText(MovieGenre.getGenreById(movies.get(position).getGenreIds().get(0)).getName());
        Glide.with(context).load(movies.get(position).getImagePath())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MovieListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MovieListAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(MovieListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}