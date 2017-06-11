package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.adapter.HorizontalAdapter;
import atlant.moviesapp.adapter.ImageAdapter;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.helper.StringUtils;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Backdrop;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowDetail;

import atlant.moviesapp.presenters.TvDetailsPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.TvDetailsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TvShowDetails extends AppCompatActivity implements TvDetailsView, ShareActionProvider.OnShareTargetSelectedListener {
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

    @BindView(R.id.images_recycler_view)
    RecyclerView imageRecyclerView;

    private TvDetailsPresenter presenter;
    private TvShowDetail series;

    private Integer seriesId;
    private Integer seasonNum;
    private Date date;
    @BindView(R.id.rate_txtBtn)
    TextView rateTxt;
    String name, decription, link;
    private static final int TAG = 1;
    private ShareActionProvider mShareActionProvider = null;

    @OnClick(R.id.rate_txtBtn)
    void rate() {
        Intent i = new Intent(this, RatingActivity.class);
        i.putExtra(getString(R.string.title), getString(R.string.ratethisTVshow));
        i.putExtra(getString(R.string.id), seriesId);
        i.putExtra(getString(R.string.tag), TAG);
        startActivity(i);

    }
    @OnClick(R.id.see_all_images)
    public void seeAllImages() {
        Intent intent = new Intent(TvShowDetails.this, GalleryActivity.class);
        intent.putExtra(getString(R.string.name), name);
        intent.putExtra(getString(R.string.id), seriesId);
        intent.putExtra(getString(R.string.tag), TAG);
        startActivity(intent);
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
        date = new Date(this);
        Intent intent = getIntent();
        seriesId = intent.getIntExtra(getString(R.string.series), 0);
        name = intent.getStringExtra(getString(R.string.name));
        link = intent.getStringExtra(getString(R.string.link));
        decription = intent.getStringExtra(getString(R.string.overview));

        presenter = new TvDetailsPresenter(this);
        if (isConnected) {
            if (RealmUtil.getInstance().getTvShowDetailFromRealm(seriesId) == null || RealmUtil.getInstance().getShowDetailsFromRealm(seriesId) == null)
                RealmUtil.getInstance().createRealmSeriesObject(seriesId);
            presenter.getDetails(seriesId);
            presenter.getCredits(seriesId);
            presenter.getImages(seriesId);
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
        String castString = StringUtils.showCast(cast);
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
        String directorsString = StringUtils.getDirectorString(crew, this);
        director.setText(directorsString);
        String writersString = StringUtils.getWritersString(crew, this);
        writers.setText(writersString);

    }

    @Override
    public void showDetails(final TvShowDetail series) {

        final List<Integer> seasons = new ArrayList<>();
        director.setText(getString(R.string.unknown_field));
        String year = series.getFirstAirDate();
        if (year != null) {
            title.setText(getString(R.string.getTitle, series.getName(), year));
        }
        if (series.getGenres() == null) {
            genre.setText(R.string.genre_unknown);
        } else
            genre.setText(StringUtils.getGenre(series));
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

    @Override
    public void showImages(List<Backdrop> backdrops) {
        imageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageRecyclerView.setAdapter(new ImageAdapter(getApplicationContext(), backdrops));
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

        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider source,
                                         Intent intent) {
        final String appName = intent.getComponent().getPackageName();
        if (appName.equals(getString(R.string.fbPackage))) {
            setupFacebookShareIntent();
            return true;
        }

        return false;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {

            mShareActionProvider.setShareIntent(shareIntent);
        }
    }


    private Intent createShareIntent() {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        SimpleTarget target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
            }
        };
        Glide.with(getApplicationContext()).load(link).asBitmap()
                .into(target);
        shareIntent.setType(getString(R.string.imageType));
        if (name != null)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, name);

        if (decription != null)
            shareIntent.putExtra(Intent.EXTRA_TEXT, decription);
        if (link != null)
            shareIntent.putExtra(Intent.EXTRA_STREAM, link);


        return shareIntent;
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareDialog = new ShareDialog(this);
        if (decription != null && link != null) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote(decription)
                    .setContentUrl(Uri.parse(link))
                    .build();
            shareDialog.show(linkContent);
        }
    }
}
