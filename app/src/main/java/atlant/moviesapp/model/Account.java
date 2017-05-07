package atlant.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class Account implements Parcelable{

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("include_adult")
    private Boolean includeAdult;

    @SerializedName("username")
    private String username;

    private String sessionId;
    private List<Integer> favouriteMovies = new ArrayList<>();
    private List<Integer> ratedMovies = new ArrayList<>();
    private List<Integer> watchListMovies = new ArrayList<>();

    private List<Integer> ratedSeries = new ArrayList<>();
    private List<Integer> favouriteSeries = new ArrayList<>();
    private List<Integer> watchListSeries = new ArrayList<>();

    public Account() {
    }

    protected Account(Parcel in) {
        name = in.readString();
        username = in.readString();
        sessionId = in.readString();
        id=in.readInt();
        includeAdult=(Boolean) in.readValue(Boolean.class.getClassLoader());
        this.favouriteMovies = new ArrayList<Integer>();
        in.readList(this.favouriteMovies, Integer.class.getClassLoader());
        this.favouriteSeries = new ArrayList<Integer>();
        in.readList(this.favouriteSeries, Integer.class.getClassLoader());
        this.ratedMovies = new ArrayList<Integer>();
        in.readList(this.ratedMovies, Integer.class.getClassLoader());
        this.ratedSeries = new ArrayList<Integer>();
        in.readList(this.ratedSeries, Integer.class.getClassLoader());
        this.watchListMovies = new ArrayList<Integer>();
        in.readList(this.watchListMovies, Integer.class.getClassLoader());
        this.watchListSeries = new ArrayList<Integer>();
        in.readList(this.watchListSeries, Integer.class.getClassLoader());
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIncludeAdult() {
        return includeAdult;
    }

    public void setIncludeAdult(Boolean includeAdult) {
        this.includeAdult = includeAdult;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Integer> getFavouriteMovies() {
        return favouriteMovies;
    }

    public void setFavouriteMovies(List<Integer> favouriteMovies) {
        this.favouriteMovies = favouriteMovies;
    }

    public List<Integer> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(List<Integer> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public List<Integer> getWatchListMovies() {
        return watchListMovies;
    }

    public void setWatchListMovies(List<Integer> watchListMovies) {
        this.watchListMovies = watchListMovies;
    }

    public List<Integer> getRatedSeries() {
        return ratedSeries;
    }

    public void setRatedSeries(List<Integer> ratedSeries) {
        this.ratedSeries = ratedSeries;
    }

    public List<Integer> getFavouriteSeries() {
        return favouriteSeries;
    }

    public void setFavouriteSeries(List<Integer> favouriteSeries) {
        this.favouriteSeries = favouriteSeries;
    }

    public List<Integer> getWatchListSeries() {
        return watchListSeries;
    }

    public void setWatchListSeries(List<Integer> watchListSeries) {
        this.watchListSeries = watchListSeries;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(sessionId);
        dest.writeValue(id);
        dest.writeValue(includeAdult);
        dest.writeList(favouriteMovies);
        dest.writeList(ratedMovies);
        dest.writeList(watchListMovies);
        dest.writeList(favouriteSeries);
        dest.writeList(watchListSeries);
        dest.writeList(ratedSeries);

    }
}
