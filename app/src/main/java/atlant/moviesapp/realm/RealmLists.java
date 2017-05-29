package atlant.moviesapp.realm;

import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Movie;
import atlant.moviesapp.model.Season;
import atlant.moviesapp.model.TvShow;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmLists extends RealmObject{
    @PrimaryKey
    private int id;
    private RealmList<Movie> mostPopularMovies;
    private RealmList<Movie> latestMovies;
    private RealmList<Movie> highestRatedMovies;

    private RealmList<TvShow> mostPopularSeries;
    private RealmList<TvShow> latestSeries;
    private RealmList<TvShow> highestRatedSeries;
    private RealmList<TvShow> airingSeries;

    public RealmList<Movie> getMostPopularMovies() {
        return mostPopularMovies;
    }

    public void setMostPopularMovies(RealmList<Movie> mostPopularMovies) {
        this.mostPopularMovies = mostPopularMovies;
    }

    public RealmList<Movie> getLatestMovies() {
        return latestMovies;
    }

    public void setLatestMovies(RealmList<Movie> latestMovies) {
        this.latestMovies = latestMovies;
    }

    public RealmList<Movie> getHighestRatedMovies() {
        return highestRatedMovies;
    }

    public void setHighestRatedMovies(RealmList<Movie> highestRatedMovies) {
        this.highestRatedMovies = highestRatedMovies;
    }

    public RealmList<TvShow> getMostPopularSeries() {
        return mostPopularSeries;
    }

    public void setMostPopularSeries(RealmList<TvShow> mostPopularSeries) {
        this.mostPopularSeries = mostPopularSeries;
    }

    public RealmList<TvShow> getLatestSeries() {
        return latestSeries;
    }

    public void setLatestSeries(RealmList<TvShow> latestSeries) {
        this.latestSeries = latestSeries;
    }

    public RealmList<TvShow> getHighestRatedSeries() {
        return highestRatedSeries;
    }

    public void setHighestRatedSeries(RealmList<TvShow> highestRatedSeries) {
        this.highestRatedSeries = highestRatedSeries;
    }

    public RealmList<TvShow> getAiringSeries() {
        return airingSeries;
    }

    public void setAiringSeries(RealmList<TvShow> airingSeries) {
        this.airingSeries = airingSeries;
    }

    public RealmLists() {
    }
}
