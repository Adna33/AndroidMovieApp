package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 26.04.2017..
 */

public class Video {

    @SerializedName("id")
    private String id;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private Integer size;

    @SerializedName("type")
    private String type;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}

