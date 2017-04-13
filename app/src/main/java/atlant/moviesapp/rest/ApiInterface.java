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

    //discover/movie?sort_by=popularity.desc
    //discover/movie?sort_by=release_date.desc
    @GET("discover/movie?sort_by=popularity.desc")
    Call<MoviesResponse> discoverMovies(@Query("sort_by") String sortBy,@Query("api_key") String apiKey);

    //@GET("discover/movie?sort_by=vote_average.desc")
    @GET("movie/top_rated")
    Call<MoviesResponse> getHighestRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("genre/{id}")
    Call<MovieGenre> getGenreDetails(@Path("id") int id, @Query("api_key") String apiKey);

    //http://api.themoviedb.org/3/movie/83542/reviews?api_key=###
    @GET("movie/{id}/reviews")
    Call<MoviesResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    //full details for review: http://api.themoviedb.org/3/review/51910979760ee320eb020fc2?api_key=###
    //TODO reviewResponse
    @GET("review/{id}")
    Call<MoviesResponse> getReviewDetail(@Path("id") int id, @Query("api_key") String apiKey);

    //genre details: https://api.themoviedb.org/3/genre/movie/list?api_key=<<api_key>>&language=en-US

}
