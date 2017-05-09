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
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.adapter.MovieListAdapter;
import atlant.moviesapp.adapter.TVListAdapter;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.presenters.MovieListPresenter;
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


    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

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

        recyclerView.addOnItemTouchListener(new MovieListAdapter.RecyclerTouchListener(getActivity(), recyclerView, new MovieListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movie", movies.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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

        } else {
            movies.addAll(data);
            adapter.notifyDataChanged();
        }

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Error loading data, please check your connection")
                .setTitle("Error");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void hideProgress() {
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
}
