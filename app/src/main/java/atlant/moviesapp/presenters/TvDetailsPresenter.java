package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShowDetail;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.TvDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 16.04.2017..
 */

public class TvDetailsPresenter {

    private TvDetailsView view;

    public TvDetailsPresenter(TvDetailsView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<CreditsResponse> callCredits;
    private Call<TvShowDetail> call;

    private Call<PostResponse> postCall;
    private Call<PostResponse> postCallWatchlist;

    public void getCredits(int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        callCredits = apiservice.getSeriesCredits(id, API_KEY);
        callCredits.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Cast> cast = response.body().getCast();
                    Log.d("CAST", cast.size() + "");
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

    public void getDetails(int id) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getTvShow(id, API_KEY);
        call.enqueue(new Callback<TvShowDetail>() {
            @Override
            public void onResponse(Call<TvShowDetail> call, Response<TvShowDetail> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.d("T",response.toString());
                    TvShowDetail series= response.body();

                    view.showDetails(series);

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }


            }

            @Override
            public void onFailure(Call<TvShowDetail> call, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }
    public void postFavorite(int id, String session_id, BodyFavourite favorite) {
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
    public void postWatchlist(int id, String session_id, BodyWatchlist watchlist) {
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
