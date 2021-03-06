package atlant.moviesapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.NewsFeedAdapter;
import atlant.moviesapp.adapter.ReviewAdapter;
import atlant.moviesapp.fragments.YouTubeFragment;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MovieGenre;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.TvGenre;
import atlant.moviesapp.presenters.MovieDetailsPresenter;
import atlant.moviesapp.views.MovieDetailsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsView {

    @BindView(R.id.movie_title)
    TextView title;

    @BindView(R.id.movie_genre)
    TextView genre;

    @BindView(R.id.movie_release_date)
    TextView releaseDate;

    @BindView(R.id.movie_rating)
    TextView rating;

    @BindView(R.id.movie_overview)
    TextView overview;

    @BindView(R.id.movie_director)
    TextView director;

    @BindView(R.id.movie_writors)
    TextView writers;

    @BindView(R.id.movie_stars)
    TextView stars;

    @BindView(R.id.movie_poster)
    ImageView poster;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.reviews_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;

    @BindView(R.id.play_button)
    ImageView playButton;

    @BindView(R.id.heart_detail)
    ImageView favourite;

    @BindView(R.id.bookmark_detail)
    ImageView watchlist;

    @OnClick(R.id.play_button)
    public void showTrailer() {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.id), movie.getId());
        YouTubeFragment f = new YouTubeFragment();
        f.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.main, f, f.getTag());
        ft.commit();
    }

    @BindView(R.id.rate_txtBtn)
    TextView rateTxt;
    private static final int TAG = 0;
    private BodyFavourite bodyFavourite;
    private BodyWatchlist bodyWatchlist;

    @OnClick(R.id.rate_txtBtn)
    void rate() {
        Intent i = new Intent(this, RatingActivity.class);
        i.putExtra(getString(R.string.title), getString(R.string.rateThisMovie));
        i.putExtra(getString(R.string.id), movie.getId());
        i.putExtra(getString(R.string.tag), TAG);
        startActivity(i);

    }

    @OnClick(R.id.heart_detail)
    void addToFavorites() {
        if (ApplicationState.getUser().getFavouriteMovies().contains(movie.getId())) {
            ApplicationState.getUser().removeFavouriteMovie(movie.getId());
            bodyFavourite = new BodyFavourite(getString(R.string.movie), movie.getId(), false);
            presenter.postFavorite(movie.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
            Toast.makeText(this, R.string.removedFavorite, Toast.LENGTH_SHORT).show();
            Glide.with(this).load(R.drawable.like)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(favourite);

        } else {
            ApplicationState.getUser().addFavouriteMovie(movie.getId());
            bodyFavourite = new BodyFavourite(getString(R.string.movie), movie.getId(), true);
            presenter.postFavorite(movie.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
            Toast.makeText(this, R.string.addedFavorite, Toast.LENGTH_SHORT).show();
            Glide.with(this).load(R.drawable.like_active_icon)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(favourite);

        }

    }


    @OnClick(R.id.bookmark_detail)
    void addToWatchlist() {

        if (ApplicationState.getUser().getWatchListMovies().contains(movie.getId())) {
            ApplicationState.getUser().removeWatchlistMovie(movie.getId());
            bodyWatchlist= new BodyWatchlist(getString(R.string.movie), movie.getId(), false);
            presenter.postWatchlist(movie.getId(), ApplicationState.getUser().getSessionId(), bodyWatchlist);
            Glide.with(this).load(R.drawable.bookmark_black_tool_symbol)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(watchlist);
        } else {
            ApplicationState.getUser().addWatchlistMovie(movie.getId());
            bodyWatchlist= new BodyWatchlist(getString(R.string.movie), movie.getId(), true);
            presenter.postWatchlist(movie.getId(), ApplicationState.getUser().getSessionId(), bodyWatchlist);
            Glide.with(this).load(R.drawable.bookmark_active_icon)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(watchlist);
        }
    }

    @BindView(R.id.divider)
    TextView divider;

    private MovieDetailsPresenter presenter;
    private Movie movie;
    private Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        checkLogin();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.movies_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        date = new Date(this);

        Intent intent = getIntent();
        movie = intent.getParcelableExtra(getString(R.string.movieIntent));
        director.setText("");
        String year = movie.getReleaseDate();
        title.setText(movie.getTitle() + " (" + year.substring(0, Math.min(year.length(), 4)) + ")");
        if (movie.getGenreIds().isEmpty())
            genre.setText(R.string.genre_unknown);
        else if (MovieGenre.getGenreById(movie.getGenreIds().get(0)) == null)
            genre.setText(R.string.genre_unknown);
        else
            genre.setText(MovieGenre.getGenreById(movie.getGenreIds().get(0)).getName());
        releaseDate.setText(date.getFormatedDate(movie.getReleaseDate()));
        rating.setText(movie.getRatingString());
        overview.setText(movie.getOverview());

        if (movie.isVideo()) {
            playButton.setVisibility(View.VISIBLE);
            playButton.bringToFront();
        } else {
        }

        showPoster(movie);
        presenter = new MovieDetailsPresenter(this);
        presenter.getCredits(movie.getId());
        presenter.getReviews(movie.getId());
        if (ApplicationState.isLoggedIn()) {
            watchlist.setVisibility(View.VISIBLE);
            favourite.setVisibility(View.VISIBLE);
            if (ApplicationState.getUser().getFavouriteMovies().contains(movie.getId())) {
                Glide.with(this).load(R.drawable.like_active_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(favourite);
            } else {
                Glide.with(this).load(R.drawable.like)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(favourite);
            }
            if (ApplicationState.getUser().getWatchListMovies().contains(movie.getId())) {
                Glide.with(this).load(R.drawable.bookmark_active_icon)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(watchlist);
            } else {
                Glide.with(this).load(R.drawable.bookmark_black_tool_symbol)
                        .crossFade().centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(watchlist);
            }

        } else {
            watchlist.setVisibility(View.INVISIBLE);
            favourite.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void showCast(final List<Cast> cast) {
        String castString = "";
        for (int i = 0; i < cast.size(); i++) {
            if (i < 4) {
                castString = castString + cast.get(i).getName() + " ";
            }

        }
        stars.setText(castString);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(new ActorAdapter(cast, R.layout.actor_item, this));
        castRecyclerView.addOnItemTouchListener(new ActorAdapter.RecyclerTouchListener(this, castRecyclerView, new ActorAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(MovieDetailsActivity.this, ActorActivity.class);
                intent.putExtra(getString(R.string.actorId), cast.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    public void showCrew(List<Crew> crew) {
        String directorsString = "";
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getJob().equals(getString(R.string.director))) {
                directorsString = directorsString + crew.get(i).getName() + "  ";
            }

        }
        director.setText(directorsString);
        String writersString = "";
        int num = 0;
        for (int i = 0; i < crew.size(); i++) {
            if (crew.get(i).getDepartment().equals(getString(R.string.writing)) && num < 3) {
                num++;
                writersString = writersString + crew.get(i).getName() + " (" + crew.get(i).getJob() + ")  ";
            }

        }
        writers.setText(writersString);

    }

    @Override
    public void showPoster(Movie movie) {
        if (movie.getBackdropPath() == null) {
            Glide.with(this).load(movie.getImagePath())
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(poster);
            playButton.setVisibility(View.INVISIBLE);

        } else {
            Glide.with(this).load(movie.getBackdropImagePath())
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(poster);
            playButton.setVisibility(View.VISIBLE);
            playButton.bringToFront();
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ReviewAdapter(reviews, R.layout.review_item, this));

    }

    public void checkLogin() {
        if (ApplicationState.isLoggedIn()) {
            rateTxt.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        } else {
            rateTxt.setVisibility(View.INVISIBLE);
            divider.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.onStop();
    }

    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.onDestroy();
        super.onDestroy();
    }
}
