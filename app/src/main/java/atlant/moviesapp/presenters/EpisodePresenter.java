package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.EpisodeView;
import atlant.moviesapp.views.TvDetailsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 20.04.2017..
 */

public class EpisodePresenter {

    private EpisodeView view;

    public EpisodePresenter(EpisodeView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;

    private Call<CreditsResponse> callCredits;

    public void getCredits(int id, int season, int episode) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        callCredits = apiservice.getEpisodeCredits(id, season, episode, API_KEY);
        callCredits.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Cast> cast = response.body().getCast();
                    view.showCast(cast);

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

    public void onStop() {
        if (callCredits != null)
            callCredits.cancel();
    }

    public void onDestroy() {
        if (callCredits != null)
            callCredits.cancel();
        view = null;
    }
}
