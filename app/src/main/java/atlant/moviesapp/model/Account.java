package atlant.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 04.05.2017..
 */

public class Account implements Parcelable {

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
        id = in.readInt();
        includeAdult = (Boolean) in.readValue(Boolean.class.getClassLoader());
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

    public void addFavouriteMovie(Integer id) {
        this.favouriteMovies.add(id);
    }

    public void addFavouriteShow(Integer id) {
        this.favouriteSeries.add(id);
    }

    public void addWatchlistMovie(Integer id) {
        this.watchListMovies.add(id);
    }

    public void addWatchlistShow(Integer id) {
        this.watchListSeries.add(id);
    }

    public void addMovieRating(Integer id) {
        this.ratedMovies.add(id);
    }

    public void addShowRating(Integer id) {
        this.ratedSeries.add(id);
    }

    public void removeFavouriteMovie(Integer id) {
        for (int i = 0; i < this.getFavouriteMovies().size(); i++) {
            if (this.getFavouriteMovies().get(i).equals(id))
                this.favouriteMovies.remove(i);

        }
    }
    public void removeFavoriteShow(Integer id) {
        for (int i = 0; i < this.getFavouriteSeries().size(); i++) {
            if (this.getFavouriteSeries().get(i).equals(id))
                this.favouriteSeries.remove(i);

        }
    }
    public void removeWatchlistMovie(Integer id) {
        for (int i = 0; i < this.getWatchListMovies().size(); i++) {
            if (this.getWatchListMovies().get(i).equals(id))
                this.watchListMovies.remove(i);

        }
    }
    public void removeWatchlistShow(Integer id) {
        for (int i = 0; i < this.getWatchListSeries().size(); i++) {
            if (this.getWatchListSeries().get(i).equals(id))
                this.watchListSeries.remove(i);

        }
    }
    public void removeRatingShow(Integer id) {
        for (int i = 0; i < this.getRatedSeries().size(); i++) {
            if (this.getRatedSeries().get(i).equals(id))
                this.ratedSeries.remove(i);

        }
    }
    public void removeRatingMovies(Integer id) {
        for (int i = 0; i < this.getRatedMovies().size(); i++) {
            if (this.getRatedMovies().get(i).equals(id))
                this.ratedMovies.remove(i);

        }
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
