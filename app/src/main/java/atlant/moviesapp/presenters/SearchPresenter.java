package atlant.moviesapp.presenters;

import android.util.Log;

import java.util.List;

import atlant.moviesapp.BuildConfig;
import atlant.moviesapp.model.Episode;
import atlant.moviesapp.model.NewsResponse;
import atlant.moviesapp.model.SearchResponse;
import atlant.moviesapp.model.SearchResult;
import atlant.moviesapp.model.Season;
import atlant.moviesapp.rest.ApiClient;
import atlant.moviesapp.rest.ApiInterface;
import atlant.moviesapp.views.SearchView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Korisnik on 21.04.2017..
 */

public class SearchPresenter {

    private SearchView view;
    private static final String API_KEY = BuildConfig.API_KEY;
    private Call<SearchResponse> call;

    public SearchPresenter(SearchView view) {
        this.view = view;
    }

    public void getSearchResults(String query, int page) {
        final ApiInterface apiservice = ApiClient.getClient().create(ApiInterface.class);
        call = apiservice.getSearchResults(query, API_KEY, page);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {

                    List<SearchResult> results = response.body().getResults();
                    Log.d("EZ", results.size() + " ");
                    view.DisplayResults(results);

                } else {
                    //TODO onFailure
                    Log.e("TAG", "Error");
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
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
