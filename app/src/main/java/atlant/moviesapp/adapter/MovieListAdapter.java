package atlant.moviesapp.adapter;

/**
 * Created by Korisnik on 07.04.2017..
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.helper.OnItemClick;

import android.view.View.OnClickListener;

import atlant.moviesapp.model.*;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;
    private Date date;
    private OnItemClick itemClick;

    public MovieListAdapter.OnLoadMoreListener loadMoreListener;
    public boolean isLoading = false,
            isMoreDataAvailable = true;

    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
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
            favorite.setClickable(true);
            favorite.setOnClickListener(this);

            watchlist.setClickable(true);
            watchlist.setOnClickListener(this);

            moviePoster.setClickable(true);
            moviePoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == favorite.getId()) {
                if (ApplicationState.isLoggedIn()) {
                    if (favorite.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.like_active_icon).getConstantState())) {

                        Glide.with(context).load(R.drawable.like)
                                .crossFade().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(favorite);
                    } else {

                        Glide.with(context).load(R.drawable.like_active_icon)
                                .crossFade().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(favorite);
                    }
                }
                itemClick.onFavoriteClicked(getAdapterPosition());
            }
            if (v.getId() == watchlist.getId()) {
                if (ApplicationState.isLoggedIn()) {
                    if (favorite.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.bookmark_active_icon).getConstantState())) {
                        Glide.with(context).load(R.drawable.bookmark_black_tool_symbol)
                                .crossFade().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(watchlist);
                    }
                    else{
                        Glide.with(context).load(R.drawable.bookmark_active_icon)
                                .crossFade().centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(watchlist);
                    }
                }
                itemClick.onWatchlistClicked(getAdapterPosition());
            }
            if (v.getId() == moviePoster.getId()) {
                itemClick.onposterClicked(getAdapterPosition());
            }


        }
    }

    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public MovieListAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
        date = new Date(context);
    }

    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        String year = movies.get(position).getReleaseDate();
        holder.movieTitle.setText(movies.get(position).getTitle() + " (" + year.substring(0, Math.min(year.length(), 4)) + ")");
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
        if (ApplicationState.isLoggedIn()) {
            if (ApplicationState.getUser().getFavouriteMovies().contains(movies.get(position).getId())) {
                Glide.with(context).load(R.drawable.like_active_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.favorite);
            } else {
                Glide.with(context).load(R.drawable.like)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.favorite);
            }
            if (ApplicationState.getUser().getWatchListMovies().contains(movies.get(position).getId())) {
                Glide.with(context).load(R.drawable.bookmark_active_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.watchlist);
            } else {
                Glide.with(context).load(R.drawable.bookmark_black_tool_symbol)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.watchlist);
            }
        } else {
            Glide.with(context).load(R.drawable.bookmark_black_tool_symbol)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.watchlist);
            Glide.with(context).load(R.drawable.like)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.favorite);
        }


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    /* notifyDataSetChanged is final method so we can't override it
         call adapter.notifyDataChanged(); after update the list
         */
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(MovieListAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


}