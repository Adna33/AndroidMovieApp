package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;

/**
 * Created by Korisnik on 13.04.2017..
 */

public interface TvShowListView {
    public void showTvShows(List<TvShow> data);
    void showProgress();
    void showError();
    void hideProgress();
}
