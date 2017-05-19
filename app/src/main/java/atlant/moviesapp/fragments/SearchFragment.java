package atlant.moviesapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.EpisodeActivity;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.activity.SeasonsActivity;
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.adapter.EpisodeAdapter;
import atlant.moviesapp.adapter.MovieListAdapter;
import atlant.moviesapp.adapter.NewsFeedAdapter;
import atlant.moviesapp.adapter.SearchResultAdapter;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.SearchResult;
import atlant.moviesapp.presenters.SearchPresenter;
import atlant.moviesapp.views.SearchView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView {

    @BindView(R.id.search_recycler_view)
    RecyclerView recyclerView;


    private SearchPresenter presenter;

    List<SearchResult> search;
    SearchResultAdapter adapter;
    private int currentPage = 1;
    String query;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        presenter = new SearchPresenter(this);
        search = new ArrayList<>();
        adapter = new SearchResultAdapter(search, R.layout.search_result_item, recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new SearchResultAdapter.RecyclerTouchListener(getActivity(), recyclerView, new SearchResultAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (search.get(position).getMediaType().equals(getString(R.string.tv))) {
                    Intent intent = new Intent(getActivity(), TvShowDetails.class);
                    intent.putExtra(getString(R.string.series), search.get(position).getId());
                    startActivity(intent);
                } else if (search.get(position).getMediaType().equals(getString(R.string.movie))) {
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                    Movie movie = search.get(position).getMovie();
                    intent.putExtra(getString(R.string.movie), movie);
                    startActivity(intent);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter.setLoadMoreListener(new SearchResultAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        presenter.getSearchResults(query, currentPage);

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {

            query = getArguments().getString(getString(R.string.edttext));
            if (query.length() > 3)
                presenter.getSearchResults(query, currentPage);
        }


        return v;
    }

    @Override
    public void DisplayResults(final List<SearchResult> data) {
        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);
        } else {
            search.addAll(data);
            adapter.notifyDataChanged();
        }

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
