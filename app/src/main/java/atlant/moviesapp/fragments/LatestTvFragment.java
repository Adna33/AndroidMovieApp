package atlant.moviesapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class LatestTvFragment extends Fragment implements TvShowListView {

    private static final int TAG = 1;

    @BindView(R.id.latest_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.latest_progress_bar)
    ProgressBar progressBar;

    private TvShowListPresenter presenter;
    private int currentPage = 1;
    List<TvShow> series;
    TVListAdapter adapter;
    boolean isConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_tv, container, false);

        ButterKnife.bind(this, view);
        presenter = new TvShowListPresenter(this);
        series = new ArrayList<>();
        isConnected = isNetworkAvailable();
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
                if (ApplicationState.isLoggedIn()) {
                    TvShow m = series.get(position);
                    if (ApplicationState.getUser().getWatchListSeries().contains(m.getId())) {
                        ApplicationState.getUser().removeWatchlistShow(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.tv), m.getId(), false);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistRemoved), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addWatchlistShow(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.tv), m.getId(), true);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), R.string.watchlistAdded, Toast.LENGTH_SHORT).show();

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

                        if (isConnected) {
                            presenter.getHighestRatedSeries(TAG, ++currentPage);
                        } else {
                            presenter.setUpSeries(TAG, ++currentPage);
                        }
                    }
                });

                     }
        });
        recyclerView.setAdapter(adapter);
        if (isConnected) {
            showProgress();
            presenter.getHighestRatedSeries(TAG, 1);
        } else {
            presenter.setUpSeries(TAG, 1);
            hideProgress();
        }
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
    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.errorMessage))
                .setTitle(getString(R.string.errorTitle));

        builder.setPositiveButton(getString(R.string.OkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

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
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
