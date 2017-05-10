package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.UserRatingsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 07.05.2017..
 */

public class UserRatingsPresenter {

    UserRatingsView view;

    public UserRatingsPresenter(UserRatingsView view) {
        this.view = view;
    }
    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<MoviesResponse> call;
    private Call<TvShowsResponse> seriescall;
    public void getMovieRatings(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserRatedMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


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
    public void getSeriesRatings(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        seriescall = apiservice.getUserRatedSeries(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


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
