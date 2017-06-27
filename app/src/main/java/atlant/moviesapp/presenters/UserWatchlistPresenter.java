package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.UserWatchlistView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 17.05.2017..
 */

public class UserWatchlistPresenter {

    UserWatchlistView view;

    public UserWatchlistPresenter(UserWatchlistView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<MoviesResponse> call;
    private Call<TvShowsResponse> seriescall;

    private Call<PostResponse> postCallWatchlist;

    public void getMovieWatchlist(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserMovieWatchlist(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    Log.d("proba",movies.size()+"");


                    view.hideProgress();
                    view.showMovies(movies);
                } else {
                    view.hideProgress();
                    view.showError();
                }


            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
                view.hideProgress();
                view.showError();
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }
    public void getSeriesWatchlist(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        seriescall = apiservice.getUserSeriesWatchlist(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        seriescall.enqueue(new Callback<TvShowsResponse>() {
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
                }


            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
                view.hideProgress();
                view.showError();
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


