package atlant.moviesapp.realm.models;

import java.util.List;

import atlant.moviesapp.model.Episode;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 24.05.2017..
 */

public class RealmSeason extends RealmObject {
    @PrimaryKey
    private String id;

    private RealmList<Episode> episodes;

    public RealmSeason() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(RealmList<Episode> episodes) {
        this.episodes = episodes;
    }
}
