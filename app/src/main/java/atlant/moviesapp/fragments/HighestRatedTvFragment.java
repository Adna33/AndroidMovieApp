package atlant.moviesapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.LoginActivity;
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.adapter.TVListAdapter;
import atlant.moviesapp.helper.OnItemClick;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.TvShowListPresenter;
import atlant.moviesapp.views.TvShowListView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HighestRatedTvFragment extends Fragment implements TvShowListView {

    @BindView(R.id.top_rated_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.top_rated_progress_bar)
    ProgressBar progressBar;

    private int currentPage = 1;
    List<TvShow> series;
    TVListAdapter adapter;


    private static final int TAG = 2;

    TvShowListPresenter presenter;

    public HighestRatedTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_highest_rated_tv, container, false);
        ButterKnife.bind(this, view);
        presenter = new TvShowListPresenter(this);
        series = new ArrayList<>();
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new TVListAdapter(series, getActivity().getApplicationContext());

        adapter.setItemClick(new OnItemClick() {
            @Override
            public void onfavouriteClicked(int position) {
                if (ApplicationState.isLoggedIn()) {
                    TvShow m = series.get(position);
                    if (ApplicationState.getUser().getFavouriteSeries().contains(m.getId())) {
                        ApplicationState.getUser().removeFavoriteShow(m.getId());
                        BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.tv), m.getId(), false);
                        presenter.postFavorite(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.removedFavorite), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addFavouriteShow(m.getId());
                        BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.tv), m.getId(), true);
                        presenter.postFavorite(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.addedFavorite), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    showLoginError();
                }
            }

            @Override
            public void onwatchlistClicked(int position) {
                TvShow m = series.get(position);
                if (ApplicationState.isLoggedIn()) {
                    if (ApplicationState.getUser().getWatchListSeries().contains(m.getId())) {
                        ApplicationState.getUser().removeWatchlistShow(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.tv), m.getId(), false);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistRemoved), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addWatchlistShow(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.tv), m.getId(), true);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistAdded), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    showLoginError();
                }
            }

            @Override
            public void onposterClicked(int position) {
                Intent intent = new Intent(getActivity(), TvShowDetails.class);
                intent.putExtra(getString(R.string.series), series.get(position).getId());
                startActivity(intent);
            }
        });
        adapter.setLoadMoreListener(new TVListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        presenter.getHighestRatedSeries(TAG, currentPage++);
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        presenter.getHighestRatedSeries(TAG, 1);
        return view;
    }

    @Override
    public void showTvShows(final List<TvShow> data) {

        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);
        } else {
            series.addAll(data);
            adapter.notifyDataChanged();
        }


    }

    @Override
    public void showProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);

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
    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.errorMessage))
                .setTitle(getString(R.string.errorTitle));

        builder.setPositiveButton(getString(R.string.OkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void showLoginError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.loginAcces)
                .setTitle(R.string.loginAccessTitle);

        builder.setNegativeButton(R.string.notNow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setPositiveButton(R.string.loginButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getContext(),LoginActivity.class);
                getContext().startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
