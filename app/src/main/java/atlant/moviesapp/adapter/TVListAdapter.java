package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.helper.Date;
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


    public OnLoadMoreListener loadMoreListener;
    public boolean isLoading = false,
            isMoreDataAvailable = true;


    public static class TvViewHolder extends RecyclerView.ViewHolder {
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


        public TvViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public TVListAdapter(List<TvShow> series, Context context) {
        this.series = series;
        this.context = context;
        date=new Date(context);
    }

    @Override
    public TVListAdapter.TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new TVListAdapter.TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVListAdapter.TvViewHolder holder, final int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
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

    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private TVListAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final TVListAdapter.ClickListener clickListener) {
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

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


}