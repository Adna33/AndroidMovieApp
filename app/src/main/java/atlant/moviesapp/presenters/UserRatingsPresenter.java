package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.realm.models.RealmInt;
import atlant.moviesapp.realm.RealmUtil;
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
    private Call<PostResponse> deleteCall;
    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;

    public void getMovieRatings(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserRatedMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


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
                }


            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
                view.hideProgress();
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
                }


            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
                view.hideProgress();
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }

    public void setUpRatedMovies()
    {
        List<RealmInt> movieIds= RealmUtil.getInstance().getRealmAccount().getRatedMovies();
        List<Movie> movies= new ArrayList<>();
        for(RealmInt m: movieIds) {
            if (RealmUtil.getInstance().getMovieFromRealm(m.getId()) != null)
                movies.add(RealmUtil.getInstance().getMovieFromRealm(m.getId()));


        }
        view.showMovies(movies);

    }
    public void setUpRatedSeries()
    {
        List<RealmInt> Ids= RealmUtil.getInstance().getRealmAccount().getRatedSeries();
        Log.d("velicina", Ids.size()+"");
        List<TvShow> series= new ArrayList<>();
        for(RealmInt m: Ids) {
            if (RealmUtil.getInstance().getTvShowFromRealm(m.getId()) != null)
                series.add(RealmUtil.getInstance().getTvShowFromRealm(m.getId()));


        }
        view.showTvShows(series);

    }
    public void deleteRating(int id, String session_id, int tag) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        if (tag == MOVIE)
            deleteCall = apiservice.removeRatingMovie(id, API_KEY, session_id);
        else if (tag == TVSHOW)
            deleteCall = apiservice.removeRatingSeries(id, API_KEY, session_id);
        deleteCall.enqueue(new Callback<PostResponse>() {
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
