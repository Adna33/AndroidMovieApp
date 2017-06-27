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
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.adapter.MovieListAdapter;
import atlant.moviesapp.adapter.TVListAdapter;
import atlant.moviesapp.helper.OnItemClick;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.MovieListPresenter;
import atlant.moviesapp.views.MovieListView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HighestRatedMoviesFragment extends Fragment implements MovieListView {

    @BindView(R.id.top_rated_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.top_rated_progress_bar)
    ProgressBar progressBar;

    private static final int TAG = 2;

    private int currentPage = 1;
    List<Movie> movies;
    MovieListAdapter adapter;


    private MovieListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highest_rated, container, false);
        ButterKnife.bind(this, view);
        presenter = new MovieListPresenter(this);
        movies = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new MovieListAdapter(movies, R.layout.list_item, getActivity().getApplicationContext());
        adapter.setItemClick(new OnItemClick() {
            @Override
            public void onfavouriteClicked(int position) {
                if (ApplicationState.isLoggedIn()) {
                    Movie m = movies.get(position);
                    if (ApplicationState.getUser().getFavouriteMovies().contains(m.getId())) {
                        ApplicationState.getUser().removeFavouriteMovie(m.getId());
                        BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.movie), m.getId(), false);
                        presenter.postFavorite(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.removedFavorite), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addFavouriteMovie(m.getId());
                        BodyFavourite bodyFavourite = new BodyFavourite(getString(R.string.movie), m.getId(), true);
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
                    Movie m = movies.get(position);
                    if (ApplicationState.getUser().getWatchListMovies().contains(m.getId())) {
                        ApplicationState.getUser().removeWatchlistMovie(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.movie), m.getId(), false);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistRemoved), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addWatchlistMovie(m.getId());
                        BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.movie), m.getId(), true);
                        presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), bodyFavourite);
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistAdded), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    showLoginError();
                }
            }

            @Override
            public void onposterClicked(int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(getString(R.string.movie), movies.get(position));
                startActivity(intent);
            }
        });

        adapter.setLoadMoreListener(new MovieListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        presenter.getHighestRatedMovies(TAG, ++currentPage);

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        presenter.getHighestRatedMovies(TAG, 1);
        return view;
    }

    @Override
    public void showMovies(final List<Movie> data) {
        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);
            //telling adapter to stop calling load more as no more server data available
        } else {
            movies.addAll(data);
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
                // User clicked OK button
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
