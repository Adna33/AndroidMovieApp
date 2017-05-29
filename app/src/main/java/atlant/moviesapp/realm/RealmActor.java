package atlant.moviesapp.realm;

import atlant.moviesapp.model.Movie;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 26.05.2017..
 */

public class RealmActor extends RealmObject {

    @PrimaryKey
    private int id;

    private RealmList<Movie> movies;

    public RealmActor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(RealmList<Movie> movies) {
        this.movies = movies;
    }
}
