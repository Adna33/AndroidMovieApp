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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.ActorAdapter;
import atlant.moviesapp.helper.Date;
import atlant.moviesapp.helper.StringUtils;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.presenters.EpisodePresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.EpisodeView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeActivity extends AppCompatActivity implements EpisodeView{

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_release_date)
    TextView releaseDate;

    @BindView(R.id.tv_rating)
    TextView rating;

    @BindView(R.id.tv_overview)
    TextView overview;

    @BindView(R.id.tv_poster)
    ImageView poster;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.cast_recycler_view)
    RecyclerView castRecyclerView;
    private StringUtils stringUtils;
    private EpisodePresenter presenter;
    private Date date;
    int episodeId;
    String realmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
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
        boolean isConnected = isNetworkAvailable();
        Intent intent = getIntent();
        Episode episode = intent.getParcelableExtra(getString(R.string.episodeIntent));
        Integer seriesId=intent.getIntExtra(getString(R.string.seriesId),0);
        Integer seasonId=intent.getIntExtra(getString(R.string.seasonId),0);
        episodeId=episode.getId();
        date=new Date(this);
        String year=episode.getAirDate();
        title.setText(stringUtils.getTitle(episode.getName(),year));
        releaseDate.setText(date.getFormatedDate(episode.getAirDate()));
        rating.setText(episode.getRatingString());
        overview.setText(episode.getOverview());
        Glide.with(this).load(episode.getImagePath())
                .crossFade().centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(poster);
        presenter=new EpisodePresenter(this);
        realmId=seriesId+""+seasonId+""+episode.getEpisodeNumber();
        if (isConnected) {
            if(RealmUtil.getInstance().getRealmEpisode(realmId)==null)
            {RealmUtil.getInstance().createRealmEpisode(realmId);}
            presenter.getCredits(seriesId,seasonId,episode.getEpisodeNumber(),realmId);
        } else {
            presenter.setUpEpisode(realmId);
        }

    }


    @Override
    public void showCast(final List<Cast> cast) {

        castRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(new ActorAdapter(cast, R.layout.actor_item, this));
        castRecyclerView.addOnItemTouchListener(new ActorAdapter.RecyclerTouchListener(this, castRecyclerView, new ActorAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(EpisodeActivity.this, ActorActivity.class);
                intent.putExtra(getString(R.string.actorId), cast.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null)
            presenter.onStop();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.onDestroy();
        super.onDestroy();
    }
}
