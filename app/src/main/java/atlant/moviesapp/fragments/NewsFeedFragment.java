package atlant.moviesapp.fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.NewsFeedAdapter;
import atlant.moviesapp.model.News;
import atlant.moviesapp.presenters.NewsFeedPresenter;
import atlant.moviesapp.views.NewsFeedView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements NewsFeedView{

@BindView(R.id.news_layout)
CoordinatorLayout coordinatorLayout;

    @BindView(R.id.news_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbarClassic)
    Toolbar toolbar;

    @BindView(R.id.feed_recycler_view)
    RecyclerView recyclerView;

    private NewsFeedPresenter presenter;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_news_feed, container, false);
        ButterKnife.bind(this,v);

        presenter=new NewsFeedPresenter(this);
        presenter.getNews();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
       ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.newsfeed_title);

        return v;
    }

    @Override
    public void showNews(List<News> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new NewsFeedAdapter(data, R.layout.list_item_news,getActivity().getApplicationContext()));
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
