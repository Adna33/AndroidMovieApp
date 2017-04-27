package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.model.News;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 20.04.2017..
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {
private List<Episode> episodes;
private int rowLayout;
private Context mcontext;

public static class EpisodeViewHolder extends RecyclerView.ViewHolder
{
    @BindView(R.id.season_episode_item)
    LinearLayout episodeLayout;

    @BindView(R.id.episode_title)
    TextView title;

    @BindView(R.id.episode_rating)
    TextView rating;

    @BindView(R.id.episode_date)
    TextView date;

    public EpisodeViewHolder(View v)
    {
        super(v);
        ButterKnife.bind(this, v);

    }
}

    public EpisodeAdapter(List<Episode> episodes, int rowLayout, Context context) {
        this.episodes = episodes;
        this.rowLayout = rowLayout;
        this.mcontext = context;
    }
    @Override
    public EpisodeAdapter.EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new EpisodeAdapter.EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeAdapter.EpisodeViewHolder holder, final int position)
    {
        holder.title.setText((position+1) + ". "+episodes.get(position).getName());
        holder.rating.setText(episodes.get(position).getRatingString());
        holder.date.setText(episodes.get(position).getAirDate());


    }
    @Override
    public int getItemCount()
    {return episodes.size();}

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private EpisodeAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final EpisodeAdapter.ClickListener clickListener) {
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
