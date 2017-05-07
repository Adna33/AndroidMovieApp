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
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.adapter.TVListAdapter;
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

        recyclerView.addOnItemTouchListener(new TVListAdapter.RecyclerTouchListener(getActivity(), recyclerView, new TVListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TvShowDetails.class);
                intent.putExtra("series", series.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
            //telling adapter to stop calling load more as no more server data available
            Toast.makeText(getActivity().getApplicationContext(), "No More Data Available", Toast.LENGTH_LONG).show();
        } else {
            series.addAll(data);
            adapter.notifyDataChanged();
        }


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
}