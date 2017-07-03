package atlant.moviesapp.realm.models;

import atlant.moviesapp.model.Cast;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmEpisode extends RealmObject {

    @PrimaryKey
    private String id;
    private int seasonNumber;
    private int episodeNumber;

    private RealmList<Cast> cast;

    public RealmEpisode() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Cast> getCast() {
        return cast;
    }

    public void setCast(RealmList<Cast> cast) {
        this.cast = cast;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
}
