package atlant.moviesapp.presenters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.R;
import atlant.moviesapp.fragments.MovieFragment;
import atlant.moviesapp.fragments.NewsFeedFragment;
import atlant.moviesapp.fragments.TvShowFragment;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyRating;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.MainActivityView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 18.04.2017..
 */

public class MainActivityPresenter {
    private MainActivityView view;
    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;
    private Call<PostResponse> postCall;
    private Call<PostResponse> postCallWatchlist;
    private static final String API_KEY = BuildConfig.API_KEY;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }
    public int selectFragment(MenuItem item) {
        String s="";
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_feed:
                fragment = new NewsFeedFragment();

                s="News Feed";
                break;
            case R.id.menu_movies:
                fragment = new MovieFragment();
                s="Movies";
                break;
            case R.id.menu_tvshows:
                fragment = new TvShowFragment();
                s="Tv Shows";
                break;
        }
        view.showFragment(fragment,item);
        view.setupToolbar(s);


        return item.getItemId();

    }
    public void postFavorite(int id, String session_id, BodyFavourite favorite) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        postCall = apiservice.addFavorite(id, API_KEY, session_id, favorite);
        postCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int statusCode = response.code();
                if (statusCode == 201) {


                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }
    public void postWatchlist(int id, String session_id, BodyWatchlist watchlist) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        postCallWatchlist = apiservice.addWatchlist(id, API_KEY, session_id, watchlist);
        postCallWatchlist.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int statusCode = response.code();
                if (statusCode == 201) {


                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }
    private Call<PostResponse> ratingCall;

    public void postRating(int id, String session_id, BodyRating rating, int tag) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        if (tag == MOVIE)
            ratingCall = apiservice.rateMovie(id, API_KEY, session_id, rating);
        else if (tag == TVSHOW)
            ratingCall = apiservice.rateTVSeries(id, API_KEY, session_id, rating);
        ratingCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int statusCode = response.code();
                if (statusCode == 201) {


                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }


}
