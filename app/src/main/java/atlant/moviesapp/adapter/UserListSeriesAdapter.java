package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 07.05.2017..
 */

public class UserListSeriesAdapter extends RecyclerView.Adapter<UserListSeriesAdapter.ResultViewHolder> {
    private List<TvShow> results;
    private int rowLayout;
    private Context mcontext;


    public UserListSeriesAdapter.OnLoadMoreListener loadMoreListener;
    public boolean isLoading = false,
            isMoreDataAvailable = true;

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_item)
        LinearLayout itemLayout;

        @BindView(R.id.result_title)
        TextView resultTitle;

        @BindView(R.id.result_rating)
        TextView resultRating;

        @BindView(R.id.result_year)
        TextView resultYear;

        @BindView(R.id.searchPoster)
        ImageView imageView;

        public ResultViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public UserListSeriesAdapter(List<TvShow> results, int rowLayout, Context context) {
        this.results = results;
        this.rowLayout = rowLayout;
        this.mcontext = context;

    }

    @Override
    public UserListSeriesAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new UserListSeriesAdapter.ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListSeriesAdapter.ResultViewHolder holder, final int position) {
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        holder.resultTitle.setText(results.get(position).getName());
        holder.resultRating.setText(results.get(position).getRatingString());
        String year=results.get(position).getReleaseDate();
        holder.resultYear.setText("Tv series ("+year.substring(0, Math.min(year.length(), 4))+")");

        if (results.get(position).getImagePath() != null) {
            Glide.with(mcontext).load(results.get(position).getImagePath())
                    .crossFade().centerCrop()
                    .into(holder.imageView);

        }


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UserListSeriesAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UserListSeriesAdapter.ClickListener clickListener) {
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
    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(UserListSeriesAdapter.OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
