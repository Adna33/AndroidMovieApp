package atlant.moviesapp.views;

import java.util.List;

import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.TvShow;

/**
 * Created by Korisnik on 16.04.2017..
 */

public interface TvDetailsView {
    void showCast(List<Cast> cast);
    void showCrew(List<Crew> crew);
    void showPoster(TvShow series);
    void showReviews(List<Review> reviews);
}
