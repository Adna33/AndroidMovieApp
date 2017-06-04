package atlant.moviesapp.realm.models;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmAccount extends RealmObject {

    private RealmList<RealmInt> favoriteMovies;
    private RealmList<RealmInt> watchlistMovies;
    private RealmList<RealmInt> ratedMovies;
    
    private RealmList<RealmInt> favoriteSeries;
    private RealmList<RealmInt> watchlistSeries;
    private RealmList<RealmInt> ratedSeries;

    public RealmAccount() {
    }

    public RealmList<RealmInt> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(RealmList<RealmInt> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
    }

    public RealmList<RealmInt> getWatchlistMovies() {
        return watchlistMovies;
    }

    public void setWatchlistMovies(RealmList<RealmInt> watchlistMovies) {
        this.watchlistMovies = watchlistMovies;
    }

    public RealmList<RealmInt> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(RealmList<RealmInt> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public RealmList<RealmInt> getFavoriteSeries() {
        return favoriteSeries;
    }

    public void setFavoriteSeries(RealmList<RealmInt> favoriteSeries) {
        this.favoriteSeries = favoriteSeries;
    }

    public RealmList<RealmInt> getWatchlistSeries() {
        return watchlistSeries;
    }

    public void setWatchlistSeries(RealmList<RealmInt> watchlistSeries) {
        this.watchlistSeries = watchlistSeries;
    }

    public RealmList<RealmInt> getRatedSeries() {
        return ratedSeries;
    }

    public void setRatedSeries(RealmList<RealmInt> ratedSeries) {
        this.ratedSeries = ratedSeries;
    }
}
