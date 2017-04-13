package atlant.moviesapp.adapter;

/**
 * Created by Korisnik on 07.04.2017..
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.model.*;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    private List<Movie> movies;
    private int rowLayout;
    private Context context;

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
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


        public MovieViewHolder(View v)
        {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context)
    {
        this.movies=movies;
        this.rowLayout=rowLayout;
        this.context=context;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(rowLayout,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder,final int position)
    {
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.releaseDate.setText(movies.get(position).getReleaseDate());
        holder.rating.setText(movies.get(position).getRatingString());
        if(movies.get(position).getGenreIds().isEmpty()){holder.genre.setText("Unknown Genre");}
        else
        holder.genre.setText(MovieGenre.getGenreById(movies.get(position).getGenreIds().get(0)).getName());
        Glide.with(context).load(movies.get(position).getImagePath())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.moviePoster);

    }
    @Override
    public int getItemCount()
    {return movies.size();}
}