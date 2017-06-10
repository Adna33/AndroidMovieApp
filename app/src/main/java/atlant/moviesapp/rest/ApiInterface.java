package atlant.moviesapp.rest;

/**
 * Created by Korisnik on 07.04.2017..
 */

import atlant.moviesapp.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("discover/movie")
    Call<MoviesResponse> discoverMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("discover/tv")
    Call<TvShowsResponse> discoverSeries(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getHighestRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/top_rated")
    Call<TvShowsResponse> getHighestRatedSeries(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/on_the_air")
    Call<TvShowsResponse> getAiringSeries(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}")
    Call<TvShowDetail> getTvShow(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<TvShowDetail> getMovie(@Path("id") int id, @Query("api_key") String apiKey);


    @GET("tv/{id}/credits")
    Call<CreditsResponse> getSeriesCredits(@Path("id") int tvSeriesId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("data/rss.php?file=topstories.xml")
    Call<NewsResponse> getNews();

    @GET("tv/{id}/season/{season}")
    Call<Season> getSeason(@Path("id") int id, @Path("season") Integer season, @Query("api_key") String apiKey);

    @GET("tv/{id}/season/{season}/episode/{episode}/credits")
    Call<CreditsResponse> getEpisodeCredits(@Path("id") int id, @Path("season") Integer season, @Path("episode") Integer episode, @Query("api_key") String apiKey);

    @GET("search/multi")
    Call<SearchResponse> getSearchResults(@Query("query") String query, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("person/{id}")
    Call<Actor> getActor(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getActorFilmography(@Query("sort_by") String sortBy, @Query("with_people") String withPeople, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/images")
    Call<ImageResponse> getMovieImages(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}/images")
    Call<ImageResponse> getSeriesImages(@Path("id") int tvSeriesId, @Query("api_key") String apiKey);


    //authentication
    @GET("authentication/token/new")
    Call<Token> requestToken(@Query("api_key") String apiKey);

    @GET("authentication/session/new")
    Call<Session> startSession(@Query("api_key") String apiKey, @Query("request_token") String token);

    @GET("authentication/token/validate_with_login")
    Call<Token> validateToken(@Query("api_key") String apiKey, @Query("username") String username, @Query("password") String password, @Query("request_token") String token);

    @GET("account")
    Call<Account> getAccount(@Query("api_key") String apiKey, @Query("session_id") String session_id);


    //favourite
    @GET("account/{id}/favorite/movies")
    Call<MoviesResponse> getUserFavoriteMovies(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @GET("account/{id}/favorite/tv")
    Call<TvShowsResponse> getUserFavoriteSeries(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @POST("account/{id}/favorite")
    Call<PostResponse> addFavorite(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id,@Body BodyFavourite favorite );


    //watchlist
    @GET("account/{id}/watchlist/movies")
    Call<MoviesResponse> getUserMovieWatchlist(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @GET("account/{id}/watchlist/tv")
    Call<TvShowsResponse> getUserSeriesWatchlist(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @POST("account/{id}/watchlist")
    Call<PostResponse> addWatchlist(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Body BodyWatchlist watchlist);


    //rating
    @GET("account/{id}/rated/movies")
    Call<MoviesResponse> getUserRatedMovies(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @GET("account/{id}/rated/tv")
    Call<TvShowsResponse> getUserRatedSeries(@Path("id") Integer accountId, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Query("sort_by") String sortBy, @Query("page") int page);

    @POST("movie/{id}/rating")
    Call<PostResponse> rateMovie(@Path("id") Integer id, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Body BodyRating rating);

    @POST("tv/{id}/rating")
    Call<PostResponse> rateTVSeries(@Path("id") Integer id, @Query("api_key") String apiKey, @Query("session_id") String session_id, @Body BodyRating rating);

    @DELETE("movie/{id}/rating")
    Call<PostResponse> removeRatingMovie(@Path("id") Integer id, @Query("api_key") String apiKey, @Query("session_id") String session_id);

    @DELETE("tv/{id}/rating")
    Call<PostResponse> removeRatingSeries(@Path("id") Integer id, @Query("api_key") String apiKey, @Query("session_id") String session_id);


}
