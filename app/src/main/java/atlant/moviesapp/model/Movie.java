package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("release_date")
    private String releaseDate;


    @SerializedName("video")
    private boolean video;


    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();



    //TODO for details:  crew- director,writers, stars(cast), reviews


    public Movie(Integer id, String title, String posterPath, String overview, double rating, String releaseDate, boolean video) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.video = video;
    }


    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getRatingString() {
        return Double.toString(rating);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getImagePath() {
        return "http://image.tmdb.org/t/p/w500" + posterPath;

    }
}
