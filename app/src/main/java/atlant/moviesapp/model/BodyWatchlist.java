package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 06.05.2017..
 */

public class BodyWatchlist {

    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("media_id")
    public Integer mediaId;

    @SerializedName("watchlist")
    public Boolean watchlist;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public Boolean getWatchlist() {
        return watchlist;
    }

    public void setWatchlist(Boolean watchlist) {
        this.watchlist = watchlist;
    }

    public BodyWatchlist(String mediaType, Integer mediaId, Boolean watchlist) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.watchlist = watchlist;
    }
}
