package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 21.04.2017..
 */

public class SearchResult {

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("id")
    private Integer id;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private float rating;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();

    @SerializedName("name")
    public String name;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("title")
    private String title;
    @SerializedName("video")
    private boolean video;

    @SerializedName("backdrop_path")
    private String backdropPath;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
    public String getBackdropImagePath(){
        return "http://image.tmdb.org/t/p/w500" + backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Movie getMovie() {
        if (mediaType.equals("movie")) {
            return new Movie(id, title, posterPath, overview, rating, releaseDate, video, genreIds,backdropPath);
        } else return null;
    }

    public TvShow getTvShow() {
        if (mediaType.equals("tv")) {
            return new TvShow(id, name, posterPath, overview, rating, firstAirDate, genreIds);
        } else return null;
    }

    public String getImagePath() {
        return "http://image.tmdb.org/t/p/w500" + posterPath;

    }

    public String getRatingString() {
        return String.format("%.2f", rating);
    }
}
