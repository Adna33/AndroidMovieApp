package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.realm.models.RealmInt;
import atlant.moviesapp.realm.RealmUtil;
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

    public void setUpWatchlistMovies()
    {
        List<RealmInt> movieIds= RealmUtil.getInstance().getRealmAccount().getWatchlistMovies();
        List<Movie> movies= new ArrayList<>();
        for(RealmInt m: movieIds) {
            if (RealmUtil.getInstance().getMovieFromRealm(m.getId()) != null)
                movies.add(RealmUtil.getInstance().getMovieFromRealm(m.getId()));


        }
        view.showMovies(movies);

    }
    public void setUpWatchlistSeries()
    {
        List<RealmInt> Ids= RealmUtil.getInstance().getRealmAccount().getWatchlistSeries();
        List<TvShow> series= new ArrayList<>();
        for(RealmInt m: Ids) {
            if (RealmUtil.getInstance().getTvShowFromRealm(m.getId()) != null)
                series.add(RealmUtil.getInstance().getTvShowFromRealm(m.getId()));


        }
        view.showTvShows(series);

    }
    public void postWatchlist(int id, String session_id, int t) {
        BodyWatchlist watchlist;
        if(t==0)
            watchlist= new BodyWatchlist("movie", id, false);
        else
            watchlist= new BodyWatchlist("tv", id, false);
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


