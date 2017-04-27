package atlant.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;


import atlant.moviesapp.R;
import atlant.moviesapp.model.MovieGenre;
import atlant.moviesapp.model.News;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Korisnik on 14.04.2017..
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsViewHolder> {
    private List<News> news;
    private int rowLayout;
    private Context mcontext;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_item)
        LinearLayout newsLayout;

        @BindView(R.id.news_title)
        TextView newsTitle;

        @BindView(R.id.news_overview)
        TextView overview;

        @BindView(R.id.source_link)
        TextView sourceLink;

        public NewsViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }
    }

    public NewsFeedAdapter(List<News> news, int rowLayout, Context context) {
        this.news = news;
        this.rowLayout = rowLayout;
        this.mcontext = context;
    }

    @Override
    public NewsFeedAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new NewsFeedAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        if (news.get(position).getTitle() != null)
            holder.newsTitle.setText(news.get(position).getTitle());
        if (news.get(position).getDescription() != null)
            holder.overview.setText(news.get(position).getDescription());
        else holder.overview.setText("No description");
        holder.sourceLink.setText(news.get(position).getLink());


    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
