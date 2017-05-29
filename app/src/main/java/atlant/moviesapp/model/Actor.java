package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Korisnik on 25.04.2017..
 */

public class Actor extends RealmObject {

    @SerializedName("biography")
    private String biography;

    @SerializedName("birthday")
    private String birthday;


    @SerializedName("homepage")
    private String homepage;

    @SerializedName("id")
    @PrimaryKey
    private int id;

    @SerializedName("imdb_id")
    private String imdbId;


    @SerializedName("name")
    private String name;

    @SerializedName("place_of_birth")
    private String placeOfBirth;

    @SerializedName("profile_path")
    private String profilePath;

    public void setActor(Actor a) {
        this.imdbId = a.imdbId;
        this.name = a.name;
        this.placeOfBirth = a.placeOfBirth;
        this.profilePath = a.profilePath;
        this.homepage = a.homepage;
        this.birthday = a.birthday;
        this.biography = a.biography;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getImagePath() {
        return "http://image.tmdb.org/t/p/w500" + profilePath;

    }
}
