package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Backdrop;
import atlant.moviesapp.model.ImageResponse;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.MoviesResponse;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.GalleryView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 11.06.2017..
 */

public class GalleryPresenter {

    GalleryView view;

    public GalleryPresenter(GalleryView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<ImageResponse> call;

    public void getImages(final int tag, final int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        if (tag == 0)
            call = apiservice.getMovieImages(id, API_KEY);
        else call = apiservice.getSeriesImages (id, API_KEY);


        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Backdrop> backdrops= response.body().getBackdrops();
                    view.showImages(backdrops);
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                if (call.isCanceled()) {

                    Log.e("TAG", "request was cancelled");
                }

                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });


    }

}
