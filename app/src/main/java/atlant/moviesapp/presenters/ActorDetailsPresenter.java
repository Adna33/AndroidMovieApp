package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.ReviewsResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.ActorView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 25.04.2017..
 */

public class ActorDetailsPresenter {

    ActorView view;

    public ActorDetailsPresenter(ActorView view) {
        this.view = view;
    }
    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<Actor> call;
    private Call<MoviesResponse> filmCall;

    public void getActor(final int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        call = apiservice.getActor(id, API_KEY);
        call.enqueue(new Callback<Actor>() {
            @Override
            public void onResponse(Call<Actor> call, Response<Actor> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Actor actor= response.body();
                    view.showActor(actor);

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }

            }

            @Override
            public void onFailure(Call<Actor> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }
    public void getHighestRatedMovies(int actorID) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        filmCall = apiservice.getActorFilmography("popularity.desc",actorID+"",API_KEY);


        filmCall.enqueue(new Callback<MoviesResponse>() {
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
                if (call.isCanceled()) {
                    Log.e("TAG", "request was cancelled");
                }
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }
    public void onStop() {
        if(call!=null)
            call.cancel();
    }
    public void onDestroy()
    {
        if(call!=null)
            call.cancel();
        view=null;
    }

}
