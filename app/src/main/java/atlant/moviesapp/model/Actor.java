package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 25.04.2017..
 */

public class Actor {

    @SerializedName("biography")
    private String biography;

    @SerializedName("birthday")
    private String birthday;


    @SerializedName("homepage")
    private String homepage;

    @SerializedName("id")
    private int id;

    @SerializedName("imdb_id")
    private String imdbId;


    @SerializedName("name")
    private String name;

    @SerializedName("place_of_birth")
    private String placeOfBirth;

    @SerializedName("profile_path")
    private String profilePath;

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
