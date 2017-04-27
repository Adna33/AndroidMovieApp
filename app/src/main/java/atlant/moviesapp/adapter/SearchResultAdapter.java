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
import atlant.moviesapp.model.SearchResult;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 21.04.2017..
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder> {
    private List<SearchResult> results;
    private int rowLayout;
    private Context mcontext;

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_item)
        RelativeLayout itemLayout;

        @BindView(R.id.result_title)
        TextView resultTitle;

        @BindView(R.id.result_rating)
        TextView resultRating;

        @BindView(R.id.searchPoster)
        ImageView imageView;

        public ResultViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public SearchResultAdapter(List<SearchResult> results, int rowLayout, Context context) {
        this.results = results;
        this.rowLayout = rowLayout;
        this.mcontext = context;

    }

    @Override
    public SearchResultAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new SearchResultAdapter.ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.ResultViewHolder holder, final int position) {
        if (results.get(position).getMediaType().equals("tv")) {
            holder.resultTitle.setText(results.get(position).getName());
            holder.resultRating.setText(results.get(position).getRatingString());

            if (results.get(position).getImagePath() != null) {
                Glide.with(mcontext).load(results.get(position).getImagePath())
                        .crossFade().centerCrop()
                        .into(holder.imageView);

            }

        } else if (results.get(position).getMediaType().equals("movie")) {
            holder.resultTitle.setText(results.get(position).getTitle());
            holder.resultRating.setText(results.get(position).getRatingString());
            if (results.get(position).getImagePath() != null)
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
        private SearchResultAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SearchResultAdapter.ClickListener clickListener) {
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
}
