package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 06.05.2017..
 */

public class BodyFavourite {

    @SerializedName("media_type")
    public String mediaType;

    @SerializedName("media_id")
    public Integer mediaId;

    @SerializedName("favorite")
    public Boolean favorite;

    public BodyFavourite(String mediaType, Integer mediaId, Boolean favorite) {
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
    }

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

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
