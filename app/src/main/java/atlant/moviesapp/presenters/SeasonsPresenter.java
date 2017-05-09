package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.model.Season;
import atlant.moviesapp.model.TvShowDetail;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.NewsFeedView;
import atlant.moviesapp.views.SeasonsView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 19.04.2017..
 */

public class SeasonsPresenter {
    SeasonsView view;
    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<Season> call;
    private List<Episode> episodes;

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public SeasonsPresenter(SeasonsView view) {
        this.view = view;
    }

    public void getSeasonEpisodes(int id,int season) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getSeason(id,season, API_KEY);
        call.enqueue(new Callback<Season>() {
            @Override
            public void onResponse(Call<Season> call, Response<Season> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    episodes= response.body().getEpisodes();
                    view.ShowEpisodes(episodes);
                    view.ShowYear(response.body().getAirYear());

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }


            }

            @Override
            public void onFailure(Call<Season> call, Throwable t) {
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
