package atlant.moviesapp.presenters;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import atlant.moviesapp.model.News;
import atlant.moviesapp.model.NewsResponse;
import atlant.moviesapp.model.TvShow;
import atlant.moviesapp.model.TvShowsResponse;
import atlant.moviesapp.realm.RealmUtil;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.MovieListView;
import atlant.moviesapp.views.NewsFeedView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 14.04.2017..
 */

public class NewsFeedPresenter {

    private NewsFeedView view;
    private Call<NewsResponse> call;

    public NewsFeedPresenter(NewsFeedView view) {
        this.view = view;
    }

    public void getNews() {
        final ApiInterface apiservice = ApiClient.getClientNewsFeed().create(ApiInterface.class);
        call = apiservice.getNews();

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Log.d("newfeed","in");
                    List<News> news = response.body().getChannel().getNews();
                    RealmUtil.getInstance().addNewsToRealm(news);
                    view.hideProgress();
                    view.showNews(news);
                } else {
                    view.hideProgress();
                    view.showError();

                    //TODO onFailure
                    Log.e("TAG", "Error News Feed");
                }


            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                //TODO onFailure
                if (call.isCanceled()) {

                }

                //view.hideProgress();
                //view.showError();
                Log.e("TAG", t.toString() + "On Failure News Feed");

            }
        });


    }
    public void setUpNews()
    {
       List<News> list =  RealmUtil.getInstance().getNewsFromRealm();
        view.showNews(list);

    }

    public void onStop() {

    }

    public void onDestroy() {
        if (call != null)
            call.cancel();

    }
}
