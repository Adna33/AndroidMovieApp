package atlant.moviesapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Korisnik on 19.04.2017..
 */

public class Genre {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
