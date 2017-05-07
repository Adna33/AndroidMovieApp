package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.ReviewsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.MovieDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 15.04.2017..
 */

public class MovieDetailsPresenter {

    private MovieDetailsView view;

    public MovieDetailsPresenter(MovieDetailsView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<ReviewsResponse> call;
    private Call<CreditsResponse> callCredits;

    public void getCredits(final int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        callCredits = apiservice.getMovieCredits(id, API_KEY);
        callCredits.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Cast> cast = response.body().getCast();
                    Log.d("REV", id + "");
                    List<Crew> crew = response.body().getCrew();
                    view.showCast(cast);
                    view.showCrew(crew);

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void getReviews(int id) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        call = apiservice.getMovieReviews(id, API_KEY);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Review> reviews = response.body().getResults();
                    //Log.d("REVIEWS",reviews.size()+"");
                    view.showReviews(reviews);

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }


            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());
            }
        });


    }

    public void onStop() {
        if (call != null)
            call.cancel();
        if (callCredits != null)
            callCredits.cancel();
    }

    public void onDestroy() {
        if (call != null)
            call.cancel();
        if (callCredits != null)
            callCredits.cancel();
        view = null;
    }
}