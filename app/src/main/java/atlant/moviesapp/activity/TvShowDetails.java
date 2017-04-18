package atlant.moviesapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.ReviewAdapter;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.TvGenre;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.TvDetailsPresenter;
import atlant.moviesapp.views.TvDetailsView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TvShowDetails extends AppCompatActivity implements TvDetailsView {
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_genre)
    TextView genre;

    @BindView(R.id.tv_release_date)
    TextView releaseDate;

    @BindView(R.id.tv_rating)
    TextView rating;

    @BindView(R.id.tv_overview)
    TextView overview;

    @BindView(R.id.tv_director)
    TextView director;

    @BindView(R.id.tv_writers)
    TextView writers;

    @BindView(R.id.tv_stars)
    TextView stars;

    @BindView(R.id.tv_poster)
    ImageView poster;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;

    private TvDetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tvshows_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        TvShow series = intent.getParcelableExtra("series");
        director.setText("");
        title.setText(series.getName());
        if(TvGenre.getTvGenreById(series.getGenreIds().get(0))== null || series.getGenreIds().isEmpty()){genre.setText(R.string.genre_unknown);}
        else
        genre.setText(TvGenre.getTvGenreById(series.getGenreIds().get(0)).getName());
        releaseDate.setText(series.getReleaseDate());
        rating.setText(series.getRatingString());
        overview.setText(series.getOverview());
        showPoster(series);
        presenter=new TvDetailsPresenter(this);
        presenter.getCredits(series.getId());
    }

    @Override
    public void showCast(List<Cast> cast) {
        String castString="";
        for (int i=0; i<cast.size(); i++) {
            if(i<4)
            {
                castString= castString+cast.get(i).getName()+" ";
            }

        }
        stars.setText(castString);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(new ActorAdapter(cast, R.layout.actor_item,this));

    }

    @Override
    public void showCrew(List<Crew> crew) {
        String directorsString="";
        for (int i=0; i<crew.size(); i++) {
            if(crew.get(i).getJob().equals("Director"))
            {
                directorsString= directorsString+crew.get(i).getName()+"  ";
            }

        }
        director.setText(directorsString);
        String writersString="";
        int num=0;
        for (int i=0; i<crew.size(); i++) {
            if(crew.get(i).getDepartment().equals("Writing") && num<3)
            {
                num++;
                writersString= writersString+crew.get(i).getName()+" ("+crew.get(i).getJob()+")  ";
            }

        }
        writers.setText(writersString);

    }

    @Override
    public void showPoster(TvShow series) {
        Glide.with(this).load(series.getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

    }

    @Override
    public void showReviews(List<Review> reviews) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReviewAdapter(reviews, R.layout.review_item,this));

    }
    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }
    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
