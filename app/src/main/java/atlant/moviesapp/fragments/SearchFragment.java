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

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.EpisodeActivity;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.activity.SeasonsActivity;
import atlant.moviesapp.activity.TvShowDetails;
import atlant.moviesapp.adapter.EpisodeAdapter;
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

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);
        presenter = new SearchPresenter(this);
        if (getArguments() != null) {

            String query = getArguments().getString("edttext");
            presenter.getSearchResults(query);
        }


        return v;
    }

    @Override
    public void DisplayResults(final List<SearchResult> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new SearchResultAdapter(data, R.layout.search_result_item, recyclerView.getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new SearchResultAdapter.RecyclerTouchListener(getActivity(), recyclerView, new SearchResultAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(data.get(position).getMediaType().equals("tv"))
                {
                    Intent intent = new Intent(getActivity(), TvShowDetails.class);
                    intent.putExtra("series", data.get(position).getId());
                    startActivity(intent);
                }
                else if(data.get(position).getMediaType().equals("movie"))
                {
                    Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                    Movie movie = data.get(position).getMovie();
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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
