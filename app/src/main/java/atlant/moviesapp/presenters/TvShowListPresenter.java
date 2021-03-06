package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.MovieListView;
import atlant.moviesapp.views.TvShowListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 13.04.2017..
 */

public class TvShowListPresenter {

    private TvShowListView view;
    private static final int MOST_POPULAR = 0;
    private static final int LATEST = 1;
    private static final int HIGHEST_RATED = 2;

    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;
    private Call<PostResponse> postCall;

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<TvShowsResponse> call;
    private Call<PostResponse> postCallWatchlist;

    public TvShowListPresenter(TvShowListView view) {
        this.view = view;
    }

    public void getHighestRatedSeries(int tag, int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        if (tag == MOST_POPULAR)
            call = apiservice.discoverSeries("popularity.desc", API_KEY, page);
        else if (tag == LATEST)
            call = apiservice.discoverSeries("release_date.desc", API_KEY, page);
        else if (tag == HIGHEST_RATED) call = apiservice.getHighestRatedSeries(API_KEY, page);
        else call = apiservice.getAiringSeries(API_KEY, page);

        //TODO Airing


        call.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<TvShow> shows = response.body().getResults();
                    view.hideProgress();
                    view.showTvShows(shows);


                } else {
                    view.hideProgress();
                    view.showError();
                    //TODO onFailure
                    Log.e("TAG", "Error TvShowResponse Code");
                }
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString() + "On Failure");

            }
        });


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

    public void onStop() {
        if (call != null)
            call.cancel();
    }

    public void onDestroy() {
        if (call != null)
            call.cancel();
        view = null;
    }


}
