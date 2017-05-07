package atlant.moviesapp.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.adapter.UserListAdapter;
import atlant.moviesapp.adapter.UserListSeriesAdapter;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.UserRatingsPresenter;
import atlant.moviesapp.views.UserRatingsView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeriesRatingsFragment extends Fragment implements UserRatingsView {

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

    private int currentPage = 1;

    private UserRatingsPresenter presenter;

    private static final int TAG = 0;

    public SeriesRatingsFragment() {
        // Required empty public constructor
    }
    List<TvShow> ratedSeries;
    UserListSeriesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_movie_ratings, container, false);
        ButterKnife.bind(this, v);
        presenter=new UserRatingsPresenter(this);
        ratedSeries=new ArrayList<>();
        adapter = new UserListSeriesAdapter(ratedSeries, R.layout.search_result_item, recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new UserListSeriesAdapter.RecyclerTouchListener(getActivity(), recyclerView, new UserListSeriesAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TvShowDetails.class);
                intent.putExtra("series", ratedSeries.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter.setLoadMoreListener(new UserListSeriesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        presenter.getSeriesRatings(++currentPage);

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        presenter.getSeriesRatings(1);
        return v;
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
    public void showMovies(List<Movie> data) {


    }

    @Override
    public void showTvShows(List<TvShow> data) {
        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);
            Toast.makeText(getActivity().getApplicationContext(), "No More Data Available", Toast.LENGTH_LONG).show();
        } else {
            ratedSeries.addAll(data);
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

}
