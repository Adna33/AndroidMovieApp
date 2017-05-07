package atlant.moviesapp.presenters;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MovieGenre;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.MovieListView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 12.04.2017..
 */

public class MovieListPresenter {

    private MovieListView view;
    private ProgressBar progressBar;
    private static final int MOST_POPULAR = 0;
    private static final int LATEST = 1;
    private static final int HIGHEST_RATED = 2;


    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<MoviesResponse> call;


    public MovieListPresenter(MovieListView view) {
        this.view = view;
    }

    public void getHighestRatedMovies(int tag, int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        if (tag == MOST_POPULAR)
            call = apiservice.discoverMovies("popularity.desc", API_KEY, page);
        else if (tag == LATEST)
            call = apiservice.discoverMovies("release_date.desc", API_KEY, page);
        else call = apiservice.getHighestRatedMovies(API_KEY, page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
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
