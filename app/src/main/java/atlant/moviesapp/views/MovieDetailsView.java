package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.Review;

/**
 * Created by Korisnik on 15.04.2017..
 */

public interface MovieDetailsView {

    void showCast(List<Cast> cast);
    void showCrew(List<Crew> crew);
    void showPoster(Movie movie);
    void showReviews(List<Review> reviews);

}
