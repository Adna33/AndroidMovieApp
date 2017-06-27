package atlant.moviesapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
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
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.presenters.UserWatchlistPresenter;
import atlant.moviesapp.views.UserWatchlistView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieWatchlistFragment extends Fragment implements UserWatchlistView{

    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.movies_progress_bar)
    ProgressBar progressBar;

    private int currentPage = 1;

    UserWatchlistPresenter presenter;
    private boolean resumeHasRun = false;
    List<Movie> watchlistMovies;
    UserListAdapter adapter;
    private Paint p = new Paint();
    private Paint t = new Paint();
    public MovieWatchlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user_movies, container, false);
        ButterKnife.bind(this, v);
        presenter=new UserWatchlistPresenter(this);
        watchlistMovies= new ArrayList<>();
        adapter = new UserListAdapter(watchlistMovies, R.layout.user_list_item, recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new UserListAdapter.RecyclerTouchListener(getActivity(), recyclerView, new UserListAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                Movie movie = watchlistMovies.get(position);
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

                        presenter.getMovieWatchlist(++currentPage);

                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        presenter.getMovieWatchlist(1);


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT){
                    adapter.removeItem(position);
                    int id=  ApplicationState.getUser().getWatchListMovies().get(position);
                    ApplicationState.getUser().removeFavouriteMovie(id);
                    BodyWatchlist bodyFavourite = new BodyWatchlist(getString(R.string.movie), id, false);
                    presenter.postWatchlist(id, ApplicationState.getUser().getSessionId(), bodyFavourite);

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if(dX > 0){
                        p.setColor(getResources().getColor(R.color.standardYellow));
                        t.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawText("Remove",(float) itemView.getLeft() + width,(float) itemView.getTop() + width,t);
                    } else {
                        p.setColor(getResources().getColor(R.color.standardYellow));
                        t.setColor(Color.parseColor("#FFFFFF"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawText("Remove",(float) itemView.getLeft() + width,(float) itemView.getTop() + width,t);

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
            watchlistMovies.addAll(data);
            adapter.notifyDataChanged();
        }

    }

    @Override
    public void showTvShows(List<TvShow> data) {

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
    public void onResume() {
        super.onResume();
        if (!resumeHasRun) {
            resumeHasRun = true;
            return;
        }
        adapter.clear();
        watchlistMovies.clear();
        presenter.getMovieWatchlist(1);
    }
}
