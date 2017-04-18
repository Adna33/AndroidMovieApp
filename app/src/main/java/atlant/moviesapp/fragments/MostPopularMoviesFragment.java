package atlant.moviesapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.adapter.MovieListAdapter;
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


    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

    private MovieListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_most_popular, container, false);
        ButterKnife.bind(this,view);

        presenter=new MovieListPresenter(this);
        presenter.getHighestRatedMovies(TAG);

        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void showMovies(final List<Movie> data) {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new MovieListAdapter(data, R.layout.list_item,getActivity().getApplicationContext()));
        recyclerView.addOnItemTouchListener(new MovieListAdapter.RecyclerTouchListener(getActivity(), recyclerView, new MovieListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra("movie", data.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);

    }
    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }
    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
