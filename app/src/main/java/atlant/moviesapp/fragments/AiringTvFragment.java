package atlant.moviesapp.fragments;
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
import atlant.moviesapp.adapter.TVListAdapter;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.TvShowListPresenter;
import atlant.moviesapp.views.TvShowListView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AiringTvFragment extends Fragment implements TvShowListView
{
    private static final int TAG = 3;


    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.airing_progress_bar)
    ProgressBar progressBar;

    private TvShowListPresenter presenter;

    public AiringTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_airing_tv, container, false);
        ButterKnife.bind(this,view);

        presenter=new TvShowListPresenter(this);
        presenter.getHighestRatedSeries(TAG);

        return view;
    }

    @Override
    public void showTvShows(List<TvShow> data) {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new TVListAdapter(data, R.layout.list_item, getActivity().getApplicationContext()));

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

