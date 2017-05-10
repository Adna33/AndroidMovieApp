package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Actor;
import atlant.moviesapp.model.Movie;

/**
 * Created by Korisnik on 21.04.2017..
 */

public interface ActorView {
    void showActor(Actor actor);
    void showMovies(List<Movie> data);
}
