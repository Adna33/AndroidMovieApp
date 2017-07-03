package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import atlant.moviesapp.BuildConfig;

import atlant.moviesapp.model.Account;

import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.Session;
import atlant.moviesapp.model.Token;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.realm.models.RealmPostMovie;
import atlant.moviesapp.realm.models.RealmPostSeries;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.LoginView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPresenter {

    LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<Token> tokenCall;
    private Call<Token> validateTokenCall;
    private Call<Session> createSessionCall;
    private Call<Account> getAccountCall;

    private Call<MoviesResponse> call;
    private Call<TvShowsResponse> seriescall;

    public void requestToken(final String username, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        tokenCall = apiservice.requestToken(API_KEY);
        tokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Token token = response.body();
                    String tokenString = token.getRequestToken();

                    validateToken(username, password, tokenString);


                } else {
                    view.showError();
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void validateToken(String username, final String password, final String token) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        validateTokenCall = apiservice.validateToken(API_KEY, username, password, token);
        validateTokenCall.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    String tokenString = response.body().getRequestToken();

                    createSession(tokenString, password);


                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }


    public void createSession(String token, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        createSessionCall = apiservice.startSession(API_KEY, token);
        createSessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Session session = response.body();
                    String sessionId = session.getSessionId();
                    Log.d("ACCOUNT", sessionId);
                    getAccount(sessionId, password);


                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void getAccount(final String sessionId, final String password) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        getAccountCall = apiservice.getAccount(API_KEY, sessionId);
        getAccountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    ApplicationState.setUser(response.body());
                    ApplicationState.getUser().setSessionId(sessionId);

                    RealmUtil.getInstance().createRealmAccount();



                    view.loggedUser(password);

                } else {
                    view.showError();
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void getMovieFavorites(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserFavoriteMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    List<Integer> movieId=new ArrayList<Integer>();
                    for (Movie movie : movies) {
                        ApplicationState.getUser().addFavouriteMovie(movie.getId());
                        movieId.add(movie.getId());
                        RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                        RealmUtil.getInstance().setMovieFavorite(post,true);
                    }
                    RealmUtil.getInstance().addFavoriteMovies(movieId);
                    if(response.body().getTotalPages()>1)
                        for(int i=2;i<=response.body().getTotalPages();i++)
                        {
                            call = apiservice.getUserFavoriteMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                            call.enqueue(new Callback<MoviesResponse>() {
                                @Override
                                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        List<Movie> movies = response.body().getResults();
                                        for (Movie movie : movies) {
                                            ApplicationState.getUser().addFavouriteMovie(movie.getId());
                                            RealmUtil.getInstance().addFavoriteMovie(movie.getId());
                                            RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                                            RealmUtil.getInstance().setMovieFavorite(post,true);

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                                    if (call.isCanceled()) {
                                    }
                                }
                            });
                        }

                }


            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }

            }
        });


    }
    public void getSeriesFavorites(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        seriescall = apiservice.getUserFavoriteSeries(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        seriescall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<TvShow> shows = response.body().getResults();
                    List<Integer> Ids=new ArrayList<Integer>();
                    for (TvShow show : shows) {
                        ApplicationState.getUser().addFavouriteShow(show.getId());
                        Ids.add(show.getId());
                        RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                        RealmUtil.getInstance().setSeriesFavorite(post,true);
                    }
                    RealmUtil.getInstance().addFavoriteSeries(Ids);

                    if(response.body().getTotalPages()>1)
                        for(int i=2;i<=response.body().getTotalPages();i++)
                        {
                            seriescall = apiservice.getUserFavoriteSeries(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                            seriescall.enqueue(new Callback<TvShowsResponse>() {
                                @Override
                                public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        List<TvShow> shows = response.body().getResults();
                                        for (TvShow show : shows) {
                                            ApplicationState.getUser().addFavouriteShow(show.getId());
                                            RealmUtil.getInstance().addFavoriteSeries(show.getId());
                                            RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                                            RealmUtil.getInstance().setSeriesFavorite(post,true);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                                    if (call.isCanceled()) {
                                    }
                                }
                            });
                        }
                }
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
            }
        });
    }
    public void getMovieRatings(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserRatedMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    List<Integer> Ids=new ArrayList<Integer>();
                    for (Movie movie : movies) {
                        Ids.add(movie.getId());
                        ApplicationState.getUser().addMovieRating(movie.getId());
                        RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                        RealmUtil.getInstance().setMovieRating(post,movie.getRatingString());
                    }
                    RealmUtil.getInstance().addMovieRatings(Ids);
                    if(response.body().getTotalPages()>1)
                        for(int i=2;i<=response.body().getTotalPages();i++)
                        {
                            call = apiservice.getUserRatedMovies(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                            call.enqueue(new Callback<MoviesResponse>() {
                                @Override
                                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        List<Movie> movies = response.body().getResults();
                                        for (Movie movie : movies) {
                                            ApplicationState.getUser().addMovieRating(movie.getId());
                                            RealmUtil.getInstance().addMovieRating(movie.getId());
                                            RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                                            RealmUtil.getInstance().setMovieRating(post,movie.getRatingString());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                                    if (call.isCanceled()) {
                                    }
                                }
                            });
                        }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
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
                    List<Integer> Ids=new ArrayList<Integer>();
                    for (TvShow show : shows) {
                        ApplicationState.getUser().addShowRating(show.getId());
                        Ids.add(show.getId());
                        RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                        RealmUtil.getInstance().setSeriesRating(post,show.getRatingString());
                    }
                    RealmUtil.getInstance().addSeriesRatings(Ids);
                    if(response.body().getTotalPages()>1)
                        for(int i=2;i<=response.body().getTotalPages();i++)
                        {
                            seriescall = apiservice.getUserRatedSeries(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                            seriescall.enqueue(new Callback<TvShowsResponse>() {
                                @Override
                                public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        List<TvShow> shows = response.body().getResults();
                                        for (TvShow show : shows) {
                                            ApplicationState.getUser().addShowRating(show.getId());
                                            RealmUtil.getInstance().addSeriesRatings(show.getId());
                                            RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                                            RealmUtil.getInstance().setSeriesRating(post,show.getRatingString());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                                    if (call.isCanceled()) {
                                    }
                                }
                            });
                        }
                }


            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }

            }
        });


    }

    public void getMovieWatchlist(int page) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getUserMovieWatchlist(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",page);


        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Movie> movies = response.body().getResults();
                    List<Integer> Ids=new ArrayList<Integer>();
                    for (Movie movie : movies) {
                        ApplicationState.getUser().addWatchlistMovie(movie.getId());
                        Ids.add(movie.getId());
                        RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                        RealmUtil.getInstance().setMovieWatchlist(post,true);
                    }
                    RealmUtil.getInstance().addMovieWatchlist(Ids);
                    if(response.body().getTotalPages()>1)
                    for(int i=2;i<=response.body().getTotalPages();i++)
                    {
                        call = apiservice.getUserMovieWatchlist(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                        call.enqueue(new Callback<MoviesResponse>() {
                            @Override
                            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                                int statusCode = response.code();
                                if (statusCode == 200) {
                                    List<Movie> movies = response.body().getResults();
                                    for (Movie movie : movies) {
                                        ApplicationState.getUser().addWatchlistMovie(movie.getId());
                                        RealmUtil.getInstance().addMovieWatchlist(movie.getId());
                                        RealmPostMovie post= RealmUtil.getInstance().getPostMovie(movie.getId());
                                        RealmUtil.getInstance().setMovieWatchlist(post,true);

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                                if (call.isCanceled()) {
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
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
                    List<Integer> Ids=new ArrayList<Integer>();
                    for (TvShow show : shows) {
                        ApplicationState.getUser().addWatchlistShow(show.getId());
                        Ids.add(show.getId());
                        RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                        RealmUtil.getInstance().setSeriesWatchlist(post,true);
                    }
                    RealmUtil.getInstance().addSeriesWatchlist(Ids);
                    if(response.body().getTotalPages()>1)
                        for(int i=2;i<=response.body().getTotalPages();i++)
                        {
                            seriescall = apiservice.getUserSeriesWatchlist(ApplicationState.getUser().getId(),API_KEY,ApplicationState.getUser().getSessionId(),"created_at.desc",i);
                            seriescall.enqueue(new Callback<TvShowsResponse>() {
                                @Override
                                public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                                    int statusCode = response.code();
                                    if (statusCode == 200) {
                                        List<TvShow> shows = response.body().getResults();
                                        for (TvShow show : shows) {
                                            ApplicationState.getUser().addWatchlistShow(show.getId());
                                            RealmUtil.getInstance().addShowWatchlist(show.getId());
                                            RealmPostSeries post= RealmUtil.getInstance().getPostSeries(show.getId());
                                            RealmUtil.getInstance().setSeriesWatchlist(post,true);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                                    if (call.isCanceled()) {
                                    }
                                }
                            });
                        }
                }
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }
            }
        });


    }

    public void onStop() {
        if (tokenCall != null)
            tokenCall.cancel();
        if (validateTokenCall != null)
            validateTokenCall.cancel();
        if (createSessionCall != null)
            createSessionCall.cancel();
        if (getAccountCall != null)
            getAccountCall.cancel();

    }

    public void onDestroy() {
        if (tokenCall != null)
            tokenCall.cancel();
        if (validateTokenCall != null)
            validateTokenCall.cancel();
        if (createSessionCall != null)
            createSessionCall.cancel();
        if (getAccountCall != null)
            getAccountCall.cancel();
        view = null;
    }
}
