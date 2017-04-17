package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 11.04.2017..
 */

public class TvShow {

    @SerializedName("id")
    private Integer id;

    @SerializedName("original_name")
    private String originalTitle;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("seasons_season_number")
    private List<Integer> seasons=new ArrayList<>();

    @SerializedName("created_by_names")
    private List<String> writers=new ArrayList<>();

    /*@SerializedName("genre_ids")
    private List<Integer> genreIds=new ArrayList<>();

ili

    @SerializedName("genre_names")
    private List<String> genreNames=new ArrayList<>();

    */

    //TODO crew cast reviews director writers


    public TvShow(Integer id, String originalTitle, String posterPath, String overview, double rating, List<Integer> seasons, List<String> writers) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.seasons = seasons;
        this.writers = writers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }
}
