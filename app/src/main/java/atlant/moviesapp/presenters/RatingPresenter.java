package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.ApplicationState;
import atlant.moviesapp.model.BodyRating;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.SearchResponse;
import atlant.moviesapp.model.SearchResult;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.RatingView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 06.05.2017..
 */

public class RatingPresenter {

    RatingView view;
    private static final int MOVIE = 0;
    private static final int TVSHOW = 1;

    public RatingPresenter(RatingView view) {
        this.view = view;
    }

    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<PostResponse> ratingCall;

    public void addRatingToList(int TAG,int id)
    { if(TAG==0)
    {
        RealmUtil.getInstance().addMovieRating(id);
        ApplicationState.getUser().addMovieRating(id);

    }
    else {
        RealmUtil.getInstance().addSeriesRatings(id);
        ApplicationState.getUser().addFavouriteShow(id);
    }}

    public void postRatingRealm(int TAG, int id,float rating) {
        if (TAG == 0) {
            if (RealmUtil.getInstance().getPostMovie(id) == null) {
                RealmUtil.getInstance().createPostMovie(id);
            }
            RealmUtil.getInstance().setMovieRating(RealmUtil.getInstance().getPostMovie(id), String.format("%.2f", rating));
        } else {

            if (RealmUtil.getInstance().getPostSeries(id) == null) {
                RealmUtil.getInstance().createPostSeries(id);
            }
            RealmUtil.getInstance().setSeriesRating(RealmUtil.getInstance().getPostSeries(id), String.format("%.2f", rating));
        }
    }

    public void postRating(int id, String session_id, Double r, int tag) {
        BodyRating rating=new BodyRating(r);
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        if (tag == MOVIE)
            ratingCall = apiservice.rateMovie(id, API_KEY, session_id, rating);
        else if (tag == TVSHOW)
            ratingCall = apiservice.rateTVSeries(id, API_KEY, session_id, rating);
        ratingCall.enqueue(new Callback<PostResponse>() {
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
        if (ratingCall != null)
            ratingCall.cancel();
    }

    public void onDestroy() {
        if (ratingCall != null)
            ratingCall.cancel();
        view = null;
    }

}
