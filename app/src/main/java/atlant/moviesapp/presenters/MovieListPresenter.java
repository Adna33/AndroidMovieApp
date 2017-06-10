package atlant.moviesapp.presenters;

import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.realm.RealmUtil;
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


    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;
    private Call<PostResponse> postCall;

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<MoviesResponse> call;
    private Call<PostResponse> postCallWatchlist;


    public MovieListPresenter(MovieListView view) {
        this.view = view;
    }

    public void getHighestRatedMovies(final int tag, final int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        if (tag == MOST_POPULAR)
            call = apiservice.discoverMovies("popularity.desc", API_KEY, page);
        else if (tag == LATEST)
            call = apiservice.getUpcomingMovies( API_KEY, page);
        else call = apiservice.getHighestRatedMovies(API_KEY, page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();

                    if (RealmUtil.getInstance().getRealmList(page) == null)
                        RealmUtil.getInstance().createRealmList(page);

                    if (tag == MOST_POPULAR)
                        RealmUtil.getInstance().addPopularMovies(page, movies);
                    else if (tag == LATEST)
                        RealmUtil.getInstance().addLatestMovies(page, movies);
                    else
                        RealmUtil.getInstance().addHighestRatedMovies(page, movies);

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

    public void setUpMovies(final int tag, int page) {
        List<Movie> list = new ArrayList<>();
        if (RealmUtil.getInstance().getRealmList(page) != null) {
            if (tag == MOST_POPULAR)
                list = RealmUtil.getInstance().getPopularMovies(page);
            else if (tag == LATEST)
                list = RealmUtil.getInstance().getLatestMovies(page);
            else
                list = RealmUtil.getInstance().getHighestRatedMovies(page);
            if (list != null)
                view.showMovies(list);
        }

    }
    public void postFavoriteRealm(int id) {
        if (RealmUtil.getInstance().getPostMovie(id) == null) {
            RealmUtil.getInstance().createPostMovie(id);
        }
        RealmUtil.getInstance().addFavoriteMovie(id);
        RealmUtil.getInstance().setMovieFavorite(RealmUtil.getInstance().getPostMovie(id), true);
    }

    public void removeFavoriteRealm(int id) {
        if (RealmUtil.getInstance().getPostMovie(id) == null) {
            RealmUtil.getInstance().createPostMovie(id);
        }
        RealmUtil.getInstance().deleteRealmInt(id);
        RealmUtil.getInstance().setMovieFavorite(RealmUtil.getInstance().getPostMovie(id), false);
    }

    public void postWatchlistRealm(int id) {
        if (RealmUtil.getInstance().getPostMovie(id) == null) {
            RealmUtil.getInstance().createPostMovie(id);
        }
        RealmUtil.getInstance().addMovieWatchlist(id);
        RealmUtil.getInstance().setMovieWatchlist(RealmUtil.getInstance().getPostMovie(id), true);
    }

    public void removeWatchlistRealm(int id) {
        if (RealmUtil.getInstance().getPostMovie(id) == null) {
            RealmUtil.getInstance().createPostMovie(id);
        }
        RealmUtil.getInstance().deleteRealmInt(id);
        RealmUtil.getInstance().setMovieWatchlist(RealmUtil.getInstance().getPostMovie(id), false);
    }
    public void postFavorite(int id, String session_id, Boolean b) {
        BodyFavourite favorite = new BodyFavourite("movie", id, b);
        if (b) {
            RealmUtil.getInstance().addFavoriteMovie(id);
        } else {
            RealmUtil.getInstance().deleteRealmInt(id);
        }
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

    public void postWatchlist(int id, String session_id, Boolean b) {
        if (b) {
            RealmUtil.getInstance().addMovieWatchlist(id);
        } else {
            RealmUtil.getInstance().deleteRealmInt(id);
        }
        BodyWatchlist watchlist = new BodyWatchlist("movie", id, b);
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
