package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Video;
import atlant.moviesapp.model.VideosResponse;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.YouTubeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 26.04.2017..
 */

public class YouTubePresenter {

    YouTubeView view;
    private static final String API_KEY = BuildConfig.API_KEY;

    public YouTubePresenter(YouTubeView view) {
        this.view = view;
    }

    private Call<VideosResponse> ytCall;

    public void getVideo(int id) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        ytCall = apiservice.getMovieVideos(id, API_KEY);
        ytCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> ytCall, Response<VideosResponse> response) {
                int statusCode = response.code();

                if (statusCode == 200) {

                    List<Video> videos = response.body().getVideos();

                    for (int i = 0; i < videos.size(); i++) {
                        if (videos.get(i).getType().equals("Trailer") && videos.get(i).getSite().equals("YouTube")) {

                            view.ShowVideo(videos.get(i).getKey());
                            break;
                        }
                    }


                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> ytCall, Throwable t) {
                //TODO onFailure
                Log.e("TAG", t.toString());

            }
        });

    }

    public void onStop() {
        if (ytCall != null)
            ytCall.cancel();
    }

    public void onDestroy() {
        if (ytCall != null)
            ytCall.cancel();
        view = null;
    }
}
