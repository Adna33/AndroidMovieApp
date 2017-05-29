package atlant.moviesapp.realm;

import com.google.gson.annotations.SerializedName;

import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Review;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmMovie extends RealmObject {
    //ovdje se cuvaju svi ostali podaci koji nisu u movie klasi

    @PrimaryKey
    private Integer id;

    private Crew director;
    private RealmList<Crew> writers;
    private RealmList<Cast> actors;
    private RealmList<Review> reviews;

    public RealmMovie() {
    }

    public Crew getDirector() {
        return director;
    }

    public void setDirector(Crew director) {
        this.director = director;
    }

    public RealmList<Crew> getWriters() {
        return writers;
    }

    public void setWriters(RealmList<Crew> writers) {
        this.writers = writers;
    }

    public RealmList<Cast> getActors() {
        return actors;
    }

    public void setActors(RealmList<Cast> actors) {
        this.actors = actors;
    }

    public RealmList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(RealmList<Review> reviews) {
        this.reviews = reviews;
    }
}
