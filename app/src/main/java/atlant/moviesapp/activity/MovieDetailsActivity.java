package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.ReviewAdapter;
import atlant.moviesapp.fragments.YouTubeFragment;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.helper.StringUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MovieGenre;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.presenters.MovieDetailsPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.MovieDetailsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity implements MovieDetailsView, ShareActionProvider.OnShareTargetSelectedListener {

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
    ShareActionProvider mShareActionProvider;

    @OnClick(R.id.rate_txtBtn)
    void rate() {
        Intent i = new Intent(this, RatingActivity.class);
        i.putExtra(getString(R.string.title), getString(R.string.rateThisMovie));
        i.putExtra(getString(R.string.id), movie.getId());
        i.putExtra(getString(R.string.tag), TAG);
        startActivity(i);

    }

    @OnClick(R.id.heart_detail)
    void updateFavorites() {
        if (ApplicationState.getUser().getFavouriteMovies().contains(movie.getId())) {
            ApplicationState.getUser().removeFavouriteMovie(movie.getId());
            if (isNetworkAvailable()) {
                presenter.postFavorite(movie.getId(), ApplicationState.getUser().getSessionId(), false);
                Toast.makeText(this, R.string.removedFavorite, Toast.LENGTH_SHORT).show();


            } else {
                presenter.removeFavoriteRealm(movie.getId());
            }
            Glide.with(this).load(R.drawable.like)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(favourite);

        } else {
            ApplicationState.getUser().addFavouriteMovie(movie.getId());
            if (isNetworkAvailable()) {
                presenter.postFavorite(movie.getId(), ApplicationState.getUser().getSessionId(), true);

            } else {
                presenter.postFavoriteRealm(movie.getId());
            }
            Toast.makeText(this, R.string.addedFavorite, Toast.LENGTH_SHORT).show();
            Glide.with(this).load(R.drawable.like_active_icon)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(favourite);

        }

    }


    @OnClick(R.id.bookmark_detail)
    void updateWatchlist() {

        if (ApplicationState.getUser().getWatchListMovies().contains(movie.getId())) {
            ApplicationState.getUser().removeWatchlistMovie(movie.getId());
            if (isNetworkAvailable()) {
                presenter.postWatchlist(movie.getId(), ApplicationState.getUser().getSessionId(), false);
            } else {
                presenter.removeWatchlistRealm(movie.getId());
            }
            Glide.with(this).load(R.drawable.bookmark_black_tool_symbol)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(watchlist);
        } else {
            ApplicationState.getUser().addWatchlistMovie(movie.getId());
            if (isNetworkAvailable()) {
                presenter.postWatchlist(movie.getId(), ApplicationState.getUser().getSessionId(), true);
            } else {
                presenter.postWatchlistRealm(movie.getId());
            }
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
        boolean isConnected = isNetworkAvailable();
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
        director.setText(getString(R.string.unknown_field));
        String year = movie.getReleaseDate();
        title.setText(StringUtils.getTitle(movie.getTitle(), year));
        if (movie.getGenreIds().isEmpty())
            genre.setText(R.string.genre_unknown);
        else if (MovieGenre.getGenreById(movie.getGenreIds().get(0)) == null)
            genre.setText(R.string.genre_unknown);
        else
            genre.setText(MovieGenre.getGenreById(movie.getGenreIds().get(0)).getName());
        releaseDate.setText(date.getFormatedDate(movie.getReleaseDate()));
        rating.setText(movie.getRatingString());
        overview.setText(movie.getOverview());


        if (movie.isVideo() && isConnected) {
            playButton.setVisibility(View.VISIBLE);
            playButton.bringToFront();
        } else {
            playButton.setVisibility(View.INVISIBLE);
        }

        showPoster(movie);
        presenter = new MovieDetailsPresenter(this);

        if (isConnected) {
            if (RealmUtil.getInstance().getMovieDetailsFromRealm(movie.getId()) == null)
                RealmUtil.getInstance().createRealmMovieObject(movie.getId());
            presenter.getCredits(movie.getId());
            presenter.getReviews(movie.getId());
        } else {
            presenter.setUpMovie(movie.getId());

        }
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
        String castString = StringUtils.showCast(cast);
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showCrew(List<Crew> crew) {
        String directorsString = StringUtils.getDirectorString(crew,this);
        director.setText(directorsString);
        String writersString = StringUtils.getWritersString(crew,this);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuItem item = menu.add(Menu.NONE, R.id.share, Menu.NONE, R.string.share);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mShareActionProvider = new ShareActionProvider(this) {
            @Override
            public View onCreateActionView() {
                return null;
            }
        };

        item.setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha);
        mShareActionProvider.setOnShareTargetSelectedListener(this);
        setShareIntent(createShareIntent());
        MenuItemCompat.setActionProvider(item, mShareActionProvider);

        return(super.onCreateOptionsMenu(menu));

    }
    @Override
    public boolean onShareTargetSelected(ShareActionProvider source,
                                         Intent intent) {
        final String appName = intent.getComponent().getPackageName();
        if(appName.equals("com.facebook.katana")){setupFacebookShareIntent(); return true;}

        return false;
    }
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {

            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(movie.getImagePath()));
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
                movie.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                movie.getOverview());
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        return shareIntent;
    }

    public void setupFacebookShareIntent() {
      ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(movie.getOverview())
                .setContentUrl(Uri.parse(movie.getImagePath()))
                .build();
        shareDialog.show(linkContent);
    }
}
