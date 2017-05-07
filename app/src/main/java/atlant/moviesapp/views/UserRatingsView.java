package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.TvShow;

/**
 * Created by Korisnik on 07.05.2017..
 */

public interface UserRatingsView {
    void showMovies(List<Movie> data);
    void showTvShows(List<TvShow> data);
    void showProgress();
    void hideProgress();
    void showError();

}
