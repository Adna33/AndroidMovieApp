package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    private static final int MOST_POPULAR = 0;
    private static final int LATEST = 1;
    private static final int HIGHEST_RATED = 2;


    private final static String API_KEY = "e87991d039ddbe98de1958bf8a8b5148";

    public MovieListPresenter(MovieListView view) {
        this.view = view;
    }

    public void getHighestRatedMovies(int tag) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;
        if (tag == MOST_POPULAR)
            call = apiservice.discoverMovies("popularity.desc", API_KEY);
        else if (tag == LATEST) call = apiservice.discoverMovies("release_date.desc", API_KEY);
        else call = apiservice.getHighestRatedMovies(API_KEY);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    view.showMovies(movies);
                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }


            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }


}
