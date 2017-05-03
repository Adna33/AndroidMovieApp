package atlant.moviesapp.rest;

/**
 * Created by Korisnik on 07.04.2017..
 */

import atlant.moviesapp.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("discover/movie")
    Call<MoviesResponse> discoverMovies(@Query("sort_by") String sortBy,@Query("api_key") String apiKey, @Query("page") int page);

    @GET("discover/tv")
    Call<TvShowsResponse> discoverSeries(@Query("sort_by") String sortBy,@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<MoviesResponse> getHighestRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/top_rated")
    Call<TvShowsResponse> getHighestRatedSeries(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("tv/on_the_air")
    Call<TvShowsResponse> getAiringSeries(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("tv/{id}")
    Call<TvShowDetail> getTvShow(@Path("id") int id, @Query("api_key") String apiKey);


    @GET("tv/{id}/credits")
    Call<CreditsResponse> getSeriesCredits(@Path("id") int tvSeriesId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("tv/{id}/reviews")
    Call<ReviewsResponse> getSeriesReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("review/{id}")
    Call<Review> getReviewDetail(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("data/rss.php?file=topstories.xml")
    Call<NewsResponse> getNews();

    @GET("tv/{id}/season/{season}")
    Call<Season> getSeason(@Path("id") int id, @Path("season") Integer season, @Query("api_key") String apiKey);

    @GET("tv/{id}/season/{season}/episode/{episode}/credits")
    Call<CreditsResponse> getEpisodeCredits(@Path("id") int id,@Path("season") Integer season,@Path("episode") Integer episode,@Query("api_key") String apiKey);

    @GET("search/multi")
    Call<SearchResponse> getSearchResults(@Query("query") String query, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("person/{id}")
    Call<Actor> getActor(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("discover/movie")
    Call<MoviesResponse> getActorFilmography(@Query("sort_by") String sortBy ,@Query("with_people") String withPeople ,@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<VideosResponse> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

}
