package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MovieGenre;
import atlant.moviesapp.model.TvGenre;
import atlant.moviesapp.model.TvShow;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 14.04.2017..
 */

public class TVListAdapter extends RecyclerView.Adapter<TVListAdapter.TvViewHolder> {

    private List<TvShow> series;
    private Context context;
    private Date date;
    private OnItemClick itemClick;


    public OnLoadMoreListener loadMoreListener;
    public boolean isLoading = false,
            isMoreDataAvailable = true;


    public class TvViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movies_layout)
        RelativeLayout moviesLayout;

        @BindView(R.id.title)
        TextView seriesName;

        @BindView(R.id.release_date)
        TextView releaseDate;

        @BindView(R.id.poster_iv)
        ImageView seriesPoster;

        @BindView(R.id.movieGenre)
        TextView genre;


        @BindView(R.id.rating)
        TextView rating;

        @BindView(R.id.like_btn)
        ImageButton favorite;

        @BindView(R.id.bookmark_btn)
        ImageButton watchlist;


        public TvViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            favorite.setClickable(true);
            favorite.setOnClickListener(this);

            watchlist.setClickable(true);
            watchlist.setOnClickListener(this);

            seriesPoster.setClickable(true);
            seriesPoster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == favorite.getId()) {
                itemClick.onfavouriteClicked(getAdapterPosition());
            }
            if (v.getId() == watchlist.getId()) {
                itemClick.onwatchlistClicked(getAdapterPosition());
            }
            if (v.getId() == seriesPoster.getId()) {
                itemClick.onposterClicked(getAdapterPosition());
            }


        }
    }

    public TVListAdapter(List<TvShow> series, Context context) {
        this.series = series;
        this.context = context;
        date = new Date(context);
    }

    @Override
    public TVListAdapter.TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TVListAdapter.TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVListAdapter.TvViewHolder holder, final int position) {

        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        holder.seriesName.setText(series.get(position).getName());
        holder.releaseDate.setText(date.getFormatedDate(series.get(position).getReleaseDate()));
        holder.rating.setText(series.get(position).getRatingString());
        if (series.get(position).getGenreIds().isEmpty()) {
            holder.genre.setText(R.string.genre_unknown);
        } else if (TvGenre.getTvGenreById(series.get(position).getGenreIds().get(0)) == null)
            holder.genre.setText(R.string.genre_unknown);
        else
            holder.genre.setText(TvGenre.getTvGenreById(series.get(position).getGenreIds().get(0)).getName());
        Glide.with(context).load(series.get(position).getImagePath())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.seriesPoster);
        if (ApplicationState.isLoggedIn()) {
            if (ApplicationState.getUser().getFavouriteMovies().contains(series.get(position).getId())) {
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
            if (ApplicationState.getUser().getWatchListMovies().contains(series.get(position).getId())) {
                Glide.with(context).load(R.drawable.bookmark_active_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.watchlist);
            } else {
                Glide.with(context).load(R.drawable.not_bookmarked_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.watchlist);
            }
        } else {
            Glide.with(context).load(R.drawable.not_bookmarked_icon)
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
        return series.size();
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

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }


}