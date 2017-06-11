package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.R;
import atlant.moviesapp.model.Backdrop;
import atlant.moviesapp.model.BodyFavourite;
import atlant.moviesapp.model.BodyWatchlist;
import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.CreditsResponse;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.ImageResponse;
import atlant.moviesapp.model.PostResponse;
import atlant.moviesapp.model.TvShowDetail;
import atlant.moviesapp.realm.models.RealmSeries;
import atlant.moviesapp.realm.RealmUtil;
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

    public void getCredits(final int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);

        callCredits = apiservice.getSeriesCredits(id, API_KEY);
        callCredits.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    List<Cast> cast = response.body().getCast();
                    List<Crew> crew = response.body().getCrew();
                    RealmUtil.getInstance().addRealmSeriesActors(id, cast);
                    RealmUtil.getInstance().addRealmSeriesWriters(id, crew);
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

    public void setUpTvShow(int id) {
        if (RealmUtil.getInstance().getTvShowDetailFromRealm(id) != null) {
            TvShowDetail details = RealmUtil.getInstance().getTvShowDetailFromRealm(id);
            view.showDetails(details);
        }
        if (RealmUtil.getInstance().getTvShowDetailFromRealm(id) != null) {
            RealmSeries m = RealmUtil.getInstance().getShowDetailsFromRealm(id);
            if (m.getWriters() != null)
                view.showCrew(m.getWriters());
            if (m.getActors() != null)
                view.showCast(m.getActors());
        }
    }

    public void getDetails(int id) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getTvShow(id, API_KEY);
        call.enqueue(new Callback<TvShowDetail>() {
            @Override
            public void onResponse(Call<TvShowDetail> call, Response<TvShowDetail> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.d("T", response.toString());
                    TvShowDetail series = response.body();
                    RealmUtil.getInstance().addRealmSeriesDetails(series.getId(), series);

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
    public void postFavoriteRealm(int seriesId) {
        if (RealmUtil.getInstance().getPostSeries(seriesId) == null) {
            RealmUtil.getInstance().createPostSeries(seriesId);

        }
        RealmUtil.getInstance().addFavoriteSeries(seriesId);
        RealmUtil.getInstance().setSeriesFavorite(RealmUtil.getInstance().getPostSeries(seriesId), true);
    }

    public void removeFavoriteRealm(int seriesId) {
        if (RealmUtil.getInstance().getPostSeries(seriesId) == null) {
            RealmUtil.getInstance().createPostSeries(seriesId);

        }
        RealmUtil.getInstance().deleteRealmInt(seriesId);
        RealmUtil.getInstance().setSeriesFavorite(RealmUtil.getInstance().getPostSeries(seriesId), false);
    }

    public void postWatchlistRealm(int seriesId) {
        if (RealmUtil.getInstance().getPostSeries(seriesId) == null) {
            RealmUtil.getInstance().createPostSeries(seriesId);
        }
        RealmUtil.getInstance().addShowWatchlist(seriesId);
        RealmUtil.getInstance().setSeriesWatchlist(RealmUtil.getInstance().getPostSeries(seriesId), true);

    }

    public void removeWatchlistRealm(int seriesId) {
        if (RealmUtil.getInstance().getPostSeries(seriesId) == null) {
            RealmUtil.getInstance().createPostSeries(seriesId);

        }
        RealmUtil.getInstance().deleteRealmInt(seriesId);
        RealmUtil.getInstance().setSeriesWatchlist(RealmUtil.getInstance().getPostSeries(seriesId), false);
    }

    public void postFavorite(int id, String session_id, Boolean b) {
        BodyFavourite favorite= new BodyFavourite("tv", id, b);
        if (b) {
            RealmUtil.getInstance().addFavoriteSeries(id);
        } else {
            RealmUtil.getInstance().deleteRealmInt(id);
        }
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

    public void postWatchlist(int id, String session_id, Boolean b) {
        BodyWatchlist watchlist=new BodyWatchlist("tv", id, b);
        if (b) {
            RealmUtil.getInstance().addShowWatchlist(id);
        } else {
            RealmUtil.getInstance().deleteRealmInt(id);
        }
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
    private Call<ImageResponse> imageCall;

    public void getImages(final int id) {

        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        imageCall = apiservice.getSeriesImages(id, API_KEY);


        imageCall.enqueue(new Callback<ImageResponse>() {
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
    public void onStop() {
        if (call != null)
            call.cancel();
        if (callCredits != null)
            callCredits.cancel();
        if(imageCall!=null)
            imageCall.cancel();
    }

    public void onDestroy() {
        if (call != null)
            call.cancel();

        if (callCredits != null)
            callCredits.cancel();
        if(imageCall!=null)
            imageCall.cancel();
        view = null;
    }
}
