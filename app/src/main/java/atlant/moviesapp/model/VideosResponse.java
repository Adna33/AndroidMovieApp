package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Korisnik on 26.04.2017..
 */

public class VideosResponse {
    @SerializedName("id")
    private Integer id;

    @SerializedName("results")
    private List<Video> videos = null;

    public Integer getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
