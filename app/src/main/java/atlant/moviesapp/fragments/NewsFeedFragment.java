package atlant.moviesapp.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.adapter.NewsFeedAdapter;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.News;
import atlant.moviesapp.presenters.NewsFeedPresenter;
import atlant.moviesapp.views.NewsFeedView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements NewsFeedView {

    @BindView(R.id.news_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.news_progress_bar)
    ProgressBar progressBar;


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
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ButterKnife.bind(this, v);

        boolean isConnected = ApplicationState.isNetworkAvailable(getActivity().getApplicationContext());

        presenter = new NewsFeedPresenter(this);
        if (isConnected) {
            showProgress();
            presenter.getNews();
        } else {
            hideProgress();
            presenter.setUpNews();
        }


        return v;
    }

    @Override
    public void showNews(List<News> data) {

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new NewsFeedAdapter(data, R.layout.list_item_news, getActivity().getApplicationContext()));
    }

    @Override
    public void showProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        if (progressBar != null)
            progressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.errorMessage)
                .setTitle(R.string.errorTitle);

        builder.setPositiveButton(R.string.OkButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.onDestroy();
        super.onDestroy();
    }
}
