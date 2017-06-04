package atlant.moviesapp.realm.models;

import com.google.gson.annotations.SerializedName;

import atlant.moviesapp.model.Cast;
import atlant.moviesapp.model.Crew;
import atlant.moviesapp.model.Review;
import atlant.moviesapp.model.Season;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmSeries extends RealmObject{
    @PrimaryKey
    private Integer id;

    private Crew director;
    private RealmList<Crew> writers;
    private RealmList<Cast> actors;
    private RealmList<Season> seasons;

    public RealmSeries() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public RealmList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(RealmList<Season> seasons) {
        this.seasons = seasons;
    }
}
