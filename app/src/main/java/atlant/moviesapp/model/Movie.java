package atlant.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Parcelable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("backdrop_path")
    private String backdropPath;

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


    public Movie(Integer id, String title, String posterPath, String overview, double rating, String releaseDate, boolean video,List<Integer> genreIds, String backdropPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.video = video;
        this.genreIds = genreIds;
        this.backdropPath=backdropPath;
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getRatingString(){return String.format("%.2f", rating);}

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
    public String getBackdropImagePath(){
        return "http://image.tmdb.org/t/p/w500" + backdropPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeDouble(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeList(this.genreIds);
        dest.writeByte(isVideo() ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);


    }
    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.rating = in.readDouble();
        this.releaseDate = in.readString();
        genreIds = new ArrayList<Integer>();
        in.readList(genreIds, null);
        this.video = in.readByte() != 0;
        this.backdropPath=in.readString();

    }
}
