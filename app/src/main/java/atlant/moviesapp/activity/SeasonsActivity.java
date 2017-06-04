package atlant.moviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.EpisodeAdapter;
import atlant.moviesapp.adapter.HorizontalAdapter;
import atlant.moviesapp.adapter.NewsFeedAdapter;
import atlant.moviesapp.helper.StringUtils;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.presenters.SeasonsPresenter;
import atlant.moviesapp.presenters.TvDetailsPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.SeasonsView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SeasonsActivity extends AppCompatActivity implements SeasonsView {

    SeasonsPresenter presenter;

    @BindView(R.id.seasons_recycler_view)
    RecyclerView seasonRecyclerView;

    @BindView(R.id.episodes_recycler_view)
    RecyclerView episodesRecyclerView;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.season_year)
    TextView seasonYear;
    String realmId;
    private Integer seriesId, seasonId, seasonNum;
    private StringUtils stringUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tvshows_title);
        stringUtils= new StringUtils(this);
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
        seriesId = intent.getIntExtra(getString(R.string.show_id_intent), 0);
        seasonId = intent.getIntExtra(getString(R.string.season_id_intent), 0);
        seasonNum = intent.getIntExtra(getString(R.string.season_num_intent), 0);
        realmId= seriesId+""+seasonId;
        ShowSeasonList(seasonNum, seriesId);
        presenter = new SeasonsPresenter(this);
        if (isConnected) {
            if (RealmUtil.getInstance().getRealmSeason(realmId) == null)
                RealmUtil.getInstance().createRealmSeason(realmId);
            presenter.getSeasonEpisodes(seriesId, seasonId,realmId);
        } else {
            presenter.setUpSeason(realmId);
        }

    }

    @Override
    public void ShowSeasonList(Integer seasonNum, final Integer id) {

        final List<Integer> seasons = new ArrayList<>();
        for (int i = 1; i <= seasonNum; i++) {
            seasons.add(i);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        seasonRecyclerView.setLayoutManager(layoutManager);
        seasonRecyclerView.setAdapter(new HorizontalAdapter(seasons, R.layout.season_item));
        seasonRecyclerView.addOnItemTouchListener(new HorizontalAdapter.RecyclerTouchListener(this, seasonRecyclerView, new HorizontalAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                realmId= stringUtils.getId(seriesId,seasons.get(position));
                if(isNetworkAvailable()) {
                    presenter.getSeasonEpisodes(id, seasons.get(position), realmId);
                }
                else {
                    presenter.setUpSeason(realmId);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public void ShowEpisodes(final List<Episode> episodes) {

        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        episodesRecyclerView.setAdapter(new EpisodeAdapter(episodes, R.layout.season_episode_item, this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(episodesRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        episodesRecyclerView.addItemDecoration(dividerItemDecoration);
        episodesRecyclerView.addOnItemTouchListener(new EpisodeAdapter.RecyclerTouchListener(this, episodesRecyclerView, new EpisodeAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SeasonsActivity.this, EpisodeActivity.class);

                if (isNetworkAvailable()) {
                    intent.putExtra(getString(R.string.episodeIntent), presenter.getEpisodes().get(position));
                } else {
                    intent.putExtra(getString(R.string.episodeIntent), episodes.get(position));
                }
                intent.putExtra(getString(R.string.seriesId), seriesId);
                intent.putExtra(getString(R.string.seasonId), seasonId);
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
    public void ShowYear(String year) {
        seasonYear.setText(year);
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
