package atlant.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Korisnik on 19.04.2017..
 */

public class Episode implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("guest_stars")
    private List<Cast> crew;

    @SerializedName("episode_number")
    private int episodeNumber;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("still_path")
    private String image;

    @SerializedName("vote_average")
    private double rating;

    protected Episode(Parcel in) {
        id=in.readInt();
        airDate = in.readString();
        episodeNumber = in.readInt();
        name = in.readString();
        overview = in.readString();
        seasonNumber = in.readInt();
        image = in.readString();
        rating = in.readDouble();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public List<Cast> getCrew() {
        return crew;
    }

    public void setCrew(List<Cast> crew) {
        this.crew = crew;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public String getRatingString(){return String.format("%.2f", rating);}

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImagePath() {
        return "http://image.tmdb.org/t/p/w500" + image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(airDate);
        dest.writeInt(episodeNumber);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeInt(seasonNumber);
        dest.writeString(image);
        dest.writeDouble(rating);
    }
}
