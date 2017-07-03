package atlant.moviesapp.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.R;
import atlant.moviesapp.activity.MovieDetailsActivity;
import atlant.moviesapp.adapter.UserListAdapter;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.UserFavoritesPresenter;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.views.UserFavoritesView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoritesFragment extends Fragment implements UserFavoritesView {

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

    private int currentPage = 1;

    UserFavoritesPresenter presenter;
    private boolean resumeHasRun = false;
    List<Movie> favoriteMovies;
    UserListAdapter adapter;
    private Paint p = new Paint();
    private Paint t = new Paint();

    public MovieFavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_favorites, container, false);
        ButterKnife.bind(this, v);
        presenter = new UserFavoritesPresenter(this);
        favoriteMovies = new ArrayList<>();
        adapter = new UserListAdapter(favoriteMovies, R.layout.user_list_item, recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new UserListAdapter.RecyclerTouchListener(getActivity(), recyclerView, new UserListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                Movie movie = favoriteMovies.get(position);
                intent.putExtra(getString(R.string.movie), movie);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        adapter.setLoadMoreListener(new UserListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        if (ApplicationState.isNetworkAvailable(getActivity().getApplicationContext())) {
                            presenter.getMovieFavorites(++currentPage);
                        }

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);

        if (ApplicationState.isNetworkAvailable(getActivity().getApplicationContext())) {
            showProgress();
            presenter.getMovieFavorites(currentPage);
        } else {
            presenter.setUpFavoriteMovies();
            hideProgress();

        }

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    int id = favoriteMovies.get(position).getId();
                    adapter.removeItem(position);
                    ApplicationState.getUser().removeFavouriteMovie(id);
                    RealmUtil.getInstance().deleteRealmInt(id);
                    if (ApplicationState.isNetworkAvailable(getActivity().getApplicationContext())) {
                        presenter.postFavorite(id, ApplicationState.getUser().getSessionId(), 0);
                    } else {
                        if (RealmUtil.getInstance().getPostMovie(id) == null) {
                            RealmUtil.getInstance().createPostMovie(id);
                        }
                        RealmUtil.getInstance().setMovieFavorite(RealmUtil.getInstance().getPostMovie(id), false);

                    }

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if (dX > 0) {
                        p.setColor(getResources().getColor(R.color.standardYellow));
                        t.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                    } else {
                        p.setColor(getResources().getColor(R.color.standardYellow));
                        t.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.errorMessage))
                .setTitle(getString(R.string.errorTitle));

        builder.setPositiveButton(getString(R.string.OkButton), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void showMovies(List<Movie> data) {
        if (!(data.size() > 0)) {
            adapter.setMoreDataAvailable(false);
        } else {
            favoriteMovies.addAll(data);
            adapter.notifyDataChanged();
        }

    }

    @Override
    public void showTvShows(List<TvShow> data) {

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
    public void onResume() {
        super.onResume();
        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        }
        adapter.clear();
        favoriteMovies.clear();
        if (ApplicationState.isNetworkAvailable(getActivity().getApplicationContext())) {
            showProgress();
            presenter.getMovieFavorites(1);
        } else {
            presenter.setUpFavoriteMovies();
            hideProgress();

        }
    }

}
