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
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.adapter.MovieListAdapter;
import atlant.moviesapp.helper.OnItemClick;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.presenters.MovieListPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.MovieListView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostPopularMoviesFragment extends Fragment implements MovieListView {

    private static final int TAG = 0;
    private int currentPage = 1;
    List<Movie> movies;
    MovieListAdapter adapter;
    private BodyFavourite bodyFavourite;


    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;
    boolean isConnected;

    private MovieListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_most_popular, container, false);
        ButterKnife.bind(this, view);

        presenter = new MovieListPresenter(this);
        movies = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new MovieListAdapter(movies, R.layout.list_item, getActivity().getApplicationContext());
        isConnected=isNetworkAvailable();
        adapter.setItemClick(new OnItemClick() {
            @Override
            public void onFavoriteClicked(int position) {
                if (ApplicationState.isLoggedIn()) {

                    Movie m = movies.get(position);
                    if (ApplicationState.getUser().getFavouriteMovies().contains(m.getId())) {
                        ApplicationState.getUser().removeFavouriteMovie(m.getId());
                        if (isConnected) {
                            presenter.postFavorite(m.getId(), ApplicationState.getUser().getSessionId(), false);
                        } else {
                            presenter.removeFavoriteRealm(m.getId());
                        }
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.removedFavorite), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addFavouriteMovie(m.getId());
                        if (isConnected) {
                            presenter.postFavorite(m.getId(), ApplicationState.getUser().getSessionId(), true);
                        } else {
                            presenter.postFavoriteRealm(m.getId());
                        }
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.addedFavorite), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    showLoginError();
                }
            }

            @Override
            public void onWatchlistClicked(int position) {
                if (ApplicationState.isLoggedIn()) {

                    Movie m = movies.get(position);
                    if (ApplicationState.getUser().getWatchListMovies().contains(m.getId())) {
                        ApplicationState.getUser().removeWatchlistMovie(m.getId());
                        if (isConnected) {
                            presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), false);
                        } else {
                            presenter.removeWatchlistRealm(m.getId());
                        }
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistRemoved), Toast.LENGTH_SHORT).show();

                    } else {
                        ApplicationState.getUser().addWatchlistMovie(m.getId());
                        if (isConnected) {
                            presenter.postWatchlist(m.getId(), ApplicationState.getUser().getSessionId(), true);
                        } else {
                            presenter.postWatchlistRealm(m.getId());

                        }
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.watchlistAdded), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    showLoginError();
                }
            }

            @Override
            public void onposterClicked(int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movie", movies.get(position));
                startActivity(intent);
            }
        });

        adapter.setLoadMoreListener(new MovieListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (isConnected) {
                            presenter.getHighestRatedMovies(TAG, ++currentPage);
                        } else {
                            presenter.setUpMovies(TAG, ++currentPage);
                        }

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        if (isConnected) {
            showProgress();
            presenter.getHighestRatedMovies(TAG, 1);
        } else {
            presenter.setUpMovies(TAG,1);
            hideProgress();
        }
        return view;

    }

    @Override
    public void showMovies(final List<Movie> data) {
        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);

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
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
