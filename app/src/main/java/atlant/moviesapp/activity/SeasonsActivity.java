package atlant.moviesapp.activity;

import android.content.Intent;
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
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.presenters.SeasonsPresenter;
import atlant.moviesapp.presenters.TvDetailsPresenter;
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

    private Integer seriesId, seasonId, seasonNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);

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
        seriesId = intent.getIntExtra("showId", 0);
        seasonId = intent.getIntExtra("seasonId", 0);
        seasonNum = intent.getIntExtra("seasonNum", 0);

        ShowSeasonList(seasonNum, seriesId);
        presenter = new SeasonsPresenter(this);
        presenter.getSeasonEpisodes(seriesId, seasonId);

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
                presenter.getSeasonEpisodes(id, seasons.get(position));

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
                intent.putExtra("episode", presenter.getEpisodes().get(position));
                intent.putExtra("seriesid", seriesId);
                intent.putExtra("seasonid", seasonId);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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
