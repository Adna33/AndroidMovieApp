package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.HorizontalAdapter;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.helper.StringUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.TvShowDetail;

import atlant.moviesapp.presenters.TvDetailsPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.TvDetailsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.heart_detail)
    ImageView favourite;

    @BindView(R.id.bookmark_detail)
    ImageView watchlist;

    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;

    @BindView(R.id.seasons_recycler_view)
    RecyclerView seasonRecyclerView;

    private TvDetailsPresenter presenter;

    private Integer seriesId;
    private Integer seasonNum;
    private Date date;
    private StringUtils stringUtils;

    private BodyFavourite bodyFavourite;
    private BodyWatchlist bodyWatchlist;

    @BindView(R.id.rate_txtBtn)
    TextView rateTxt;
    private static final int TAG = 1;

    @OnClick(R.id.rate_txtBtn)
    void rate() {
        Intent i = new Intent(this, RatingActivity.class);
        i.putExtra(getString(R.string.title), getString(R.string.ratethisTVshow));
        i.putExtra(getString(R.string.id), seriesId);
        i.putExtra(getString(R.string.tag), TAG);
        startActivity(i);

    }

    @BindView(R.id.divider)
    TextView divider;

    @OnClick(R.id.heart_detail)
    void updateFavorites() {
        if (ApplicationState.getUser().getFavouriteSeries().contains(seriesId)) {
            ApplicationState.getUser().removeFavoriteShow(seriesId);
            if (isNetworkAvailable()) {

                presenter.postFavorite(seriesId, ApplicationState.getUser().getSessionId(), false);
            } else {
                presenter.removeFavoriteRealm(seriesId);

            }
            Toast.makeText(this, R.string.removedFavorite, Toast.LENGTH_SHORT).show();
            Glide.with(this).load(R.drawable.like)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(favourite);

        } else {
            ApplicationState.getUser().addFavouriteShow(seriesId);
            if (isNetworkAvailable()) {
                presenter.postFavorite(seriesId, ApplicationState.getUser().getSessionId(), true);
            } else {
                presenter.postFavoriteRealm(seriesId);

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

        if (ApplicationState.getUser().getWatchListSeries().contains(seriesId)) {
            ApplicationState.getUser().removeWatchlistShow(seriesId);
            if (isNetworkAvailable()) {
                presenter.postWatchlist(seriesId, ApplicationState.getUser().getSessionId(), false);
            } else {
                presenter.removeWatchlistRealm(seriesId);
            }
            Glide.with(this).load(R.drawable.bookmark_black_tool_symbol)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(watchlist);
        } else {
            ApplicationState.getUser().addWatchlistShow(seriesId);
            if (isNetworkAvailable()) {
                presenter.postWatchlist(seriesId, ApplicationState.getUser().getSessionId(), true);
            } else {
                presenter.postWatchlistRealm(seriesId);
            }

            Glide.with(this).load(R.drawable.bookmark_active_icon)
                    .crossFade().centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(watchlist);
        }
    }

    @OnClick(R.id.see_all_seasons)
    public void seeAll() {
        Intent intent = new Intent(TvShowDetails.this, SeasonsActivity.class);
        intent.putExtra(getString(R.string.show_id_intent), seriesId);
        intent.putExtra(getString(R.string.season_id_intent), 1);
        intent.putExtra(getString(R.string.season_num_intent), seasonNum);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);
        ButterKnife.bind(this);

        checkLogin();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tvshows_title);
        boolean isConnected = isNetworkAvailable();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        stringUtils = new StringUtils(this);
        date = new Date(this);
        Intent intent = getIntent();
        seriesId = intent.getIntExtra(getString(R.string.series), 0);
        presenter = new TvDetailsPresenter(this);
        if (isConnected) {
            if (RealmUtil.getInstance().getTvShowDetailFromRealm(seriesId) == null || RealmUtil.getInstance().getShowDetailsFromRealm(seriesId) == null)
                RealmUtil.getInstance().createRealmSeriesObject(seriesId);
            presenter.getDetails(seriesId);
            presenter.getCredits(seriesId);
        } else {
            presenter.setUpTvShow(seriesId);
        }

        if (ApplicationState.isLoggedIn()) {
            watchlist.setVisibility(View.VISIBLE);
            favourite.setVisibility(View.VISIBLE);
            if (ApplicationState.getUser().getFavouriteSeries().contains(seriesId)) {
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
            if (ApplicationState.getUser().getWatchListSeries().contains(seriesId)) {
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void showCast(final List<Cast> cast) {
        String castString = stringUtils.emptyString();
        ;
        for (int i = 0; i < cast.size(); i++) {
            if (i < 4) {
                castString = stringUtils.getCast(castString, cast.get(i).getName());
            }

        }
        stars.setText(castString);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(new ActorAdapter(cast, R.layout.actor_item, this));
        castRecyclerView.addOnItemTouchListener(new ActorAdapter.RecyclerTouchListener(this, castRecyclerView, new ActorAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(TvShowDetails.this, ActorActivity.class);
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
        String directorsString = stringUtils.getDirectorString(crew);
        director.setText(directorsString);
        String writersString = stringUtils.getWritersString(crew);
        writers.setText(writersString);

    }

    @Override
    public void showDetails(final TvShowDetail series) {
        final List<Integer> seasons = new ArrayList<>();
        director.setText("");
        String year = series.getFirstAirDate();
        if (year != null) {
            title.setText(stringUtils.getTitle(series.getName(), year));
        }
        if (series.getGenres() == null) {
            genre.setText(R.string.genre_unknown);
        } else
            genre.setText(stringUtils.getGenre(series));
        for (int i = 1; i <= series.getNumberOfSeasons(); i++) {
            seasons.add(i);
        }

        releaseDate.setText(series.getAiring());
        rating.setText(series.getVoteAverage().toString());
        overview.setText(series.getOverview());
        seasonNum = seasons.size();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        seasonRecyclerView.setLayoutManager(layoutManager);
        seasonRecyclerView.setAdapter(new HorizontalAdapter(seasons, R.layout.season_number_item));
        seasonRecyclerView.addOnItemTouchListener(new HorizontalAdapter.RecyclerTouchListener(this, seasonRecyclerView, new HorizontalAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                Intent intent = new Intent(TvShowDetails.this, SeasonsActivity.class);
                intent.putExtra(getString(R.string.show_id_intent), series.getId());
                intent.putExtra(getString(R.string.season_id_intent), seasons.get(position));
                intent.putExtra(getString(R.string.season_num_intent), seasons.size());

                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        showPoster(series);

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

    public void showPoster(TvShowDetail series) {
        Glide.with(this).load(series.getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);

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
