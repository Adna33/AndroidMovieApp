package atlant.moviesapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.MoviesAdapter;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.presenters.MovieListPresenter;
import atlant.moviesapp.views.MovieListView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LatestFragment extends Fragment  implements MovieListView {

    private static final int TAG=1;
    @BindView(R.id.latest_recycler_view)
    RecyclerView recyclerView;

    private MovieListPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);

        ButterKnife.bind(this,view);
        presenter=new MovieListPresenter(this);
        presenter.getHighestRatedMovies(TAG);

        return view;
    }

    @Override
    public void showMovies(List<Movie> data) {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new MoviesAdapter(data, R.layout.list_item_movie,getActivity().getApplicationContext()));
    }
}
