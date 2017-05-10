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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 25.04.2017..
 */

public class FilmographyAdapter  extends RecyclerView.Adapter<FilmographyAdapter.FilmographyViewHolder> {
    private List<Movie> movies;
    private int rowLayout;
    private Context mcontext;


    public static class FilmographyViewHolder extends RecyclerView.ViewHolder {



        @BindView(R.id.filmography_item)
        LinearLayout filmLayout;

        @BindView(R.id.film_image)
        ImageView image;

        @BindView(R.id.film_name)
        TextView name;

        @BindView(R.id.film_actor_role)
        TextView role;


        public FilmographyViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public FilmographyAdapter(List<Movie> movies, int rowLayout, Context mcontext) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.mcontext = mcontext;
    }

    @Override
    public FilmographyAdapter.FilmographyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new FilmographyAdapter.FilmographyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmographyAdapter.FilmographyViewHolder holder, final int position) {
        holder.name.setText(movies.get(position).getTitle());
        holder.role.setText("");
        Glide.with(mcontext).load(movies.get(position).getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.image);


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
        private FilmographyAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FilmographyAdapter.ClickListener clickListener) {
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
