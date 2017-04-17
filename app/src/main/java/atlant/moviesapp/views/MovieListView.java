package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Movie;

/**
 * Created by Korisnik on 12.04.2017..
 */

public interface MovieListView {

    public void showMovies(List<Movie> data);
}
